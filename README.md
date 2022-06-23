# egisdataproto

This project contains source code supporting two different mechanisms for moving messages automatically from Kafka topics to SQS queues.

- lambdafunctions - java code for processing trigger based Kafka topic messages and sending messages to SQS queues
- connectors - java open-source Camel AWS2 SQS Event Sink connector to move Kafka topic messages to SQS queues

Kafka Connector Setup

=== What is needed

- An AWS SQS queue (normal or FIFO)

=== Running Kafka

[source]
----
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
$KAFKA_HOME/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic mytopic
----

=== Download the connector package

Download the connector package tar.gz and extract the content to a directory. In this example we'll use `/home/oscerd/connectors/`

[source]
----
> cd /home/oscerd/connectors/
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-aws2-sqs-kafka-connector/0.11.5/camel-aws2-sqs-kafka-connector-0.11.5-package.tar.gz
> untar.gz camel-aws2-sqs-kafka-connector-0.11.5-package.tar.gz
----

=== Configuring Kafka Connect

You'll need to set up the `plugin.path` property in your kafka

Open the `$KAFKA_HOME/config/connect-standalone.properties` and set the `plugin.path` property to your choosen location:

[source]
----
...
plugin.path=/home/oscerd/connectors
...
----

=== Setup the connectors

Open the AWS2 SQS configuration file at `CamelAWS2SQSSinkConnector.properties`

[source]
----

name=CamelAWSSQSSinkConnector
topics=egis-data-topic
tasks.max=1
connector.class=org.apache.camel.kafkaconnector.awssqs.CamelAwssqsSinkConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.kafka.connect.storage.StringConverter

camel.sink.path.queueNameOrArn=mysqs

camel.component.aws-sqs.configuration.access-key=<youraccesskey>
camel.component.aws-sqs.configuration.secret-key=<yoursecretkey>
camel.component.aws-sqs.configuration.region=<yourregion>

 
----

 

Run the kafka connect with the SQS Sink connector:

[source]
----
$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties $EXAMPLES/aws2-sqs/aws2-sqs-sink/config/CamelAWS2SQSSinkConnector.properties
----

On a different terminal run the kafka-producer and send messages to your Kafka Broker:

[source]
----
$KAFKA_HOME/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic mytopic
Kafka to SQS message 1
Kafka to SQS message 2
----

You should see the messages enqueued in the `camel-1` SQS queue.
 

 