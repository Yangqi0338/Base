package com.newzkl.platform.base.biz.content.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.content.infrastructure.entity.ArticleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文章 DAO
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.dao.ArticleDAO}(纯 MyBatis-Plus, 无自定义 XML)。
 * 类名加 {@code Content} 域前缀以规避跨域 mapper bean 重名。</p>
 *
 * @author KC
 */
@Mapper
@Repository
public interface ContentArticleDAO extends BaseMapper<ArticleDO> {
}
