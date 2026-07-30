package com.newzkl.platform.base.biz.sys.model.template.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板配置视图对象
 *
 * <p>迁移说明: 源 {@code TemplateConfigVO}, 按 Base 约定改名为 Res。id/createTime/
 * updateTime 上提至 {@code BaseRes}。</p>
 *
 * <p>契约变更留档: 源 VO 的 createTime/updateTime 声明为 {@code String}, 而 DO 侧是
 * {@code LocalDateTime}, 源用 {@code BeanUtils.copyProperties} 拷贝, 类型不兼容被跳过,
 * 实际这两个字段恒为 null。此处改用 {@code LocalDateTime} (随 BaseRes), 由 Jackson
 * 序列化为时间串, 前端由 "恒 null" 变为 "有值"。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateConfigRes extends BaseRes {

    /**
     * 模板业务 ID (如 YB0001)
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 背景色 (如 #f144)
     */
    private String themeColor;

    /**
     * 主色
     */
    private String domColor;

    /**
     * 次色
     */
    private String secColor;

    /**
     * 主色文字颜色
     */
    private String domTextColor;

    /**
     * 次色文字颜色
     */
    private String secTextColor;

    /**
     * 状态: 0-停用, 1-启用
     */
    private Integer status;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 是否默认模板: 0-否, 1-是
     */
    private Integer isDefault;

    /**
     * 应用该模板的门店数量 (展示用, 由系统维护)
     */
    private Integer applyStoreCount;
}
