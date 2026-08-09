package com.newzkl.platform.base.biz.store.model.template.req;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * 修改样板店req
 */
@Data
public class ModelShopUpdateReq {

    /**
     * 样板Code
     */
    @NotEmpty(message = "样板Code不能为空")
    private String styleCode;

    /**
     * 状态
     * @ext 取值范围: 0=正常, 1=已禁用
     */
    private Integer state;
}
