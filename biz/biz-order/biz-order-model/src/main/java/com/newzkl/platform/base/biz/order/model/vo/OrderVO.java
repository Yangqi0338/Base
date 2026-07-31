package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交易单
 * @author fang
 */
@Data
public class OrderVO extends BaseVO {
     /**
     * 交易单ID
     */
     private Long id;
     /**
      * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
      */
     private OrderEnum.OrderType orderType;
     /**
      * 外部订单号
      */
     private String outOrderNo;
     /**
     * 收货信息值对象
     */
     private String shipVO;
     /**
     * 商品类型 (0:实物 1:课程 2:服务) 查询
     */
     private Integer goodsType;
     /**
      * 运营商ID
      */
     private Long operatorId;
     /**
     * 渠道商ID 查询
     */
     private Long channelId;
     /**
     * 订单备注
     */
     private String remark;
     /**
      * 货款金额
      */
     private Integer supplierAmount;
     /**
      * 选品金额
      */
     private Integer goodsAmount;
     /**
      * 铺货金额
      */
     private Integer storeAmount;
     /**
      * 选品运费
      */
     private Integer freightAmount;
     /**
      * 自营运费
      */
     private Integer customFreightAmount;
     /**
      * 优惠金额
      */
     private Integer discountAmount;
     /**
      * 服务费: 渠道商应付
      */
     private Integer serviceAmount;
     /**
      * 渠道商待支付金额
      */
     private Integer totalAmount;
     /**
      * C端待支付金额
      */
     private Integer memberAmount;
     /**
     * 支付时间
     */
     private LocalDateTime payTime;
     private Integer payType;
     /**
      * 订单状态 (0, "新订单"),(1,"C端待付款"),(2,"渠道商待付款"),(3,"运营商待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭"),
      */
     private OrderEnum.State orderState;
     /**
      * 订单状态流转日志,逗号隔开
      */
     private String orderStateLog;
     /**
      * 支付流水
      */
     private String payFlow;

    /**
     * 购买方式
     */
    private String buyMode;

    /**
     * 用户名
     */
    private String nickname;
}