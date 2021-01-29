package com.henvealf.watermelon.redis.util;

import com.google.common.base.Preconditions;
import io.rebloom.client.Client;
import org.apache.commons.lang3.ArrayUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-07-18
 */
public class JedisClientPools {

    private static JedisPool instant;
    private static Client bloomFilterClient;
    private static Lock lock = new ReentrantLock();
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JedisClientPools.class);

    public static final String REDIS_CLIENT_MODE_SINGLE = "single";

    public static void initIfNone(String redisUri, String mode, int pollMaxSize) {
        Preconditions.checkNotNull(redisUri, "redis uri is null");
        if (instant == null) {
            try {
                lock.lock();
                if (instant == null) {
                    if (REDIS_CLIENT_MODE_SINGLE.equals(mode)) {
                        instant = createJedisPool(redisUri, pollMaxSize);
                        bloomFilterClient = new Client(instant);
                    } else {
                        throw new RuntimeException("Redis client mode not support: " + mode);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * redisUri: localhost:6379
     */
    private static JedisPool createJedisPool(String redisUri,
                                             int max) {
        java.util.List<URI> uris = parseUriFromString(redisUri);
        URI uri = uris.get(0);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(max);
        return new JedisPool(jedisPoolConfig, uri);
    }

    /**
     * redis://host1:6379,redis://host2:6379
     */
    private static List<URI> parseUriFromString(String uris) {
        String[] uriArray = uris.split("\\s*,\\s*");
        if (ArrayUtils.isEmpty(uriArray)) {
            throw new RuntimeException("redis urls not formatter: " + uris);
        }
        return Arrays.stream(uriArray).map(URI::create).collect(Collectors.toList());
    }

    public static JedisPool getInstant() {
        return instant;
    }

    public static String get(String key) {
        try (Jedis jedis = instant.getResource()) {
            return jedis.get(key);
        }
    }

    public static String set(String key, String value) {
        try (Jedis jedis = instant.getResource()) {
            return jedis.set(key, value);
        }
    }

    public static List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = instant.getResource()) {
            return jedis.lrange(key, start, end);
        }
    }

    public static Map<String, String> hgetAll(String key) {
        try (Jedis jedis = instant.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    // --------------
    // bloom filter
    // -------------
    public static boolean bfAdd(String key, String value) {
        return bloomFilterClient.add(key, value);
    }

    public static boolean bfExists(String key, String value) {
        return bloomFilterClient.exists(key, value);
    }

    public static Boolean sismember(String key, String member) {
        try(Jedis jedis = instant.getResource()) {
            return jedis.sismember(key, member);
        }
    }

    public static void close() {
        if (instant != null) {
            try {
                instant.close();
            } catch (NullPointerException e) {
                // noop
            }
            instant = null;
        }
        logger.info("Stopped RedisClientSingle");
        System.out.println("Stopped RedisClientSingle");
    }

}
