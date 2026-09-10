package com.newzkl.platform.base.biz.order.model.req;


import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 提交预支付订单请求对象
 * @Author: niu
 * @Date: 2023/4/19 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCreateCommand {
    /**
     * 订单类型 (0:渠道商订单 1:C端订单) 查询
     */
    @NotNull(message = "orderType?")
    private OrderEnum.OrderType orderType;
    /**
     *  收货地址id
     */
    @NotNull
    private Long shipId;
    private ShipVO shipVO;
    /**
     * 渠道商ID 查询
     */
    private Long channelId;
    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * 订单来源平台 openapi(乐态)入口下单时传 LE_TAI 平台内部下单为 null
     * @ext 轴A 订单来源 与商品级 OrderSkuVO.platformType(轴B 供货平台)不是同一语义 不参与三方下单派发
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;
    /**
     * 商品信息
     */
    @NotEmpty(message = "orderGoodsList?")
    private List<OrderItemCommand> orderGoodsList;
    private Long operatorId;
}
