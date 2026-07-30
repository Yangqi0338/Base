package com.newzkl.platform.base.biz.sys.domain.service;

import com.newzkl.platform.base.biz.sys.model.template.query.TemplateQuery;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateAddReq;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateUpdateReq;
import com.newzkl.platform.base.biz.sys.model.template.res.TemplateConfigRes;

import java.util.List;

/**
 * 模板配置领域服务
 *
 * <p>命名说明: biz-store 已有 {@code TemplateService} (装修模板), 本服务带 Config 后缀
 * 并遵循 biz-sys 的 Domain 后缀约定, 避免 bean 简名冲突。</p>
 *
 * @author KC
 */
public interface TemplateConfigDomain {

    /**
     * 新增模板
     *
     * @param req 新增请求
     * @return 新增模板自增主键
     */
    Long create(TemplateAddReq req);

    /**
     * 编辑模板
     *
     * @param req 修改请求
     */
    void update(TemplateUpdateReq req);

    /**
     * 删除模板
     *
     * @param id 模板自增主键
     */
    void delete(Long id);

    /**
     * 模板详情
     *
     * @param id 模板自增主键
     * @return 模板视图, 不存在返回 null (与源行为一致)
     */
    TemplateConfigRes detail(Long id);

    /**
     * 分页查询模板列表
     *
     * @param query 查询条件
     * @return 模板视图列表
     */
    List<TemplateConfigRes> page(TemplateQuery query);

    /**
     * 设置默认模板
     *
     * @param templateId 模板业务 ID
     */
    void setDefault(String templateId);

    /**
     * 获取当前默认模板
     *
     * @return 默认模板视图, 无默认模板返回 null (与源行为一致)
     */
    TemplateConfigRes getDefault();

    /**
     * 模板操作 (启用/禁用/设为默认)
     *
     * @param id        模板自增主键
     * @param operation 操作类型: 1-启用, 0-禁用, 2-设为默认
     */
    void operate(Long id, int operation);
}
