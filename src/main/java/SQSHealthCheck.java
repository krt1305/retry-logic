import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

public class SQSHealthCheck  {

    //https://sqs.ap-southeast-1.amazonaws.com
    public static void main(String[] args) {




        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

             AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_2)
                .build();

        //final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        //System.out.println();
         CreateQueueRequest createQueueRequest =
                new CreateQueueRequest("MyQueue");
         String myQueueUrl = sqs.createQueue(createQueueRequest)
                .getQueueUrl();
        System.out.println("myQueueUrl "+myQueueUrl);

    }




}
