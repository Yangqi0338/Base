package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 账号升级请求
 * @date 2023/12/2110:17
 */
@Data
public class AccountAddAmountReq implements Serializable {

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 角色id
     */
    private RoleEnum.CompanyRole role;

    /**
     * 商品金额
     */
    private Integer goodsAmount;

    /**
     * 货款金额
     */
    private Integer supplierAmount;

    /**
     * 自身
     */
    private Boolean myself;


}
