package com.newzkl.platform.base.biz.account.facade.model;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class SupplierSimpleVO extends BaseRes {
    /**
     * 头像
     */
    private String headImg;
    /**
     * 供应商名称
     */
    private String supplierName;

}