package com.henvealf.watermelon.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-24
 */
public class PropertiesUtil {


    public static Properties initProperties(String filePath) {
        try (InputStream reader = new FileInputStream(filePath)) {
            Properties props = new Properties();
            props.load(reader);
            return props;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValueFromProp(Properties props, String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static long getValueFromProp(Properties props, String key, long defaultValue) {
        String strValue = props.getProperty(key, null);
        if (strValue == null) {
            return defaultValue;
        }
        return Long.parseLong(strValue);
    }

    public static int getValueFromProp(Properties props, String key, int defaultValue) {
        String strValue = props.getProperty(key, null);
        if (strValue == null) {
            return defaultValue;
        }
        return Integer.parseInt(strValue);
    }

    public static boolean getValueFromProp(Properties props, String key, boolean defaultValue) {
        Boolean strValue = (Boolean) props.get(key);
        if (strValue == null) {
            return defaultValue;
        }
        return strValue;
    }

    /**
     * 解密 properties 中的密文。
     * @param properties
     */
    public static void decryptProperties(Properties properties) {
        if (properties == null) return;
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                String afterDecrypt = JasyptUtil.decryptIfCan((String) value);
                entry.setValue(afterDecrypt);
            }
        }
    }

    public static void coverPropertiesFromSysEnv(Properties properties, String envKey, String configKey) {
        Map<String, String> sysProp = System.getenv();
        String value = sysProp.getOrDefault(envKey, null);
        if (value != null && !value.trim().isEmpty()) {
            properties.setProperty(configKey, value);
        }
    }

}
