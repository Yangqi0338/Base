package com.newzkl.platform.base.biz.account.model.merchant.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户写入入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.MerchantCommand}。
 * 旧 {@code id} 字段由父类 {@code BaseReq} 提供, 此处不重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantReq extends BaseReq {

    /**
     * 名称 (查询)
     */
    private String name;

    /**
     * 账号名称 (查询)
     */
    private String username;

    /**
     * 营业执照
     */
    private String license;
}
