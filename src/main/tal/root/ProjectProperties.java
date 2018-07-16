package main.tal.root;

public class ProjectProperties {

    static ProjectProperties instance = new ProjectProperties();

    //RDS
    String rds_table_name;
    int rds_table_limit = 1000;
    String rds_db_name;
    String rds_user_name;
    String rds_password;
    String rds_hostname;
    String rds_port;
    String sqs_url;
    String s3_bucket_name;
    String s3_path;

    public static ProjectProperties getInstance() {
        return instance;
    }

    public String getRds_table_name() {
        return rds_table_name;
    }

    public int getRds_table_limit() {
        return rds_table_limit;
    }

    public String getRds_db_name() {
        return rds_db_name;
    }

    public String getRds_user_name() {
        return rds_user_name;
    }

    public String getRds_password() {
        return rds_password;
    }

    public String getRds_hostname() {
        return rds_hostname;
    }

    public String getRds_port() {
        return rds_port;
    }

    public String getSqs_url() {
        return sqs_url;
    }

    public String getS3_bucket_name() {
        return s3_bucket_name;
    }

    public String getS3_path() {
        return s3_path;
    }

    private ProjectProperties() {

        if ((rds_table_name = System.getenv("RDS_TABLE_NAME")) == null) {
            rds_table_name = System.getProperty("RDS_TABLE_NAME");
        }

        if ((rds_db_name = System.getenv("RDS_DB_NAME")) == null) {
            rds_db_name = System.getProperty("RDS_DB_NAME");
        }

        if ((rds_user_name = System.getenv("RDS_USERNAME")) == null) {
            rds_user_name = System.getProperty("RDS_USERNAME");
        }

        if ((rds_password = System.getenv("RDS_PASSWORD")) == null) {
            rds_password = System.getProperty("RDS_PASSWORD");
        }

        if ((rds_hostname = System.getenv("RDS_HOSTNAME")) == null) {
            rds_hostname = System.getProperty("RDS_HOSTNAME");
        }

        if ((rds_port = System.getenv("RDS_PORT")) == null) {
            rds_port = System.getProperty("RDS_PORT");
        }

        if ((sqs_url = System.getenv("sqs_url")) == null) {
            sqs_url = System.getProperty("sqs_url");
        }

        if ((s3_bucket_name = System.getenv("S3_BUCKET_NAME")) == null) {
            s3_bucket_name = System.getProperty("S3_BUCKET_NAME");
        }

        if ((s3_path = System.getenv("S3_PATH")) == null) {
            s3_path = System.getProperty("S3_PATH");
        }
    }
}
