package com.steve.framework;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Description:
 * @Author: stevejobson
 * @CreateDate: 2017/12/18 下午6:37
 */
public class RedisTemplate extends StringRedisTemplate {


    /**
     * 生产环境尽量避切库操作，影响性能，通过合理的配置cachekey
     */

    public static ThreadLocal<Integer> REDIS_DB_INDEX = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        try {
            Integer dbIndex = REDIS_DB_INDEX.get();
            //如果设置了dbIndex
            if (dbIndex != null) {
                if (connection instanceof JedisConnection) {
                    if (((JedisConnection) connection).getNativeConnection().getDB().intValue() != dbIndex) {
                        connection.select(dbIndex);
                    }
                } else {
                    connection.select(dbIndex);
                }
            } else {
                connection.select(0);
            }
        } finally {
            REDIS_DB_INDEX.remove();
        }
        return super.preProcessConnection(connection, existingConnection);
    }
}