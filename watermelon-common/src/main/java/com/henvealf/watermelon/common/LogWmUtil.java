package com.henvealf.watermelon.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * 简单的日志工具类，使用 SLF4J,参考了 Spark 的 <>org.apache.spark.internal.Logging</link>
 * @author hongliang.yin/Henvealf on 2018/10/27
 */
public class LogWmUtil {

    public static ClassLoader getClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = LogWmUtil.class.getClassLoader();
        }
        return contextClassLoader;
    }

    static {
        try {
            ClassLoader contextClassLoader = getClassLoader();
            Class bridgeClass = Class.forName("org.slf4j.bridge.SLF4JBridgeHandler", true, contextClassLoader);
            bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null);
            boolean installed = (boolean) bridgeClass.getMethod("isInstalled").invoke(null);
            if (!installed) {
                bridgeClass.getMethod("install").invoke(null);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        }

    }

    private static Logger LOG = null;
    private volatile static boolean initialized = false;
    private static final Object initLock = new Object();

    public static Logger getLog(Class clazz) {
        if (LOG == null) {
            initializeLogIfNecessary(clazz);
            LOG = LoggerFactory.getLogger(clazz);
        }
        return LOG;
    }

    public static void setLog4jProperties(String fileInClassPath) {
        if (fileInClassPath == null) {
            System.err.println("the log file can't be null ");
            return;
        }

        URL url = getClassLoader().getResource(fileInClassPath);
        if (url != null) {
            PropertyConfigurator.configure(url);
            System.err.println("LogWmUtil load success log4j profile: " + fileInClassPath);
        } else {
            System.err.println("LogWmUtil can't find log4j profile: " + fileInClassPath);
        }

    }

    private static void initializeLogIfNecessary(Class clazz) {
        if (!initialized) {
            synchronized (initLock) {
                if (!initialized) {
                    initializeLog(clazz);
                }
            }
        }
    }

    private static void initializeLog(Class clazz) {

        // Don't use a logger in here, as this is itself occurring during initialization of a logger
        // If Log4j 1.2 is being used, but is not initialized, load a default properties file
        String binderClass = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();

        // This distinguishes the log4j 1.2 binding, currently
        // org.slf4j.impl.Log4jLoggerFactory, from the log4j 2.0 binding, currently
        // org.apache.logging.slf4j.Log4jLoggerFactory
        boolean usingLog4j12 = "org.slf4j.impl.Log4jLoggerFactory".equals(binderClass);
        if (usingLog4j12) {
            boolean log4j12Initialized = LogManager.getRootLogger().getAllAppenders().hasMoreElements();
            if (!log4j12Initialized) {
                String defaultLogProps = "com/henvealf/watermelon/common/log4j-default.properties";
                URL url = getClassLoader().getResource(defaultLogProps);
                if (url != null) {
                    PropertyConfigurator.configure(url);
                    System.err.println("LogWmUtil use default log4j profile: " + defaultLogProps);
                } else {
                    System.err.println("LogWmUtil was unable to load " + defaultLogProps);
                }
            }
        }

        initialized = true;
        // Force a call into slf4j to initialize it. Avoids this happening from multiple threads
        // and triggering this: http://mailman.qos.ch/pipermail/slf4j-dev/2010-April/002956.html
        getLog(clazz);
    }

}
