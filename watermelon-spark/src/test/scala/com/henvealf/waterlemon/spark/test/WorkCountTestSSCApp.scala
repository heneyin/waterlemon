package com.henvealf.waterlemon.spark.test

import com.henvealf.watermelon.common.ConfigWm
import com.henvealf.watermelon.spark.streaming.{KafkaOffsetManagerInZK, KafkaSparkStreamApp}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Duration, Milliseconds, StreamingContext}

import collection.JavaConverters._
import java.util

import org.apache.spark.util.LongAccumulator

/**
  * <p>
  *
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-10
  */
class WorkCountTestSSCApp(sparkConfigTuple: List[(String, String)],
                          kafkaConfig: Map[String, String],
                          appConfig: Map[String, String]) extends KafkaSparkStreamApp[String, String](sparkConfigTuple, kafkaConfig, appConfig){

  val CONFIG_DURATION = "duration.ms"

  var accu: LongAccumulator = null

  def this(sparkConfigFileName: String,
           kafkaConfigFile: String,
           appConfigFileName: String) =
    this( ConfigWm.getConfigTuplesByFileName(sparkConfigFileName),
      ConfigWm.getConfigMapByFileName(kafkaConfigFile),
      ConfigWm.getConfigMapByFileName(appConfigFileName)
    )

  override def ready(ssc: StreamingContext, appConfig: Map[String, String]): Unit = {
    accu = ssc.sparkContext.longAccumulator("total")
  }

  override def rddOperate(rdd: RDD[ConsumerRecord[String, String]],
                       streamingContext: StreamingContext,
                       appConfig: Map[String, String]): Unit = {

    val result: Long = rdd.map(a => 1L).count()
    accu.add(result)

    println("result" + accu.value)
  }

  override def getStreamDuration(appConfig: Map[String, String]): Duration = {
    val dur = appConfig.getOrElse(CONFIG_DURATION, "1000").toLong
    Milliseconds(dur)
  }

}

object WorkCountTestSSCApp {
  def main(args: Array[String]): Unit = {
    val app = new WorkCountTestSSCApp("watermelon-spark/src/main/resources/sparkConfig.properties",
      "watermelon-spark/src/main/resources/kafkaParams.properties",
      "watermelon-spark/src/main/resources/appConfig.properties")
    app.setOffsetManager(new KafkaOffsetManagerInZK("localhost:2181", "/com/henvealf/spark/offset"))
    app.setUseOffsetManager(true)
    app.start()
  }
}
