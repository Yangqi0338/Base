package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.biz.finance.model.enums.user.identity.RoleEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 礼包分润信息简介
 * @date 2023/12/23 16:49
 */
@Data
public class PickPackInfoVO {

    /**
     * 订单金额
     */
    private Integer amount;
    /**
     * 礼包类型
     */
    private RoleEnum.CompanyRole packType;
    /**
     * 礼包等级
     */
    private Integer packLevel;
    /**
     * 礼包名称
     */
    private String packName;
    /**
     * 分润比例
     */
    private Double ratio;
}
