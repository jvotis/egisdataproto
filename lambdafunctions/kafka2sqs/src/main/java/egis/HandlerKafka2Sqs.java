package egis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent.KafkaEventRecord;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


import java.util.Map;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HandlerKafka2Sqs implements RequestHandler<KafkaEvent, String> {
	    private static final Logger logger = LoggerFactory.getLogger(HandlerKafka2Sqs.class);
	    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    
	    private String QUEUE_NAME_NORMAL = "MollyCdkSamStack-EgisDataQueue858192C9-QpTKq021iRr4";

	    private String QUEUE_NAME_FIFO = "EgisDataQueue2.fifo";
	    private String QUEUE_FIFO_URL= "arn:aws:sqs:us-east-1:030819073208:EgisDataQueue2.fifo";
	  
	    
	    @Override
	    public String handleRequest(KafkaEvent event, Context context) {
	        String response = new String("KafkaEvent received from topic and sent to SQS queue.");
	        
	
	        // log execution details
	        LambdaLogger logger = context.getLogger();
	        // logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
	        logger.log("CONTEXT: " + gson.toJson(context));
	        // log event details
	        logger.log("EVENT: " + gson.toJson(event));
	        logger.log("EVENT TYPE: " + event.getClass().toString());

	        
	        
	        SqsClient sqs = SqsClient.builder()
	                .region(Region.US_EAST_1)
	                .credentialsProvider(ProfileCredentialsProvider.create())
	                .build();

	        
	        sendMessage(sqs, QUEUE_FIFO_URL, event);
	        
	        
	        // listQueues(sqs);
	        // List<Message> messages = receiveMessages(sqs, QUEUE_FIFO_URL);
	        // processMessages(sqs, QUEUE_FIFO_URL, messages);
	        

	        return response;
	    }
	    
	    private void sendMessage(SqsClient sqsClient, String queueUrl, KafkaEvent event) {
	    
	    	System.out.println("\nSend message");

	        try {
	            sqsClient.sendMessage(SendMessageRequest.builder()
	                .queueUrl(queueUrl)
	                .messageBody(gson.toJson(event))
	                .delaySeconds(10)
	                .build());

	        } catch (SqsException e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	    	
	    }	
	    	
	    
	    public static void sendBatchMessages(SqsClient sqsClient, String queueUrl) {

	        System.out.println("\nSend multiple messages");

	        try {
	            // snippet-start:[sqs.java2.sqs_example.send__multiple_messages]
	            SendMessageBatchRequest sendMessageBatchRequest = SendMessageBatchRequest.builder()
	                .queueUrl(queueUrl)
	                .entries(SendMessageBatchRequestEntry.builder().id("id1").messageBody("Hello from msg 1").build(),
	                        SendMessageBatchRequestEntry.builder().id("id2").messageBody("msg 2").delaySeconds(10).build())
	                .build();
	            sqsClient.sendMessageBatch(sendMessageBatchRequest);
	            // snippet-end:[sqs.java2.sqs_example.send__multiple_messages]

	        } catch (SqsException e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	    }
	    
	    private List<Message> receiveMessages(SqsClient sqsClient, String queueUrl) {

	        System.out.println("\nReceive messages");

	        try {
	            
	            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
	                .queueUrl(queueUrl)
	                .maxNumberOfMessages(6)
	                .build();
	            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
	            return messages;
	        } catch (SqsException e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	        return null;
	    }
	    
	    
	    private void listQueues(SqsClient sqsClient) {

	        System.out.println("\nList Queues");
	         
	        // String prefix = "que";

	        try {
	            ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().build();
	            ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);

	            for (String url : listQueuesResponse.queueUrls()) {
	                System.out.println(url);
	            }

	        } catch (SqsException e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	       
	    }
	    
	    
	    private void processMessages(SqsClient sqsClient, String queueUrl, List<Message> messages) {

	        System.out.println("\nProcessing Messages");

	        try {

	            for (Message message : messages) {
	            	
	            	System.out.println("MESSAGE: " + message.toString());
	            	
	            	/*
	                ChangeMessageVisibilityRequest req = ChangeMessageVisibilityRequest.builder()
	                    .queueUrl(queueUrl)
	                    .receiptHandle(message.receiptHandle())
	                    .visibilityTimeout(100)
	                    .build();
	                sqsClient.changeMessageVisibility(req);
	                */
	                
	            }
	        } catch (SqsException e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	    }
	    
}
