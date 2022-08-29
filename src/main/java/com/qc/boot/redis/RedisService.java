package com.qc.boot.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Map;

/**
 * 功能描述: redis service
 *
 * @author lijinhua
 * @date 2022/8/29 17:34
 */
@Component
public class RedisService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisClient redisClient;

    /** 创建一个同步连接的pool
     * Commons Pool的一个对象池，用于缓存Redis连接。
     * 因为Lettuce本身是基于Netty的异步驱动，在异步访问时并不需要创建连接池，
     * 但基于Servlet模型的同步访问时，连接池是有必要的。
     * 连接池在@PostConstruct方法中初始化，在@PreDestroy方法中关闭
     * */
    private GenericObjectPool<StatefulRedisConnection<String, String>> redisConnectionPool;

    @PostConstruct
    public  void  init(){
        //todo
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        this.redisConnectionPool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), poolConfig);
    }

    @PreDestroy
    public  void  shutdown(){
        //todo
        this.redisConnectionPool.close();
        this.redisClient.shutdown();
    }

    /** 同步执行方法，触发回调*/
    public <T> T executeSync(SyncCommandCallback<T> callback) {
        try (StatefulRedisConnection<String, String> connection = redisConnectionPool.borrowObject()) {
            connection.setAutoFlushCommands(true);
            //这个是回调参数
            RedisCommands<String, String> commands = connection.sync();
            //执行回调，set中可以拿到commands做返回了
            return callback.doInConnection(commands);
        } catch (Exception e) {
            logger.warn("executeSync redis failed.", e);
            throw new RuntimeException(e);
        }
    }

    public String set(String key, String value) {
        return executeSync(commands -> commands.set(key, value));
    }

    public String set(String key, String value, Duration timeout) {
        return executeSync(commands -> commands.setex(key, timeout.toSeconds(), value));
    }

    public String get(String key) {
        return executeSync(commands -> commands.get(key));
    }

    public boolean hset(String key, String field, String value) {
        return executeSync(commands -> commands.hset(key, field, value));
    }

    public String hget(String key, String field) {
        return executeSync(commands -> commands.hget(key, field));
    }

    public Map<String, String> hgetall(String key) {
        return executeSync(commands -> commands.hgetall(key));
    }




}
