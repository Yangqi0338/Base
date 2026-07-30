package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营商市场建议配置
 *
 * <p>迁移自 {@code com.zkl.scm.market.infrastructure.entity.MarketSuggestTagConfig}
 * (旧手写 MyBatis XML), 改为 MyBatis-Plus 实体。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("market_suggest_tag_config")
public class MarketSuggestTagConfigDO extends BaseDO {

    /**
     * 标签配置 (JSON 数组字符串)
     */
    private String tagConfig;

    /**
     * 现有资源标签 (JSON 数组字符串)
     */
    private String nowTag;

    /**
     * 客户ID (运营商账户ID)
     */
    private Long accountId;

    /**
     * 是否开启标签选择
     */
    private Integer tagConfigSelect;

    /**
     * 是否开启现有资源标签
     */
    private Integer nowTagSelect;

    /**
     * 最小选择数量
     */
    private Integer minNum;
}
