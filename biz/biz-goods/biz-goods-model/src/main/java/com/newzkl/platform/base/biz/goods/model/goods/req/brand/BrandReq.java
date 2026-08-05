package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 品牌
 *
 * @author fang
 */
@Data
public class BrandReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 添加人
     */
    private Long accountId;
    /**
     * 名称
     */
    @NotBlank(message = "品牌名称?")
    private String name;
    /**
     * LOGO
     */
    @NotBlank(message = "品牌LOGO?")
    private String logo;
    /**
     * 状态
     */
    private ApprovalStatus state;

    /**
     * 分类id
     */
    private String categoryIdList;
}