package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 渠道商上下级关系
 * @date 2024/1/1214:52
 */
@Data
public class SupplierRelationVO implements Serializable {
    /**
     * 供应商ID
     */
    private Long id;
    /**
     * 上级甄选师ID
     */
    private Long upSelectorId;
}
