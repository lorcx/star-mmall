package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author lx
 * @Date 2017/11/18 16:04
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    /**
     * 使用guava内置的缓存
     * initialCapacity 初始化大小
     * maximumSize 最大容量，如果超过该大小使用LRU算法清除
     * expireAfterAccess 失效时间 这里设置12小时
     *
     * LRU算法是最少使用算法
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                /**
                 * 默认的数据加载实现,当调用get取值的时候，
                 * 如果key没有对应的值，就调用这个方法进行加载。
                 * @param s
                 * @return
                 * @throws Exception
                 */
                @Override
                public String load(String s) throws Exception {
                    return "null";// 防止NPE
                }
            });

    public static void setKey(String key ,String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
        } catch (ExecutionException e) {
            logger.error("localCache get error ", e);
        }
        return value;
    }
}
