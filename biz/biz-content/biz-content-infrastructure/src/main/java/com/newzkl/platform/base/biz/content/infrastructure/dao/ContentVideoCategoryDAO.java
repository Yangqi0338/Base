package com.newzkl.platform.base.biz.content.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.content.infrastructure.entity.VideoCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 视频分类 DAO
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.dao.VideoCategoryDAO}。
 * 旧 {@code VideoCategoryDAO.xml} 为空 mapper(无自定义 SQL), 故不迁 XML。
 * 类名加 {@code Content} 域前缀以规避跨域 mapper bean 重名。</p>
 *
 * @author KC
 */
@Mapper
@Repository
public interface ContentVideoCategoryDAO extends BaseMapper<VideoCategoryDO> {
}
