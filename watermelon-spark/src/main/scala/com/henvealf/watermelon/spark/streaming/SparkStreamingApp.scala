package com.henvealf.watermelon.spark.streaming

import java.util

import com.henvealf.watermelon.common.ConfigWm
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Duration, Milliseconds, Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Assign
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

import collection.JavaConverters._

/**
  * <p>
  * spark streaming 应用启动模板。
  * 包括
  *   配置管理，
  *   过程定义，
  *   偏移量管理。
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-09
  */
trait SparkStreamingApp[KEY, VALUE] {

  val ssc: StreamingContext

  def getStreamDuration(appConfig: Map[String, String]) : Duration

  def getStream: InputDStream[ConsumerRecord[KEY, VALUE]]

  /**
    * 一些准备操作，比如增量器、广播器的定义。
    * @param streamContext
    * @param appConfig
    */
  def ready(streamContext: StreamingContext, appConfig: Map[String, String])

  /**
    * 在 RDD 上的操作。
    * @param rdd
    * @param streamContext
    * @param appConfig
    */
  def rddOperate(rdd: RDD[ConsumerRecord[KEY, VALUE]], streamContext: StreamingContext, appConfig: Map[String, String])

  def start()

}

abstract class KafkaSparkStreamApp [KEY, VALUE] (sparkConfigTuple: List[(String, String)],
                                                 kafkaConfig: Map[String, String],
                                                 appConfig: Map[String, String]) extends SparkStreamingApp[KEY, VALUE] {

  val ssc: StreamingContext = {
    val conf = new SparkConf().setAll(sparkConfigTuple)
    new StreamingContext(conf, getStreamDuration(appConfig))
  }

  // 是否使用外部偏移量管理器,如果外部偏移量管理器没有设置，则不会使用。
  var useOffsetManager = true
  var kafkaOffsetManager: Option[KafkaOffsetManager] = None

//  def this(sparkConfigFileName: String,
//           kafkaConfigFile: String,
//           appConfigFileName: String) =
//    this( ConfigWm.getConfigTuplesByFileName(sparkConfigFileName),
//      ConfigWm.getConfigMapByFileName(kafkaConfigFile),
//      ConfigWm.getConfigMapByFileName(appConfigFileName)
//    )

  override def getStream(): InputDStream[ConsumerRecord[KEY, VALUE]] = {
//    val topics = Array("topicA", "topicB")
    val topics = appConfig.get("topics")

    val topicArray = if (topics.nonEmpty) {
      topics.get.split("\\w*,\\w*")
    } else {
      throw new RuntimeException("Please set config 'topics' in file appConfig.properties")
    }

    if (ssc == null) {
      throw new IllegalStateException("StreamingContext can not be null.")
    }

    // 根据当前情况得到订阅策略
    val strategy = if (useOffsetManager && kafkaOffsetManager.isDefined) {
      val storedOffset = kafkaOffsetManager.get.getOffset()
      if (null == storedOffset || storedOffset.isEmpty) {
        Subscribe[KEY, VALUE](topicArray, kafkaConfig)
      } else {
        Assign[KEY, VALUE](storedOffset.keys.toList, kafkaConfig, storedOffset)
      }
    } else {
      Subscribe[KEY, VALUE](topicArray, kafkaConfig)
    }

    KafkaUtils.createDirectStream[KEY, VALUE](
      ssc,
      PreferConsistent,
      strategy
    )


  }

  def setOffsetManager(offsetManager: KafkaOffsetManager): Unit = {
    this.kafkaOffsetManager = Some(offsetManager)
    this.kafkaOffsetManager.foreach(m => {
      m.setGroupId(kafkaConfig.getOrElse("group.id", "group_id_" + this.getClass.getSimpleName))
      m.preInit()
    })
  }

  def setUseOffsetManager(use: Boolean): Unit = {
    this.useOffsetManager = use
  }

  override def start(): Unit = {
    ready(ssc, appConfig)
    val stream = getStream()
    stream.foreachRDD(rdd => {
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      println(s"offsetRange: ${offsetRanges.mkString("~~")}")
      rddOperate(rdd, ssc, appConfig)

      if (useOffsetManager) {
        kafkaOffsetManager.foreach(_.saveOffset(offsetRanges))
      } else {
        stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      }

    })
    ssc.start()             // Start the computation
    ssc.awaitTermination()
    kafkaOffsetManager.foreach(_.close())
  }


}


