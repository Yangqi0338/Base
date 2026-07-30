package com.newzkl.platform.base.common.core.utils.spring;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的用户id、用户名称、Token等信息
 * 注意：必须在网关通过请求头传入，并在拦截器设置值，否则无法获取。
 *
 * @author ruoyi
 */
@Component
public class SecurityContextHolder {
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    private static Environment environment;

    public static boolean isLocal() {
        return ArrayUtil.contains(environment.getActiveProfiles(), "local");
    }

    /* 环境不为空或不为prod则为dev */
    public static boolean isDev() {
        return !isProd();
    }

    public static boolean isProd() {
        return environment != null && ArrayUtil.contains(environment.getActiveProfiles(), "prod");
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StrUtil.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return (T) map.getOrDefault(key, null);
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        SecurityContextHolder.environment = environment;
    }
}
