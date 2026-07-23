package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营商端通用实体
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorClientBaseDO extends BaseDO {

    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;
}