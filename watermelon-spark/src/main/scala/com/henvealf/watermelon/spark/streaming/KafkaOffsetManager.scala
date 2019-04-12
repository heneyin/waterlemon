package com.henvealf.watermelon.spark.streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}

/**
  * <p>
  * offset 管理器
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-09
  */
trait KafkaOffsetManager {

  def setGroupId(groupId: String)

  def preInit()

  def getOffset(): Map[TopicPartition, Long]

  def getOffset(partition: TopicPartition): Long = {
    getOffset().getOrElse(partition, -1)
  }

  def saveOffset(offsets: Map[TopicPartition, Long])

  def saveOffset[KEY, VALUE](rdd: RDD[ConsumerRecord[KEY, VALUE]]): Unit = {
    saveOffset(rdd.asInstanceOf[HasOffsetRanges].offsetRanges)
  }

  def saveOffset(offsetRanges: Array[OffsetRange]): Unit = {
    var result: Map[TopicPartition, Long] = Map[TopicPartition, Long]()
    for (offsetRange <- offsetRanges) {
      result += (offsetRange.topicPartition() -> offsetRange.untilOffset)
    }
    saveOffset(result)
  }

  def close()

}
