package com.henvealf.waterlemon.spark.test

import org.apache.spark.util.BoundedPriorityQueue
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer
import scala.math.Ordering

/**
  * <p>
  *  寻找每个分组内的 Top-N
  * <p>
  *
  * @author hongliang.yin/Henvealf on 2019-04-20
  */
object TestTopN {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[2]").setAppName("the top N in group")
    val sc = new SparkContext(conf)

    def tuple2FirstOrdering[T1, T2](implicit ord1: Ordering[T1]): Ordering[(T1, T2)] =
      new Ordering[(T1, T2)]{
        def compare(x: (T1, T2), y: (T1, T2)): Int = {
          val compare1 = ord1.compare(x._1, y._1)
          if (compare1 != 0) return compare1
          0
        }
      }

    val rdd = sc.makeRDD(Seq(

                    ("beijing", "hehe", 38),
                    ("beijing", "lala", 32),
                    ("beijing", "lele", 74),
                    ("beijing", "yarn", 199),
                    ("beijing", "hdfs", 208),
                    ("beijing", "rdd", 19),
                    ("beijing", "idea", 349),
                    ("beijing", "lilei", 19),

                    ("shanghai", "mayun", 194),
                    ("shanghai", "flume", 87),
                    ("shanghai", "spark", 8585),
                    ("shanghai", "mapreduce", 40),
                    ("shanghai", "xiaomei", 123),
                    ("shanghai", "dazui", 84),
                    ("shanghai", "xiaomao", 385),
                    ("shanghai", "panghong", 983),
                    ("shanghai", "xiaohu", 364),
                    ("shanghai", "xiaojiang", 3748),
                    ("shanghai", "shuaihu", 7474),
                    ("shanghai", "kafka", 67)))


    val result = rdd.map(t => {(t._1, (t._3, t._2))} ).aggregateByKey(
      new MyBoundedPriorityQueue[Tuple2[Int, String]](5)(tuple2FirstOrdering[Int, String](Ordering.Int)))(
      (queue, ele) => {
        queue.+=(ele)
        queue
      },
      (queue1, queue2) => {
        queue1.++=(queue2)
        queue1
      }).mapValues(qu => {
      qu.toList
    })

    result.foreach(println(_))
  }



}
