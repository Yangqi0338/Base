package com.newzkl.platform.base.biz.store.model.template.dto;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

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
     * 状态
     */
    private CommonEnum.YesOrNo state;
}