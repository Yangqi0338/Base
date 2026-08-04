package com.newzkl.platform.base.biz.order.model.req.query;


import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
* SPU订单
* @author fang
*/
@Data
public class SpuOrderQuery extends BizPageQuery {
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
    public void setSkuId(Long skuId) {
        this.skuIdList = doWrapperList(skuIdList,skuId);
    }
    /**
     * 订单类型 (0:渠道商订单 1:c端订单) 查询
     */
    private OrderEnum.OrderType orderType;
    /**
     * 渠道商ID
     */
    private List<Long> channelIdList;

    public void setChannelId(Long channelId) {
        this.channelIdList = doWrapperList(channelIdList,channelId);
    }
    /**
     * 供应商ID
     */
    private List<Long> supplierIdList;

    public void setSupplierId(Long supplierId) {
        this.supplierIdList = doWrapperList(supplierIdList,supplierId);
    }
    /**
     * C端ID
     */
    private List<Long> memberIdList;

    public void setMemberId(Long memberId) {
        this.memberIdList = doWrapperList(memberIdList,memberId);
    }
    public Long getMemberId() {
        return CollUtil.getFirst(memberIdList);
    }
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 订单ID
     */
    private List<Long> orderIdList;

    public void setOrderId(Long orderId) {
        this.orderIdList = doWrapperList(orderIdList,orderId);
    }

    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * SPU名称
     */
    private String spuName;
    /**
     * SPU id
     */
    private List<Long> spuIdList;

    public void setSpuId(Long spuId) {
        this.spuIdList = doWrapperList(spuIdList,spuId);
    }

    /**
     * 渠道商名称
     */
    private String channelName;
    /**
     * 订单状态
     */
    private List<OrderEnum.State> orderStateList;

    public void setOrderState(OrderEnum.State orderState) {
        this.orderStateList = doWrapperList(orderStateList,orderState);
    }
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 收货人手机号
     */
    private String shipPhone;
    /**
     * 运营类型 0 机构 1 行业 2 区域
     */
    private Integer type;
    /**
     * 订单价格最小值
     */
    private Integer[] memberAmount;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 购买方式:0 激活,1 自购
     */
    private String buyMode;
    /**
     * 支付时间
     */
    private String[] payTime;

    /**
     * 退款状态 0:未退款 1:退款
     */
    private Integer refund;

    /**
     * 门店账号
     */
    private String storeAccount;

    private CommonEnum.YesOrNo settleSendStatus;
}
