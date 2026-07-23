package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.user.model.relation.res.PackGoodsInfo;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 条件参数。
 *
 * @author fang
 */
@Data
public class ConditionReq extends BaseReq {

    /**
     * 团队增加数量
     */
    private Map<Integer, List<TeamUserCountReq>> teamAddMap;

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
     */
    private RoleEnum.CompanyRole role;

    /**
     * 升级账号id
     */
    private Long id;

    /**
     * 直推下级id
     */
    private Long directId;
}
