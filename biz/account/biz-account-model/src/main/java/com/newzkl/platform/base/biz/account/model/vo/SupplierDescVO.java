package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/2017:33
 */
@Data
public class SupplierDescVO {
    /**
     * ID (查询)
     */
    private Long id;
    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 适配: 企业名称
     */
    private String companyName;
}
