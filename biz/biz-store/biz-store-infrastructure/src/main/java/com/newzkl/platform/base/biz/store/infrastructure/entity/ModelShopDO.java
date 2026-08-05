package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author
 * 样板店
 */
@Data
@TableName("model_shop")
public class ModelShopDO implements Serializable {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 渠道商收益 (Money, 落库 BIGINT 分)
     */
    private Money channelEarning;

    /**
     * 总收益 (Money, 落库 BIGINT 分)
     */
    private Money totalEarning;

    /**
     * 审核状态
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

    private static final long serialVersionUID = 1L;

    /**
     * 累计使用门店数
     */
    private Integer totalUseStoreNum;

    /**
     * 累计下单金额 (Money, 落库 BIGINT 分)
     */
    private Money totalOrderAmount;

    /**
     * 累计下单数
     */
    private Integer totalOrderNum;

    /**
     * 累计支付金额 (Money, 落库 BIGINT 分)
     */
    private Money totalPayAmount;

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