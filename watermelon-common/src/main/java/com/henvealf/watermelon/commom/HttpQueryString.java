package com.henvealf.watermelon.commom;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 代表 URL 查询部分，用于构建 URL 查询部分，并在构建过程中对查询键值对进行编码，
 * 编码格式为 <strong>utf-8</strong>
 * <p>
 * 来源 《Java 网络编程》 Elliotte 著。 P152
 * <p>
 * Created by Henvealf on 2017/3/18.
 * <p>
 * http://git.oschina.net/henvealf
 */
public class HttpQueryString {

    private StringBuilder query = new StringBuilder();
    private String ENC = "UTF-8";      // 编码格式

    public HttpQueryString() {
    }

    public HttpQueryString(String charSet) {
        this.ENC = charSet;
    }

    /**
     * 添加查询参数键值对。
     * @param name
     * @param value
     */
    public synchronized void add(String name, String value) {
        query.append('&');
        encode(name, value);
    }

    /**
     * 对键值对进行编码，并拼接成查询串
     * @param name
     * @param value
     */
    private synchronized void encode(String name, String value) {
        try {
            query.append(URLEncoder.encode(name,ENC));
            query.append('=');
            query.append(URLEncoder.encode(value,ENC));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("当前 VM 不支持 " + ENC + "编码");
        }
    }

    /**
     * 得到构建的查询串
     * @return
     */
    public synchronized String getQuery(){
        return query.toString();
    }

    @Override
    public String toString() {
        return getQuery();
    }

    public static void main(String[] args) {
        HttpQueryString qs = new HttpQueryString();
        //qs.add();
    }
}
