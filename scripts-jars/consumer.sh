#!/bin/bash

kafkaPath='/usr/lib/kafka'

if [ $1 -eq 0 ]
then
$kafkaPath/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic taxi-etl --property print.key=true --from-beginning
else
$kafkaPath/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic taxi-anomalies --property print.key=true --from-beginning
fi

