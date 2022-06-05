package egis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
// import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
// import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent.KafkaEventRecord;
import example.Util;
import java.util.Map;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handler value: egis.HandleKafka
public class HandlerKafka implements RequestHandler<KafkaEvent, String>{
  private static final Logger logger = LoggerFactory.getLogger(HandlerKafka.class);
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(KafkaEvent event, Context context)
  {
    String response = new String("200 OK");
    // for(KafkaEventRecord record : event.getRecords()) {  
    //   logger.info(gson.toJson(record.toString()));
    // }
    for (Map.Entry<String, List<KafkaEventRecord>> entry : event.getRecords().entrySet()) {
      logger.info(entry.getKey() + ":" + entry.getValue());

      // TODO - write this message to SQS queue


    }

    // log execution details
    Util.logEnvironment(event, context, gson);
    return response;
  }
}
