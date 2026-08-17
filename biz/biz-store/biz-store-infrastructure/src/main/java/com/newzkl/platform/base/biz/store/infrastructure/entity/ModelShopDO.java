package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

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
     * 状态:0正常，1已禁用
     */
    private CommonEnum.YesOrNo state;
}