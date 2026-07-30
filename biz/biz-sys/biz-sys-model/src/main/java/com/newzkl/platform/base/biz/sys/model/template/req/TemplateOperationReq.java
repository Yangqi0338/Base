package com.newzkl.platform.base.biz.sys.model.template.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板操作请求对象
 *
 * <p>迁移说明: 源 {@code TemplateOperationCommand}, 用于启用/禁用/设为默认。</p>
 *
 * @author KC
 */
@Data
public class TemplateOperationReq implements Serializable {

    /**
     * 模板自增主键
     */
    @NotNull(message = "模板ID不能为空")
    private Long id;

    /**
     * 操作类型: 1-启用, 0-禁用, 2-设为默认
     */
    @NotNull(message = "操作类型不能为空")
    private Integer operation;
}
