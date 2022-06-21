# egisdataproto

This project contains source code supporting two different mechanisms for moving messages automatically from Kafka topics to SQS queues.

- lambdafunctions - java code for processing trigger based Kafka topic messages and sending messages to SQS queues
- connectors - java open-source Camel AWS2 SQS Event Sink connector to move Kafka topic messages to SQS queues
