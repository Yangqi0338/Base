package com.newzkl.platform.base.biz.sys.model.template.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改模板请求对象
 *
 * <p>迁移说明: 源 {@code TemplateUpdateCommand}。字段全为可选 (null 不更新),
 * 由 MyBatis-Plus {@code updateById} 的空值跳过语义承接源 "仅更新非空字段" 行为。</p>
 *
 * @author KC
 */
@Data
public class TemplateUpdateReq implements Serializable {

    /**
     * 自增主键 (定位修改对象)
     */
    @NotNull(message = "模板ID不能为空")
    private Long id;

    /**
     * 模板名称 (可选, 非空时更新)
     */
    @Size(max = 50, message = "模板名称不能超过50个字符")
    private String templateName;

    /**
     * 背景色 (可选)
     */
    @Size(max = 20, message = "背景色格式不正确")
    private String themeColor;

    /**
     * 主色 (可选)
     */
    @Size(max = 20, message = "主色格式不正确")
    private String domColor;

    /**
     * 次色 (可选)
     */
    @Size(max = 20, message = "次色格式不正确")
    private String secColor;

    /**
     * 主色文字颜色 (可选)
     */
    @Size(max = 20, message = "主色文字颜色格式不正确")
    private String domTextColor;

    /**
     * 次色文字颜色 (可选)
     */
    @Size(max = 20, message = "次色文字颜色格式不正确")
    private String secTextColor;

    /**
     * 负责人 (可选)
     */
    @Size(max = 20, message = "负责人名称不能超过20个字符")
    private String responsiblePerson;

    /**
     * 状态: 0-停用, 1-启用 (可选)
     */
    private Integer status;

    /**
     * 是否默认模板: 0-否, 1-是 (可选)
     */
    private Integer isDefault;
}
