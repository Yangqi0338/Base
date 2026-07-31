package com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BuyCreateCdkReq implements Serializable {
    /** 订单ID */
    private Long orderId;
    /**
     * 获取方式 0 发放 1 购买
     */
    private Integer getType;
    /** 兑换码类型 */
    private Integer systemType;
    /** 角色ID */
    private RoleEnum.CompanyRole role;
    /** 数量 */
    private Integer count;
    /** 账号ID */
    private Long accountId;
    /**
     * 分配状态 0 未分配 1 运营商已分配 2 交易师已分配  3 平台已分配
     */
    private Integer toState;
}
