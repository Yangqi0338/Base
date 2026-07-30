package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.template.query.TemplateQuery;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateAddReq;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateUpdateReq;
import com.newzkl.platform.base.biz.sys.model.template.res.TemplateConfigRes;

import java.util.List;

/**
 * 模板配置仓储端口
 *
 * <p>命名说明: biz-store 已有 {@code TemplateRepository} (装修模板), 为避免概念混淆,
 * 本端口带 Config 后缀。</p>
 *
 * @author KC
 */
public interface TemplateConfigRepository {

    /**
     * 新增模板 (含业务 ID 生成与默认态互斥)
     *
     * @param req     新增请求
     * @param creator 创建人账号 id
     * @return 新增模板自增主键
     */
    Long insert(TemplateAddReq req, Long creator);

    /**
     * 修改模板 (仅更新非空字段, 业务 ID/创建人/应用门店数不可改)
     *
     * @param req 修改请求
     * @return 影响行数大于 0 返回 true
     */
    boolean update(TemplateUpdateReq req);

    /**
     * 删除模板
     *
     * @param id 模板自增主键
     * @return 影响行数大于 0 返回 true
     */
    boolean delete(Long id);

    /**
     * 按自增主键查模板
     *
     * @param id 模板自增主键
     * @return 模板视图, 不存在返回 null
     */
    TemplateConfigRes detail(Long id);

    /**
     * 分页查模板列表
     *
     * @param query 查询条件
     * @return 模板视图列表
     */
    List<TemplateConfigRes> page(TemplateQuery query);

    /**
     * 按业务 ID 设为默认模板 (先取消其他默认)
     *
     * @param templateId 模板业务 ID
     * @return 影响行数大于 0 返回 true
     */
    boolean setDefault(String templateId);

    /**
     * 查当前默认模板
     *
     * @return 默认模板视图, 无默认模板返回 null
     */
    TemplateConfigRes getDefault();

    /**
     * 查当前最大模板业务 ID
     *
     * @return 最大业务 ID, 无数据返回 null
     */
    String selectMaxTemplateId();

    /**
     * 模板操作 (启用/禁用/设为默认)
     *
     * @param id        模板自增主键
     * @param operation 操作类型: 1-启用, 0-禁用, 2-设为默认
     * @return 影响行数大于 0 返回 true
     */
    boolean operate(Long id, int operation);
}
