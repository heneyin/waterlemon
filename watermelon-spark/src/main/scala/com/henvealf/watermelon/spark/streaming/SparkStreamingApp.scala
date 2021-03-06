package com.henvealf.watermelon.spark.streaming


import com.henvealf.watermelon.common.ConfigWm
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Duration, Milliseconds, Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Assign
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent


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

  /**
    * 创建 spark streaming context。
    * @return
    */
  def createStreamingContext(): StreamingContext

  /**
    * 初始化流的方法。
    * @param ssc
    * @return
    */
  def getStream(ssc: StreamingContext): DStream[ConsumerRecord[KEY, VALUE]]

  /**
    * 处理方法定义
    * @param stream spark 流
    * @param ssc StreamingContext
    */
  def handle(stream: DStream[ConsumerRecord[KEY, VALUE]], ssc: StreamingContext)

  /**
    * 启动 spark streaming
    */
  def start()

}

/**
  * Spark Streaming app that consumer kafka.
  * @param sparkConfigTuple spark config
  * @param kafkaConfig      kafka consumer config
  * @param appConfig        app owner config
  * @tparam KEY      Record key type
  * @tparam VALUE    Record value type
  */
abstract class KafkaSparkStreamApp [KEY, VALUE] (sparkConfigTuple: List[(String, String)],
                                                 kafkaConfig: Map[String, String],
                                                 appConfig: Map[String, String]) extends SparkStreamingApp[KEY, VALUE] {

  def this(sparkConfigFileName: String,
           kafkaConfigFile: String,
           appConfigFileName: String) =
    this( ConfigWm.getConfigTuplesByFileName(sparkConfigFileName),
      ConfigWm.getConfigMapByFileName(kafkaConfigFile),
      ConfigWm.getConfigMapByFileName(appConfigFileName)
    )


  override def createStreamingContext(): StreamingContext = {
    val conf = new SparkConf().setAll(sparkConfigTuple)
//    conf.set("spark.serializer","org.apache.spark.serializer.KryoSerialize")
//    conf.registerKryoClasses(util.Arrays.asList(classOf[ConsumerRecord[_, _]]).toArray.asInstanceOf[Array[Class[_]]])
    val durationMs = appConfig.getOrElse("duration.ms", "5000").toLong

    val getStreamingContextFun = () =>  {
      val ssc = new StreamingContext(conf, Milliseconds(durationMs))
      val stream = getStream(ssc)
      handle(stream, ssc)
      ssc
    }

    val checkpointDirectory = appConfig.getOrElse(AppConfigConstant.CHECK_POINT_DIR, null)

    StreamingContext.getOrCreate(checkpointDirectory, getStreamingContextFun)

  }


  // 是否使用外部偏移量管理器,如果外部偏移量管理器没有设置，则不会使用。
  var useOffsetManager = true
  var kafkaOffsetManager: Option[KafkaOffsetManager] = None

  override def getStream(ssc: StreamingContext): InputDStream[ConsumerRecord[KEY, VALUE]] = {

    val topicArray = appConfig.get("topics") match {
      // topic 用逗号分隔。
      case Some(value) => value.split("\\w*,\\w*")
      case None => throw new RuntimeException("Please set config 'topics' in file appConfig.properties")
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
    val ssc = createStreamingContext()
    ssc.start()             // Start the computation
    ssc.awaitTermination()
    kafkaOffsetManager.foreach(_.close())
  }

  def getOffsetRangesFromRDD(rdd: RDD[ConsumerRecord[KEY, VALUE]]): Array[OffsetRange] = {
    rdd.asInstanceOf[HasOffsetRanges].offsetRanges
  }

  def saveOffset(offsetRanges: Array[OffsetRange], stream: DStream[ConsumerRecord[KEY, VALUE]]): Unit = {
      if (useOffsetManager) {
        kafkaOffsetManager.foreach(_.saveOffset(offsetRanges))
      } else {
        stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      }
  }

  def saveOffset(rdd: RDD[ConsumerRecord[KEY, VALUE]], stream : DStream[ConsumerRecord[KEY, VALUE]]): Unit = {
    val offsets = getOffsetRangesFromRDD(rdd)
    saveOffset(offsets, stream)
  }

}


object AppConfigConstant {
  val CHECK_POINT_DIR = "checkpoint.dir"
}

