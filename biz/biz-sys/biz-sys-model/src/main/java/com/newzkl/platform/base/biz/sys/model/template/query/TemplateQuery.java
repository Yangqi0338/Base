package com.newzkl.platform.base.biz.sys.model.template.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 模板分页查询
 *
 * <p>迁移说明: 源 {@code TemplateQuery extends PageQuery}, 保留 {@code isNonPaged} 字段
 * (源 controller 据此调 {@code resetQueryList()} 关分页); Base 的 {@code PageQuery} 另有
 * {@code nonPaged} 属性语义相同, 但为不改前端契约, 入参名沿用 {@code isNonPaged}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateQuery extends PageQuery implements Serializable {

    /**
     * 模板业务 ID (精确匹配)
     */
    private String templateId;

    /**
     * 模板名称 (模糊匹配)
     */
    private String templateName;

    /**
     * 负责人 (模糊匹配)
     */
    private String responsiblePerson;

    /**
     * 状态: 0-禁用, 1-启用 (精确匹配, 可选)
     */
    private Integer status;

    /**
     * 是否不分页: true 时忽略 pageNo/pageSize 全量返回
     */
    private Boolean isNonPaged;
}
