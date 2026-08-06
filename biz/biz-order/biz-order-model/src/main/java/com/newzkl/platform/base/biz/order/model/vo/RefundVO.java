package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.biz.order.model.support.api.ReceiveAddressOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 售后单
 * @author fang
 */
@Data
public class RefundVO extends BaseRes {
     /**
     * 主键
     */
     private Long id;
     /**
      * 外部售后单号
      */
     private String outRefundId;
     /**
     * 订单ID
     */
     private Long orderId;
     /**
      * 申请人角色
      */
     private Long createRole;
     /**
      * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
      */
     private Integer orderType;
     /**
      * 渠道类型 0 供货商品 1 自营商品
      */
     private SpuEnum.ChannelType spuChannelType;
     /**
      * 门店ID
      */
     private Long storeId;
     /**
      * 客户ID
      */
     private Long memberId;
     /**
      * 商户ID
      */
     private Long merchantId;
     /**
      * 渠道商ID
      */
     private Long channelId;
     /**
      * 供应商ID
      */
     private Long supplierId;
     /**
     * SPU订单ID
     */
     private Long spuOrderId;
     /**
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
     private RefundEnum.State refundState;
     /**
     * 售后类型 0仅退款 1退货退款
     */
      private RefundEnum.RefundType refundType;
     /**
     * 售后运费金额
     */
     private Money freightAmount;
     /**
     * 渠道商售后总金额
     */
     private Money refundAmount;
     /**
      * 货款金额
      */
     private Money supplierAmount;
    /**
     * 服务费
     */
    private Money serviceAmount;
     /**
     * 售后原因
     */
     private String reason;
     /**
     * 申请说明
     */
     private String remark;
     /**
     * 申请图片
     */
     private String images;
     /**
     * 联系电话
     */
     private String phone;
     /**
     * 物流公司名称
     */
     private String freightCompanyName;
     /**
     * 物流单号
     */
     private String freightNo;
     /**
     * 收货状态
     */
     private Integer takeDeliveryState;
     /**
     * 退款状态
     */
     private Integer payState;
     /**
     * 审核完成时间
     */
     private LocalDateTime auditTime;
     /**
     * 售后完成时间
     */
     private LocalDateTime refundTime;
     /**
      * 状态变化时间
      */
     private LocalDateTime stateTime;
    /**
     * 状态变化时间-时间戳（毫秒级）
     */
    private Long stateTimeTimestamp;

    /**
     * 退货信息拓展
     */
    private FreightExt freightExtDto;

    /**
     * 退货信息拓展
     */
    private String freightExt;
     /**
      * 售后明细 格式: List<RefundItemVO>
      */
     private List<RefundItemVO> item;
    @JsonIgnore
    private String itemStr;

    public List<RefundItemVO> getItem() {
        if (CollUtil.isEmpty(item) && StrUtil.isNotBlank(itemStr)) {
            item = new ArrayList<>();
            item.addAll(JSONArray.parseArray(itemStr, RefundItemVO.class));
        }
        return item;
    }
     /**
      * 拒绝原因
      */
     private String refuseReason;
     /**
      * 联表: 买家收货信息
      */
     private String shipVO;
     /**
      * 售后流转状态
      */
     private String refundStateLog;
     /**
      * 来源订单状态
      */
     private OrderEnum.State fromOrderState;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 下单时间
     */
    private LocalDateTime orderTime;
    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 商家自动确认截止时间
     */
    private LocalDateTime storeAuToTime;

    /**
     * 收货地址
     */
    private ReceiveAddressOutVO receiveAddress;
}