#!/bin/bash

kafkaPath='/usr/lib/kafka'
CSVFile='/home/dominikkusz1/taxi_zone_lookup.csv'
pathToProgram='.'

$kafkaPath/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic taxi-etl

$kafkaPath/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic taxi-anomalies

java -cp $pathToProgram/KafkaTaxi.jar com.project.bigdata.TaxiTripProcessing $CSVFile 4 5000

