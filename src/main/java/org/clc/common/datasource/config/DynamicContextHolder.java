package org.clc.common.datasource.config;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 多数据源上下文
 */
public class DynamicContextHolder {

    /**
     * 使用ThreadLocal存储数据源
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     */
    public static void push(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
    }

    /**
     * 获取数据源
     */
    public static String peek() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空当前线程数据源
     */
    public static void poll() {
        CONTEXT_HOLDER.remove();
    }
}