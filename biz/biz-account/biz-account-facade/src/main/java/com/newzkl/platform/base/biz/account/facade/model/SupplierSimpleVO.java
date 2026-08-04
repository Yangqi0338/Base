package com.newzkl.platform.base.biz.account.facade.model;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class SupplierSimpleVO extends BaseVO {
    /**
     * 头像
     */
    private String headImg;
    /**
     * 供应商名称
     */
    private String supplierName;

}