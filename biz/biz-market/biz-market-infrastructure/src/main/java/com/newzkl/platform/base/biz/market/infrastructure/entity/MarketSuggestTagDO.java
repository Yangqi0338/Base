package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 市场-提交建议标签。
 *
 * <p>迁移自 {@code com.zkl.scm.market.infrastructure.entity.MarketSuggestTag}
 * (旧手写 MyBatis XML), 改为 MyBatis-Plus 实体。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("market_suggest_tag")
public class MarketSuggestTagDO extends BaseDO {

    /**
     * 标签内容 (JSON 数组字符串)。
     */
    private String tagConfig;

    /**
     * 现有资源标签 (JSON 数组字符串)。
     */
    private String nowConfig;

    /**
     * 客户ID。
     */
    private Long accountId;

    /**
     * 直属运营商账户ID。
     *
     * <p>迁移保留旧列名拼写错误 {@code opeartor_id} (正确拼写应为 operator_id),
     * 防止与存量数据脱节; 列名修正属独立数据迁移任务。</p>
     */
    @TableField("opeartor_id")
    private Long opeartorId;

    /**
     * 备注。
     */
    private String remark;

    /**
     * 状态 0:配置中 1:已配置。
     */
    @TableField("`state`")
    private Integer state;

    /**
     * 提交人手机号。
     */
    private String mobile;
}
