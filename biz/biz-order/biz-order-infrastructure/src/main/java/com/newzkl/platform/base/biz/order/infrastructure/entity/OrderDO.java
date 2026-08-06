package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description: 交易单
 * @date 2023/11/1014:50
 */
@Data
public class OrderDO extends BaseDO {

    /**
     * 交易单号
     */
    private String orderNo;
    
    /**
     * 订单类型 : 0 渠道商选品下单, 1 c端铺货下单
     */
    private Integer orderType;
    
    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 外部平台来源(三方单) HUI_DING_HUO/LE_TAI 非外部单为 null
     */
    private PlatformTypeEnum platformType;

    /**
     * 运营商ID
     */
    private Long operatorId;
    
    private Long channelId;

    @JsonSerializable
    private ShipVO shipVO;
    
    private String remark;
    
    /**
     * 货款金额
     */
    private Money supplierAmount;
    
    /**
     * 选品金额
     */
    private Money goodsAmount;
    
    /**
     * 铺货金额
     */
    private Money storeAmount;
    
    /**
     * 选品运费
     */
    private Money freightAmount;
    
    /**
     * 自营运费
     */
    private Money customFreightAmount;
    
    /**
     * 优惠金额
     */
    private Money discountAmount;
    
    /**
     * 服务费: 渠道商应付
     */
    private Money serviceAmount;
    
    /**
     * 渠道商待支付金额
     */
    private Money totalAmount;
    
    /**
     * C端待支付金额
     */
    private Money memberAmount;
    
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

    @JsonSerializable
    private OrderSnapVO orderSnapVO;
    
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
