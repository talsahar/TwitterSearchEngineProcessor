package main.tal.root;

import com.alibaba.fastjson.JSON;
import com.amazonaws.services.sqs.model.Message;
import main.tal.db.DBManager;
import main.tal.db.TwitterLink;
import main.tal.metrics.GenericMetrics;
import main.tal.metrics.TimeCalculator;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapperManager {

    int thread_count = 5;
    ExecutorService executor;
    SqsReceiver sqsReceiver;
    boolean is_running;
    Runnable messageHandleProcess;
    JsoupParser jsoup;
    GenericMetrics screenshotMetrics;
    ScreenshotCapture screenShotManager;

    public MapperManager(){
        screenShotManager = new ScreenshotCapture();
        screenshotMetrics = new GenericMetrics
                ("TWEETS/MANUFACTOR", "TimeToTakeScreenshot",
                        "TimeInterval", "Millisecond");
        jsoup = new JsoupParser();
        executor = Executors.newFixedThreadPool(thread_count);
        String sqs = ProjectProperties.getInstance().getSqs_url();
        sqsReceiver = new SqsReceiver(sqs);
        S3BucketManager s3 = new S3BucketManager();
        messageHandleProcess = () -> {
                while (is_running) {
                    Message message = sqsReceiver.receiveMessage();
                    if(message != null) {
                        SqsReceiver.TwitterSqsObject sqsObj = JSON
                                .parseObject(message.getBody(), SqsReceiver.TwitterSqsObject.class);
                        TwitterLink twitterLink = jsoup.parseTweetWithMetrics(sqsObj);
                        if (twitterLink != null) {
                            TimeCalculator timer = new TimeCalculator();
                            timer.start();
                            String image_file_name = screenShotManager.capture(twitterLink.getUrl());
                            long shotTime = timer.stop();
                            screenshotMetrics.sendMetricData((double) shotTime);
                            if(image_file_name != null) {
                                File image_file = new File(image_file_name);
                                String imageUrl = s3.storeImage(image_file);
                                image_file.delete();
                                twitterLink.setImageUrl(imageUrl);
                                twitterLink.balanceNulls();
                                DBManager.getInstance().insertData(twitterLink);
                            }
                        }
                        sqsReceiver.deleteMessage(message);
                    }
                }
        };
    }

    public void start(){
        if(!is_running) {
            is_running = true;
            for(int i = 0 ; i < thread_count; i++) {
                executor.execute(messageHandleProcess);
            }
        }
    }

    public void stop() {
        if(is_running) {
            is_running = false;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
