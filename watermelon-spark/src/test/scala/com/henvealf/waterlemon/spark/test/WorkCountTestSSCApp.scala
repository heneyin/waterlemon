package com.henvealf.waterlemon.spark.test

import com.henvealf.watermelon.common.ConfigWm
import com.henvealf.watermelon.spark.streaming.{AppConfigConstant, KafkaOffsetManagerInZK, KafkaSparkStreamApp}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Duration, Milliseconds, StreamingContext}

/**
  * <p>
  *
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-10
  */
class WorkCountTestSSCApp(sparkConfigTuple: List[(String, String)],
                          kafkaConfig: Map[String, String],
                          appConfig: Map[String, String]) extends KafkaSparkStreamApp[String, String](sparkConfigTuple, kafkaConfig, appConfig) with Serializable{


  override def handle(stream: DStream[ConsumerRecord[String, String]]): Unit = {

    // 用了window就不能获得offset了。
    // 窗口长度为每次计算的批次
    // 滑动间隔，为每几秒滑动一次。
    stream.map(r => r.value()).window(Milliseconds(15000), Milliseconds(20000)).foreachRDD(rdd => {
      val re = rdd.map(r => (r, 1)).reduceByKey(_ + _)
      re.foreach(println)
      println("-------")
    })

    stream.foreachRDD(rdd => {
      saveOffset(rdd, stream)
    })

    ssc.checkpoint(appConfig.getOrElse(AppConfigConstant.CHECK_POINT_DIR, null))
  }

  def this(sparkConfigFileName: String,
           kafkaConfigFile: String,
           appConfigFileName: String) =
    this( ConfigWm.getConfigTuplesByFileName(sparkConfigFileName),
      ConfigWm.getConfigMapByFileName(kafkaConfigFile),
      ConfigWm.getConfigMapByFileName(appConfigFileName)
    )

}


object WorkCountTestSSCAppMain {
  def main(args: Array[String]): Unit = {
    val app = new WorkCountTestSSCApp("watermelon-spark/src/main/resources/sparkConfig.properties",
      "watermelon-spark/src/main/resources/kafkaParams.properties",
      "watermelon-spark/src/main/resources/appConfig.properties")
    app.setOffsetManager(new KafkaOffsetManagerInZK("localhost:2181", "/com/henvealf/spark/offset"))
    app.setUseOffsetManager(true)
    app.start()
  }
}
