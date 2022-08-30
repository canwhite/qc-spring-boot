package com.qc.boot.redis;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 功能描述: interface
 *
 * @author lijinhua
 * @date 2022/8/29 17:33
 */
@FunctionalInterface /** 主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错*/
public interface SyncCommandCallback<T> {
    T doInConnection(RedisCommands<String, String> commands);
}
