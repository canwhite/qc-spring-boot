package com.qc.boot.config.context;

/**
 * 功能描述: local thread
 *
 * @author lijinhua
 * @date 2022/8/24 14:59
 */
/**实例化一个实现了AutoCloseable接口的类的对象时，
 * close()方法将会自动被调用，确保及时释放资源，
 * 避免可能发生的资源耗尽问题
 * 要实现一个close方法吧
 *
 * */
public class RoutingDataSourceContext implements  AutoCloseable {
    public  static  final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    //在thread local中持有 data source 的key，切换是在ano和aspect
    static  final  ThreadLocal<String> threadLocalDataSourceKey = new ThreadLocal<>();


    //构造函数的时候设置key
    public RoutingDataSourceContext(String key){
        threadLocalDataSourceKey.set(key);
    }


    //获取key,方便在routing config中切换
    public  static  String getDataSourceRoutingKey(){
        String key = threadLocalDataSourceKey.get();
        /**如果传入key为null，就使用master，
         * 默认是不传的
         * 如果传入其他key，就赋值为其他key
         * */
        return key == null ? MASTER_DATASOURCE : key;
    }

    //如果资源释放自动清空
    public void close() {
        threadLocalDataSourceKey.remove();
    }

}
