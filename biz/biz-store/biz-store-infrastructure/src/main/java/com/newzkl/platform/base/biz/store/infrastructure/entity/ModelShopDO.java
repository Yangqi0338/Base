package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 样板店
 */
@Data
@TableName
public class ModelShopDO extends BaseDO {

    /**
     * 渠道商id
     */
    @Index
    private Long channelId;

    /**
     * 样板店名称
     */
    @Index
    private String modelShopName;

    /**
     * 样板店描述
     */
    private String modelDescription;

    /**
     * 运营商id
     */
    @Index
    private Long operatorId;

    /**
     * 分润配置
     */
    @JsonSerializable
    private String earningConfig;

    /**
     * 渠道商收益
     */
    private Money channelEarning;

    /**
     * 总收益
     */

    /**
     * 审核状态
     */
    @Index
    private AuditEnum.State auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

    /**
     * 样式code
     */
    @Index
    private String styleCode;

    /**
     * 使用门店数
     */
    private Integer useStoreNum;

    /**
     * 累计使用门店数
     */

    /**
     * 累计下单金额
     */

    /**
     * 累计下单数
     */

    /**
     * 累计支付金额
     */

    /**
     * 累计支付订单数
     */

    /**
     * 状态:0正常，1已禁用
     */
    private CommonEnum.YesOrNo state;
}