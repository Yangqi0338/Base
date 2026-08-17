package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号升级请求
 *
 * @author muc_fang
 * @date 2023/12/21 10:17
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
     * @ext Money, 落库 BIGINT 分
     */
    private Money goodsAmount;

    /**
     * 货款金额
     * @ext Money, 落库 BIGINT 分
     */
    private Money supplierAmount;

    /**
     * 自身
     */
    private Boolean myself;


}
