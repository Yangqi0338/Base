package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: 交易单
 * @date 2023/11/1014:50
 */
@Data
public class OrderDO extends BaseDO {
    
    /**
     * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
     */
    private Integer orderType;
    
    /**
     * 外部订单号
     */
    private String outOrderNo;
    
    /**
     * 运营商ID
     */
    private Long operatorId;
    
    private Long channelId;
    
    private String shipVO;
    
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
    
    private Integer orderState;
    
    /**
     * C端支付状态 0 未支付 1 已支付
     */
    private Integer memberPayState;
    
    /**
     * 渠道商支付状态 0 未支付 1 已支付
     */
    private Integer channelPayState;
    
    private LocalDateTime payTime;
    
    private Integer payType;
    
    private String orderStateLog;
    
    private String orderSnapVO;
    
    private Long storeId;
    
    private Long memberId;
    
    /**
     * 账号id(account.id)
     */
    private Long accountId;
    
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
    
    /**
     * 账号
     */
    private String username;
}
