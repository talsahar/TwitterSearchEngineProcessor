package main.tal.root;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import main.tal.metrics.GenericMetrics;
import main.tal.metrics.TimeCalculator;
import java.io.File;

public class S3BucketManager {

    AmazonS3 s3Client;
    GenericMetrics s3Metrics;

    public S3BucketManager() {
        s3Client = AmazonS3ClientBuilder.defaultClient();
        s3Metrics = new GenericMetrics
                ("TWEETS/MANUFACTOR", "UploadS3Time",
                        "TimeInterval", "Millisecond");
    }

    public String storeImage(File image_file) {
        try {
            TimeCalculator timer = new TimeCalculator();
            timer.start();
            String bucket_name = ProjectProperties.getInstance().getS3_bucket_name();
            String key = ProjectProperties.getInstance().getS3_path() + image_file.getName();
            PutObjectRequest request = new PutObjectRequest(bucket_name, key, image_file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpg");
            request.setMetadata(metadata);
            s3Client.putObject(request);
            long totalTime = timer.stop();
            s3Metrics.sendMetricData((double) totalTime);
            return s3Client.getUrl(bucket_name, key).toString();
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return null;
    }
}
