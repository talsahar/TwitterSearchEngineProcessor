package main.tal.db;
import main.tal.root.ProjectProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {

    private static Connection connection;

    private static class SqliteConnectionHolder {
        private static final SqliteConnection instance = new SqliteConnection();
    }

    public static SqliteConnection getInstance() {
        return SqliteConnectionHolder.instance;
    }

    private SqliteConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String dbName = ProjectProperties.getInstance().getRds_db_name();
                String userName = ProjectProperties.getInstance().getRds_user_name();
                String password = ProjectProperties.getInstance().getRds_password();
                String hostname = ProjectProperties.getInstance().getRds_hostname();
                String port = ProjectProperties.getInstance().getRds_port();
                String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                Connection con = DriverManager.getConnection(jdbcUrl);
                System.out.println("Remote connection successful.");
                connection = con;
            }
            catch (ClassNotFoundException e) { e.printStackTrace();}
            catch (SQLException e) { e.printStackTrace();}
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        SqliteConnection.getInstance().getConnection();
    }
}
