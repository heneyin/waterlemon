package com.henvealf.watermelon.commom

import java.io.FileInputStream
import java.util.Properties

import scala.collection.mutable


/**
  *
  * @author hongliang.yin/Henvealf on 2018/5/30
  */
object ConfigWm {

  def getConfigMapFormFileName(fileName: String): Map[String, String] = {
    import collection.JavaConverters._
    val properties = getPropertiesFromFileName(fileName)
    val resultMap = mutable.HashMap[String, String]()
    for( name <- properties.stringPropertyNames().asScala ) {
      resultMap.put(name, properties.getProperty(name))
    }
    resultMap.toMap
  }

  def getPropertiesFromFileName(fileName: String): Properties = {
    val properties = new Properties()
    val in = new FileInputStream(fileName)
    properties.load(in)
    in.close()
    properties
  }

}
