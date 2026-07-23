package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;
import lombok.Data;

import java.util.List;

/**
 * SPU级订单分页请求参数
 * @author sijiwang
 */
@Data
public class SpuOrderPageReq extends Page<SpuOrderVO> {

    /**
     * spu订单号
     */
    private String spuOrderNo;
    /**
     * ID集合
     */
    private List<String> spuOrderNoList;
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
    /**
     * 订单类型 (0:渠道商订单 1:c端订单) 查询
     */
    private Integer sourceType;
    /**
     * C端ID
     */
    private Long memberId;

    /**
     * C端ID集合
     */
    private List<Long> memberIdList;

    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 渠道商ID
     */
    private List<Long> channelIdList;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商ID列表
     */
    private List<Long> supplierIdList;

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 交易单号
     */
    private String orderNo;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * SPU名称
     */
    private String spuName;
    /**
     * 渠道商名称
     */
    private String channelName;
    /**
     * 创建开始时间
     */
    private String createBeginTime;
    /**
     * 创建结束时间
     */
    private String createEndTime;
    /**
     * 订单状态
     */
    private Integer orderState;
    /**
     * 订单状态集合
     */
    private List<Integer> orderStateList;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private Integer orderType;
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
    private Integer memberAmountMin;
    /**
     * 订单价格最大值
     */
    private Integer memberAmountMax;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 购买方式:0 激活,1 自购
     * @see OrderEnum.BuyMode
     */
    private String buyMode;

    /**
     * 支付开始时间
     */
    private String payBeginTime;
    /**
     * 支付结束时间
     */
    private String payEndTime;

    /**
     * 退款状态 0:未退款 1:退款
     */
    private Integer refund;

    /**
     * 门店账号
     */
    private String storeAccount;
}
