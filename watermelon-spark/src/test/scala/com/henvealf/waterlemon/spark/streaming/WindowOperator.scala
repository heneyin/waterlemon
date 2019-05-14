package com.henvealf.waterlemon.spark.streaming

import com.henvealf.watermelon.spark.streaming.{KafkaSparkStreamApp, SparkStreamingApp}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream


