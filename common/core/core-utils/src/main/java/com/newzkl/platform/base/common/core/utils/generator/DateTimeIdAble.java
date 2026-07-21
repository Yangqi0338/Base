package com.newzkl.platform.base.common.core.utils.generator;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于日期时间前缀 + 自增序列的编码生成器。
 *
 * @author fang
 */
public class DateTimeIdAble extends AtomicLong implements Generator {

    private static Integer MODE = 0;

    public DateTimeIdAble() {
        super(0);
    }

    public DateTimeIdAble(long initialValue) {
        super(initialValue);
    }

    public DateTimeIdAble(long initialValue, Integer mode) {
        super(initialValue);
        setMode(mode);
    }

    public static void setMode(Integer mode) {
        MODE = mode;
    }

    /**
     * 获取当前日期字符串。
     */
    private static String getCurrentDate() {
        return DateUtil.date().toString("yyyyMMdd");
    }

    /**
     * 获取当前时间字符串。
     */
    private static String getCurrentTime() {
        return DateUtil.date().toString("HHmmss");
    }

    public static String getPrefix() {
        if (MODE == 0) {
            return getCurrentDate() + getCurrentTime();
        } else if (MODE == 1) {
            return getCurrentDate();
        } else if (MODE == 2) {
            return getCurrentTime();
        }
        return "";
    }

    @Override
    public Number nextId(Object entity) {
        return this.incrementAndGet();
    }

    /**
     * 生成带时间戳的UUID（格式：日期 + 时间 + 序列号）。
     * 不支持截断。
     */
    @Override
    public String nextUUID(Object entity) {
        return getPrefix() + String.format("%0" + Opt.ofNullable(entity).orElse(4) + "d", nextId(entity));
    }
}
