package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.biz.store.model.enums.AuditEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author niu
 * @description: 审核样板店req
 * @date 2024/4/7 16:02
 */
@Data
public class AuditModelShopReq {

    /**
     * 样板Code
     */
    @NotEmpty(message = "样板Code不能为空")
    private String styleCode;

    /**
     * 审核状态
     * @see AuditEnum.State
     */
    private Integer auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

}
