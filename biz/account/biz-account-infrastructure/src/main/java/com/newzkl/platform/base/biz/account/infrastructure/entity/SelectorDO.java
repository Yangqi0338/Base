package com.newzkl.platform.base.biz.account.infrastructure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 甄选师
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SelectorDO extends OperatorClientBaseDO {

    /**
     * 等级
     */
    private Integer level;
}