package com.henvealf.watermelon.spark.streaming
import org.apache.kafka.common.TopicPartition
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry


/**
  * <p>
  * offset 管理器，存储在 ZK 上。
  * savePath/
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-10
  */
class KafkaOffsetManagerInZK(zkServers: String, savedPath: String) extends KafkaOffsetManager {

  // TODO 需要在每次app启动的时候检测分区信息，如果 topic 添加了新的分区，按照现在的实现，新的分区不会被分配去消费。

  var groupId: String = null
  var finalPath: String = null

  val retryPolicy = new ExponentialBackoffRetry(1000, 3)
  val client: CuratorFramework = CuratorFrameworkFactory.newClient(zkServers, retryPolicy)
  client.start()

  def setGroupId(groupId: String): Unit = {
    this.groupId = groupId
  }

  def preInit(): Unit = {
    this.finalPath= savedPath + "/" + groupId
    if (null == client.checkExists.forPath(finalPath))
      client.create().creatingParentsIfNeeded().forPath(finalPath, "".getBytes())
  }

  override def getOffset(): Map[TopicPartition, Long] = {
    var result = Map[TopicPartition, Long]()
    val data: String = new String(client.getData.forPath(finalPath))
    if (data.isEmpty) return result
    val kvs = data.split("\n")
    for (kvStr <- kvs) {
      if (!kvStr.isEmpty) {
        val kv = kvStr.split("=")
        if (kv.length != 2) throw new IllegalAccessException("the value can't by parsed:" + kvStr)
        result += (toTopicPartition(kv(0)) -> java.lang.Long.parseLong(kv(1)))
      }
    }
    result
  }

  override def saveOffset(offsets: Map[TopicPartition, Long]): Unit = {
    val sb: StringBuilder = new StringBuilder
    for ((topicPartition, c) <- offsets) {
      sb.append(topicPartition.toString).append("=").append(c)
      sb.append("\n")
    }
    println(sb.toString())
    client.setData().forPath(finalPath, sb.toString().getBytes())
  }

  override def close(): Unit = {
    client.close()
  }

  def toTopicPartition(kvStr: String): TopicPartition = {
    val kv = kvStr.split("-")
    if (kv.length != 2) throw new IllegalAccessException("the value can't by parsed:" + kvStr)
    new TopicPartition(kv(0), Integer.parseInt(kv(1)))
  }

}
