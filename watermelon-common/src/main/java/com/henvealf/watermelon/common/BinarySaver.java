package com.henvealf.watermelon.common;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用 URLConnection 读取网络上的二进制资源, 并保存。
 * 关键是提前获得二进制资源的长度。
 * Created by Henvealf on 2017/3/18.
 * http://git.oschina.net/henvealf
 */
public class BinarySaver {

    public static void main(String[] args) {
        try {
            URL u = new URL("http://dlsw.baidu.com/sw-search-sp/soft/85/22235/10000177.1375530509.exe");
            saveBinaryFile(u);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBinaryFile( URL u ) throws IOException {
        URLConnection conn = u.openConnection();
        String contentType = conn.getContentType();
        int contentLength = conn.getContentLength();

        if( contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("this is not a binary file");
        }

        // 开始读取数据
        try (InputStream raw = conn.getInputStream()) {
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];

            int offset = 0;
            // 数据偏移量，相对于 data 数组的起始处。
            // 第三个参数 len 是希望读取的长度，但很有可能小于 len
            while(offset < contentLength) {
                int bytesRead = in.read(data, offset,data.length - offset);
                if(bytesRead == -1) break;
                offset += bytesRead;
            }

            // check
            if (offset != contentLength) {
                throw  new IOException("error read two less");
            }

            String fileName = u.getFile();
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            // 写出
            try (FileOutputStream fout = new FileOutputStream(fileName)) {
                fout.write(data);
                fout.flush();
                fout.close();
            }
        }
    }

}
