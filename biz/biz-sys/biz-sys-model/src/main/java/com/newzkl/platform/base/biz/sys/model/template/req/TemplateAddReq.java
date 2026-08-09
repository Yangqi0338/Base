package com.newzkl.platform.base.biz.sys.model.template.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增模板请求对象
 *
 * <p>迁移说明: 源 {@code TemplateAddCommand}, 按 Base 约定改名为 Req 落 model 层。
 * 校验注解由 {@code jakarta.validation} 换为 {@code jakarta.validation} (Spring Boot 3),
 * 提示文案逐字保留。</p>
 *
 * @author KC
 */
@Data
public class TemplateAddReq implements Serializable {

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 50, message = "模板名称不能超过50个字符")
    private String templateName;

    /**
     * 背景色 (如 #f144)
     */
    @NotBlank(message = "背景色不能为空")
    @Size(max = 20, message = "背景色格式不正确")
    private String themeColor;

    /**
     * 主色
     */
    @NotBlank(message = "主色不能为空")
    @Size(max = 20, message = "主色格式不正确")
    private String domColor;

    /**
     * 次色
     */
    @NotBlank(message = "次色不能为空")
    @Size(max = 20, message = "次色格式不正确")
    private String secColor;

    /**
     * 主色文字颜色
     */
    @NotBlank(message = "主色文字颜色不能为空")
    @Size(max = 20, message = "主色文字颜色格式不正确")
    private String domTextColor;

    /**
     * 次色文字颜色
     */
    @NotBlank(message = "次色文字颜色不能为空")
    @Size(max = 20, message = "次色文字颜色格式不正确")
    private String secTextColor;

    /**
     * 负责人
     */
    @NotBlank(message = "负责人不能为空")
    @Size(max = 20, message = "负责人名称不能超过20个字符")
    private String responsiblePerson;

    /**
     * 是否默认模板
     *
     * @ext 取值: 0-否 (默认), 1-是
     */
    private Integer isDefault = 0;
}
