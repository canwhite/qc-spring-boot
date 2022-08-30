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
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        this.redisConnectionPool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), poolConfig);
    }
    @PreDestroy
    public  void  shutdown(){
        //关闭pool
        this.redisConnectionPool.close();
        //关闭client
        this.redisClient.shutdown();
    }

    /** 同步执行方法，触发回调
     * PS-1:
     * <T> T
     * 如果是泛型返回值，如果直接写个T必然是无法识别，需要一个<T>来修饰一下
     * 这里是从doInConnection继承来的返回值
     * 具体返回什么会在接口实现的时候定义；
     * PS-2:
     * executeSync参数是接口类，可以点语法调用内部接口
     * */
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
        //return executeSync(commands -> commands.set(key, value));
        /** 上边的 return语句可以展开写成这样，在内部确定了返回值，
         *  这里executeSync等于将doInConnection的返回再返回，继承返回
         *  并且在内部构建了doInConnection的上下文，执行调用
         * */
        return  executeSync(new SyncCommandCallback<String>(){
            @Override
            public String doInConnection(RedisCommands<String, String> commands) {
                return  commands.set(key,value);
            }
        });
    }

    /**由此可以看出箭头函数的写法要比上边展开的写法方便很多
     * commands.setex返回值给到executeSync，Sync再给到外部函数
     * */
    public String set(String key, String value, Duration timeout) {
        /** Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值*/
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
