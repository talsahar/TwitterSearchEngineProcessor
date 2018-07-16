package main.tal.root;

import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * Dequeue Listener's links from sqs,
 * fetch data from link,
 * take screenshot via selenium with chrome driver,
 * store screenshot on s3
 * store data with screenshot url in rds
 * the stored data will be used by web server to serve user's search queries.
 *
 * Enter the following params as System property/Environment variable
 * @par "S3_BUCKET_NAME"
 * @par "S3_PATH"
 * @par "sqs_url"
 * @par "RDS_DB_NAME"
 * @par "RDS_USERNAME"
 * @par "RDS_PASSWORD"
 * @par "RDS_HOSTNAME"
 * @par "RDS_PORT"
 * @par "RDS_TABLE_NAME"
 * @par "AWS_ACCESS_KEY_ID"
 * @par "AWS_SECRET_ACCESS_KEY"
 */

public class Main {

    public static boolean checkParams() {
        boolean result = true;
        ProjectProperties prop = ProjectProperties.getInstance();
        try {
            for (Field f : prop.getClass().getDeclaredFields())
                if (f.get(prop) == null) {
                    System.out.println(f.getName() + " param is missing");
                    result = false;
                }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        if (checkParams() == false)
            return;
        MapperManager mapperManager = new MapperManager();
        mapperManager.start();
        Scanner s = new Scanner(System.in);
        while(!s.nextLine().equals("q"));
        mapperManager.stop();
    }
}
