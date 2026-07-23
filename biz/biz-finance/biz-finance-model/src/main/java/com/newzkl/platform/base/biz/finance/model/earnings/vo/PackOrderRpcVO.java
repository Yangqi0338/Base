package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 礼包订单基础VO
 * @date 2023/12/2110:17
 */
@Data
public class PackOrderRpcVO implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 下单账号id
     */
    private Long accountId;
    /**
     * 订单金额
     */
    private Integer amount;
    /**
     * 礼包等级
     */
    private Integer packLevel;
    /**
     * 礼包类型
     */
    private RoleEnum.CompanyRole packType;
    /**
     * 礼包名称
     */
    private String packName;
    /**
     * 礼包Id
     */
    private Long packId;
    /**
     * 订单状态 查询 (0,新订单),(2,待支付),(4,待发货),(6,已发货),(8,已收货),(10,已完成),(-1,已关闭)
     */
    private Integer state;
}
