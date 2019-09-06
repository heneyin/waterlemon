package com.henvealf.watermelon.common;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-09-06
 */
public class JasyptUtil {

    private static class JasyptConfig {

        private String password = "betterNow123!SJMP:$%DG$";
        private char[] passwordChar = password.toCharArray();
        public StringEncryptor stringEncryptor() {
            SimpleStringPBEConfig config = new SimpleStringPBEConfig();
            config.setPasswordCharArray(passwordChar);
            config.setAlgorithm("PBEWithMD5AndDES");
            config.setKeyObtentionIterations("1000");
            config.setPoolSize("1");
            config.setProviderName("SunJCE");
            config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
            config.setStringOutputType("base64");
            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            encryptor.setConfig(config);
            return encryptor;
        }

        public void setPassword(String password) {
            if (password != null && password.length() > 0) {
                this.passwordChar = password.toCharArray();
            }
        }
    }

    /**
     * 尽可能的解码 string，如果字符串没有 ENC() 标识, 则返回原字符串.
     * @param string
     * @return
     */
    public static String decryptIfCan(String string) {
        if (string == null) return null;
        if(string.startsWith("ENC(") && string.endsWith(")")) {
            String willDecry = string.substring(4, string.length() - 1);
            JasyptConfig jasyptConfig = new JasyptConfig();
            jasyptConfig.setPassword("betterNow123!SJMP:$%DG$");
            StringEncryptor encryptor = jasyptConfig.stringEncryptor();
            return encryptor.decrypt(willDecry);
        } else {
            return string;
        }
    }

    public static String encrypt(String string) {
        JasyptConfig jasyptConfig = new JasyptConfig();
        StringEncryptor encryptor = jasyptConfig.stringEncryptor();
        String encrypt = encryptor.encrypt(string);
        return "ENC(" + encrypt +")";
    }

}
