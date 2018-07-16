package main.tal.db;
import main.tal.root.ProjectProperties;
import java.sql.*;

public class DBManager {

    private final Connection connection;
    private String table_name;
    private int table_limit;

    private static class DBManagerHolder {
        private static final DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return DBManagerHolder.instance;
    }

    private DBManager() {
        this.table_name = ProjectProperties.getInstance().getRds_table_name();
        this.table_limit = ProjectProperties.getInstance().getRds_table_limit();
        this.connection = SqliteConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String create = "CREATE TABLE IF NOT EXISTS " + table_name + " (id INTEGER PRIMARY KEY, "
                    + " track TEXT, title TEXT," + "  content TEXT," + "  url TEXT," + " imageUrl TEXT," + "  description TEXT,"
                    + "  timestamp long)";
            statement.executeUpdate(create);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertData(TwitterLink link) {
        try {
            updateDbSize();
            String query = "INSERT INTO " + table_name
                    + "(id,track,title,content,url,imageUrl,description,timestamp) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            long timestamp = System.currentTimeMillis();
            statement.setInt(1, link.getUrl().hashCode());
            statement.setString(2, link.getTrack());
            statement.setString(3, link.getTitle());
            statement.setString(4, link.getContent());
            statement.setString(5, link.getUrl());
            statement.setString(6, link.getImageUrl());
            statement.setString(7, link.getDescription());
            statement.setLong(8, timestamp);
            System.out.println(statement);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void updateDbSize() {
        try {
            Statement count_statement = connection.createStatement();
            ResultSet count_result = count_statement
                    .executeQuery("SELECT COUNT(*) AS total FROM " + table_name);
            int count = -1;
            if(count_result.next())
            count = count_result.getInt("total");
            count_statement.close();
            int to_delete = count - table_limit;
            if (to_delete > 0) {
                String sql = "DELETE FROM " + table_name + " order by timestamp desc LIMIT " + to_delete;
                Statement delete_statement = connection.createStatement();
                delete_statement.executeUpdate(sql);
                delete_statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}