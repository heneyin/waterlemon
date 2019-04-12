package com.henvealf.watermelon.tokafka

import java.util.Properties

import org.apache.hadoop.security.UserGroupInformation
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.{SparkConf, SparkContext}
import com.henvealf.watermelon.common.{ConfigWm, LogWmUtil}

/**
  * read file data to Kafka
  * @author hongliang.yin/Henvealf
  */
object FileToKafkaMain {

  def main(args: Array[String]): Unit = {


    if (args.length < 3 || args.length == 4  || args.length > 5) {
      println("must arg <filepath> <bootstrap.servers> <topic-name> [kerberos_principle keytab_file]")
      System.exit(1)
    }

    LogWmUtil.setLog4jProperties("log4j.properties")
    val log = LogWmUtil.getLog(FileToKafkaMain.getClass)

    val fileName = args(0)
    val brokerList = args(1)
    val topicName = args(2)

    var kerberos_principle: String = null
    var keytab_file: String = null

    if (args.length == 5) {
      kerberos_principle = args(3)
      keytab_file = args(4)
    }


    val kafkaProps = ConfigWm.getPropertiesByFileName("kafka.properties")
    val sparkConf: SparkConf = new SparkConf()
    val sc: SparkContext = new SparkContext(sparkConf)

    if (kerberos_principle != null && keytab_file != null) {
      UserGroupInformation.loginUserFromKeytab(kerberos_principle.trim,keytab_file.trim)
    }

    val fileRdd = sc.textFile(fileName)

    val totalCount = sc.longAccumulator("total count")

    fileRdd.foreachPartition( prititions => {
      log.info("begin init product")
      val product = new KafkaProducer[String, String](kafkaProps)
      log.info("init product end,begin product in file partition.")

      prititions.foreach(line => {
        val data = new ProducerRecord[String, String](topicName, line)
        product.send(data)
        totalCount.add(1)
      })

      log.info("product partition end.")
      product.close()
    })

    println(s"end send data from $fileName to kafka, total count: ${totalCount.value}.")
    sc.stop()
  }


}
