package com.newzkl.platform.base.common.core.utils.generator;

import java.util.List;
import java.util.Random;

/**
 * 基于字库随机取值的生成器基类
 *
 * @author sijiwang
 */
public abstract class PropertiesRandomGenerator implements Generator {

    protected static final Random RANDOM = new Random();

    /**
     * 从列表中随机获取一个元素
     *
     * @param list 待取值列表
     * @param <T>  元素类型
     * @return 随机元素
     */
    protected static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        return list.get(RANDOM.nextInt(list.size()));
    }

    /**
     * 获取候选数据列表
     *
     * @return 候选列表
     */
    public abstract List<String> getDataList();

    @Override
    public Number nextId(Object entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String nextUUID(Object entity) {
        return getRandomElement(getDataList());
    }
}
