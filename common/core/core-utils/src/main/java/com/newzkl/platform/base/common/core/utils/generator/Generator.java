package com.newzkl.platform.base.common.core.utils.generator;

/**
 * 编码 / ID 生成器接口。
 *
 * @author fang
 */
public interface Generator {
    /**
     * 生成下一个数值 ID。
     *
     * @param entity 上下文参数
     * @return 生成的数值
     */
    Number nextId(Object entity);

    /**
     * 生成下一个字符串编码。
     *
     * @param entity 上下文参数
     * @return 生成的编码
     */
    String nextUUID(Object entity);

    /**
     * 设置业务类型。
     *
     * @param bizType 业务类型
     */
    default void setBizType(BusinessType bizType) {

    }

}
