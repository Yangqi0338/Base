package com.newzkl.platform.base.biz.sys.model.template.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板业务 ID 请求对象
 *
 * <p>迁移说明: 源 {@code TemplateIdCommand}, 用于按业务 ID (如 YB0001) 操作模板。</p>
 *
 * @author KC
 */
@Data
public class TemplateIdReq implements Serializable {

    /**
     * 模板业务 ID (如 YB0001)
     */
    @NotNull(message = "模板业务ID不能为空")
    private String templateId;
}
