package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * 审核样板店req
 *
 * @author niu
 * @date 2024/4/7 16:02
 */
@Data
public class AuditModelShopReq {

    /**
     * 样板编码
     */
    @NotEmpty
    private String styleCode;

    /**
     * 审核状态
     */
    private AuditEnum.State auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

}
