package com.newzkl.platform.base.common.ddd.facade;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description: 条件参数
 * @date 2023/12/1811:24
 */
@Data
public class ConditionCommand implements Serializable {

    /**
     * 团队增加数量
     */
    private Map<Integer, List<TeamUserCountCommand>> teamAddMap;

    /**
     * 购买的礼包
     */
    private PackGoodsInfo packInfo;

    /**
     * 订单流水
     * 0：直属下级总流水
     * 1：非直属下级总流水
     */
    private Map<Integer, Integer> amountScopeMap;

    /**
     * 商品审核通过数量
     */
    private Integer goodsAuditCount;

    /**
     * 角色id
     *
     */
    private Long roleId;

    /**
     * 升级账号id
     *
     */
    private Long id;

    /**
     * 直推下级id
     *
     */
    private Long directId;
}
