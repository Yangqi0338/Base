package com.newzkl.platform.base.biz.content.model.enums;

import java.util.Arrays;

/**
 * 内容域推荐人群枚举
 *
 * <p>迁移说明: 旧实现直接引用 {@code com.zkl.scm.model.constants.finance.FinanceEnum.FinanceUser}
 * 的 {@code CHANNEL} / {@code C_CLIENT} 两个枚举名作为 {@code recommend_groups} 列的存储值。
 * Base 侧 {@code FinanceEnum} 落在 {@code biz-store-model}, 内容域不得反向依赖门店域,
 * 故在本域重新声明同名枚举项, <b>枚举名必须与旧库存量数据逐字一致</b>。</p>
 *
 * @author KC
 */
public enum RecommendGroupEnum {

    /**
     * 渠道商
     *
     * <p>对应旧 {@code FinanceEnum.FinanceUser.CHANNEL}, 角色ID 1002</p>
     */
    CHANNEL,

    /**
     * C端用户
     *
     * <p>对应旧 {@code FinanceEnum.FinanceUser.C_CLIENT}, 角色ID 1000</p>
     */
    C_CLIENT;

    /**
     * 判断给定名称是否为合法推荐人群
     *
     * @param name 推荐人群名称, 允许为 null
     * @return true 表示合法
     */
    public static boolean contains(String name) {
        return Arrays.stream(values()).anyMatch(it -> it.name().equals(name));
    }
}
