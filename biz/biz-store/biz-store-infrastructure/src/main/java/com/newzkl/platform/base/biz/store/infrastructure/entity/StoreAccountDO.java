package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 门店客户表
 */
@Data
@TableName
public class StoreAccountDO extends BaseDO {

    /**
     * 门店id
     */
    @Index
    private Long storeId;
    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 渠道商ID
     */
    @Index
    private Long channelId;

    /**
     * 支付笔数
     */
    private Integer countPayNumber;

    /**
     * 支付金额
     */
    private Money countPayAmount;

    /**
     * 进店总数
     */
    private Integer countVisitNumber;

    /**
     * 最后进店时间
     */
    private LocalDateTime lastViewTime;

    /**
     * 最后支付时间
     */
    private LocalDateTime lastPayTime;

    /**
     * 最后支付金额
     */
    private Money lastPayAmount;

    /**
     * 0:未拉黑，1已拉黑
     */
    private Integer relationType;

    /**
     * 是否默认：1 是
     */
    private CommonEnum.YesOrNo defult;

}