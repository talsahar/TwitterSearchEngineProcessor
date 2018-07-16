package main.tal.root;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class SqsReceiver {

    private AmazonSQS sqs;
    private String queue_url;

    public static class TwitterSqsObject {
        String url;
        String track;

        public TwitterSqsObject(String url, String track) {
            this.url = url;
            this.track = track;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getTrack() {
            return track;
        }
    }


    public SqsReceiver(String queue_url) {
        this.queue_url = queue_url;
        sqs = AmazonSQSClientBuilder.defaultClient();
    }

    public Message receiveMessage() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queue_url);
        receiveMessageRequest.setMaxNumberOfMessages(1);
        receiveMessageRequest.setWaitTimeSeconds(5);
        ReceiveMessageResult result = sqs.receiveMessage(receiveMessageRequest);

        if (result.getMessages().size() > 0) {
            Message message = result.getMessages().get(0);
            return message;
        }
        return null;
    }

    public void deleteMessage(Message message) {
        final String messageReceiptHandle = message.getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(queue_url, messageReceiptHandle));
    }
}
