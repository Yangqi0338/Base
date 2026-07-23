package com.newzkl.platform.base.biz.store.model.template.entity;

import com.newzkl.platform.base.biz.store.model.enums.AuditEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 样板店
 */
@Data
public class ModelShop implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

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
    private Integer channelEarning;

    /**
     * 总收益
     */
    private Integer totalEarning;

    /**
     * 审核状态
     *
     * @see AuditEnum.State
     */
    private Integer auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人名
     */
    private String createName;

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
    private Integer totalOrderAmount;

    /**
     * 累计下单数
     */
    private Integer totalOrderNum;

    /**
     * 累计支付金额
     */
    private Integer totalPayAmount;

    /**
     * 累计支付订单数
     */
    private Integer totalPayNum;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 状态:0正常，1已禁用
     */
    private Integer state;
}