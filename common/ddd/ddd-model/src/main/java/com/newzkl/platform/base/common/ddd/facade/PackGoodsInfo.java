package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 礼包商品信息
 *
 * @author fang
 */
@Data
public class PackGoodsInfo extends BaseRes {

    /**
     * 订单金额
     */
    private Integer amount;
    /**
     * 礼包等级
     */
    private Integer level;
    /**
     * 礼包类型
     */
    private RoleEnum.CompanyRole type;

}
