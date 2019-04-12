package com.henvealf.watermelon.common

import java.io.FileInputStream
import java.util.Properties

import scala.collection.mutable
import scala.collection.mutable.ListBuffer



/**
  *
  * @author hongliang.yin/Henvealf on 2018/5/30
  */
object ConfigWm {

  def getConfigMapByFileName(fileName: String): Map[String, String] = {
    import collection.JavaConverters._
    val properties = getPropertiesByFileName(fileName)
    val resultMap = mutable.HashMap[String, String]()
    for( name <- properties.stringPropertyNames().asScala ) {
      resultMap.put(name, properties.getProperty(name))
    }
    resultMap.toMap
  }

  def getConfigTuplesByFileName(fileName: String): List[(String, String)] = {
    import collection.JavaConverters._
    val properties = getPropertiesByFileName(fileName)

    val resultList = ListBuffer[(String, String)]()

    for( name <- properties.stringPropertyNames().asScala ) {
      resultList += ((name , properties.getProperty(name)))
    }
    resultList.toList
  }

  def getPropertiesByFileName(fileName: String): Properties = {
    val properties = new Properties()
    val in = new FileInputStream(fileName)
    properties.load(in)
    in.close()
    properties
  }

  def getPropertiesByClassPath(filePath: String):Properties = {
    val properties = new Properties()
    val url = Option(ClassLoader.getSystemClassLoader.getResourceAsStream(filePath))
    url match {
      case Some(pro) =>
        properties.load(pro)
        pro.close()
      case None => System.err.println(s"can not found file: $filePath" )
    }
    properties
  }


}
