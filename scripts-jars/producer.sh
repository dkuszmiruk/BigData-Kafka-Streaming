#!/bin/bash

kafkaPath='/usr/lib/kafka'
pathToProducer='.'
folderPath='/home/dominikkusz1/yellow_tripdata_result'

$kafkaPath/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic taxi-input

#Local machine
#java -cp $kafkaPath/libs/*:$pathToProducer/KafkaProducer.jar \
#	com.example.bigdata.TestProducer $folderPath 15 taxi-input 1 localhost:9092

#GCP
CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
java -cp $kafkaPath/libs/*:$pathToProducer/KafkaProducer.jar \
	com.example.bigdata.TestProducer $folderPath 15 taxi-input 1 ${CLUSTER_NAME}-w-0:9092

