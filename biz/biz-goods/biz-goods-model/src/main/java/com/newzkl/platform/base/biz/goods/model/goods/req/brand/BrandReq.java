package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 品牌
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BrandReq extends BaseReq {
    /**
     * 添加人
     */
    private Long accountId;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * LOGO
     */
    @NotBlank
    private String logo;
    /**
     * 状态
     */
    private AuditEnum.ApprovalStatus state;

    /**
     * 分类id
     */
    private String categoryIdList;
}