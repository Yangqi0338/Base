package com.newzkl.platform.base.biz.store.model.template.dto;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 样板店
 */
@Data
public class ModelShopDTO extends BaseDTO {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 样板店描述
     */
    private String modelDescription;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 分润配置
     */
    private String earningConfig;

    /**
     * 渠道商收益
     */
    private Money channelEarning;

    /**
     * 总收益
     */
    private Money totalEarning;

    /**
     * 审核状态
     */
    private  AuditEnum.State auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 使用门店数
     */
    private Integer useStoreNum;

    /**
     * 累计使用门店数
     */
    private Integer totalUseStoreNum;

    /**
     * 累计下单金额
     */
    private Money totalOrderAmount;

    /**
     * 累计下单数
     */
    private Integer totalOrderNum;

    /**
     * 累计支付金额
     */
    private Money totalPayAmount;

    /**
     * 累计支付订单数
     */
    private Integer totalPayNum;

    /**
     * 状态
     */
    private CommonEnum.YesOrNo state;
}