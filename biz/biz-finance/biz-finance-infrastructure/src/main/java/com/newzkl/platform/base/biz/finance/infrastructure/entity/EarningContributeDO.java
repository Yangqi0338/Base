package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 个人贡献表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class EarningContributeDO extends BaseDO {

    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 客户类型
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 客户姓名
     */
    private String accountName;

    /**
     * 直推上级id
     */
    @Index
    private Long parentId;

    /**
     * 贡献人
     */
    @Index
    private Long contributeId;

    /**
     * 总消费
     */
    private Money totalConsume;

    /**
     * 分润贡献
     */
    private Money earningContribute;

    /**
     * 服务费贡献
     */
    private Money serviceChangeContribute;

}