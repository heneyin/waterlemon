package com.henvealf.watermelon.redis.util;

import com.google.common.collect.Lists;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;

/**
 * RedisTemplate 工具类。
 * @author hongliang.yin/Henvealf
 * @date 2019-12-04
 */
public class RedisTemplateUtil {

    /**
     * 使用通配符扫描得到 key。
     * @param pattern 通配符
     * @param maxCount key 的最大数目
     * @param redisTemplate redisTemplate
     * @return key list
     */
    public static List<String> scanKeysByPattern(String pattern, int maxCount, RedisTemplate<String, ?> redisTemplate) {
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            List<String> result = Lists.newArrayList();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(pattern).count(maxCount).build());
            while (cursor.hasNext()) {
                byte[] next = cursor.next();
                result.add(new String(next));
            }
            return result;
        });
    }

    /**
     * 使用通配符扫描得到 key，最多可得到 Integer.MAX_VALUE 个结果。
     * @param pattern 通配符
     * @param redisTemplate redisTemplate
     * @return key list
     */
    public static List<String> scanKeysByPattern(String pattern, RedisTemplate<String, ?> redisTemplate) {
        return scanKeysByPattern(pattern, Integer.MAX_VALUE, redisTemplate);
    }
}
