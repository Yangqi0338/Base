package com.newzkl.platform.base.biz.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.TemplateConfigDO;
import com.newzkl.platform.base.biz.sys.model.template.query.TemplateQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板配置 DAO
 *
 * <p>迁移说明: 源 {@code TemplateConfigDAO} 全部 SQL 写在 {@code TemplateConfigDao.xml},
 * 此处改由 MyBatis-Plus 条件构造器等价表达, 不再需要 XML。默认默认模板标记值见
 * {@link TemplateConfigDAO#DEFAULT_ON}。</p>
 *
 * @author KC
 */
@Mapper
public interface TemplateConfigDAO extends BaseMapper<TemplateConfigDO> {

    /**
     * 默认模板标记: 是
     */
    Integer DEFAULT_ON = 1;

    /**
     * 构建模板查询条件 (按创建时间倒序)
     *
     * <p>源 XML 支持 {@code ${query.sortSQL}} 动态排序, 为规避 SQL 拼接风险, 此处固定
     * 按创建时间倒序 (与源 sortSQL 为空时的兜底排序一致)。</p>
     *
     * @param query 查询条件
     * @return 查询条件
     */
    default LambdaQueryWrapper<TemplateConfigDO> getLw(TemplateQuery query) {
        return new BaseLambdaQueryWrapper<TemplateConfigDO>()
                .notNullEq(TemplateConfigDO::getTemplateId, query.getTemplateId())
                .notEmptyLike(TemplateConfigDO::getTemplateName, query.getTemplateName())
                .notEmptyLike(TemplateConfigDO::getResponsiblePerson, query.getResponsiblePerson())
                .notNullEq(TemplateConfigDO::getStatus, query.getStatus())
                .orderByDesc(TemplateConfigDO::getCreateTime);
    }

    /**
     * 构建按业务 ID 精确匹配条件
     *
     * @param templateId 模板业务 ID
     * @return 查询条件
     */
    default LambdaQueryWrapper<TemplateConfigDO> getLwByTemplateId(String templateId) {
        return new BaseLambdaQueryWrapper<TemplateConfigDO>()
                .notNullEq(TemplateConfigDO::getTemplateId, templateId);
    }

    /**
     * 构建默认模板查询条件
     *
     * @return 查询条件
     */
    default LambdaQueryWrapper<TemplateConfigDO> getLwDefault() {
        return new BaseLambdaQueryWrapper<TemplateConfigDO>()
                .notNullEq(TemplateConfigDO::getIsDefault, DEFAULT_ON)
                .last("limit 1");
    }
}
