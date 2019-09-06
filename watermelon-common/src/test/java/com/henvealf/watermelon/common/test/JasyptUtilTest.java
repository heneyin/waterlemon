package com.henvealf.watermelon.common.test;

import com.henvealf.watermelon.common.JasyptUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-09-06
 */
public class JasyptUtilTest {
    @Test
    public void testsJasypt() throws Exception {
        String rawString = "123456";
        String encrypt = JasyptUtil.encrypt(rawString);
        String decrypt = JasyptUtil.decryptIfCan(encrypt);
        Assert.assertEquals(rawString, decrypt);
    }

    @Test
    public void printEncryptResult() {
        System.out.println(JasyptUtil.encrypt("111"));
    }

    @Test
    public void printDecryptResult() {
        System.out.println(JasyptUtil.decryptIfCan("ENC(1fc53jLMhdqF8K8Xm3Ii7Q==)"));
    }

    @Test
    public void testDecryptIfCan() {
        JasyptUtil.decryptIfCan("ENC(i+Nc/k8pbc/3ctdNa9uAtMl78ZI3z2L3)");
    }


}
