package com.qc.boot.redis;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 功能描述: interface
 *
 * @author lijinhua
 * @date 2022/8/29 17:33
 */
@FunctionalInterface
public interface SyncCommandCallback<T> {
    T doInConnection(RedisCommands<String, String> commands);
}
