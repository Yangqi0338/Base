package com.newzkl.platform.base.biz.content.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.content.infrastructure.entity.ProjectDO;
import com.newzkl.platform.base.biz.content.model.project.query.ProjectQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目 DAO
 *
 * <p>类名加 {@code Content} 域前缀以规避跨域 mapper bean 重名。</p>
 *
 * @author KC
 */
@Mapper
public interface ContentProjectDAO extends BaseMapper<ProjectDO> {

    /**
     * 构建项目查询条件 (按创建时间倒序)
     *
     * @param query 查询条件
     * @return 查询条件
     */
    default LambdaQueryWrapper<ProjectDO> getLw(ProjectQuery query) {
        return new BaseLambdaQueryWrapper<ProjectDO>()
                .notEmptyIn(ProjectDO::getId, query.getIdList())
                .notEmptyLike(ProjectDO::getName, query.getName())
                .notNullEq(ProjectDO::getProvince, query.getProvince())
                .notNullEq(ProjectDO::getCity, query.getCity())
                .notNullEq(ProjectDO::getArea, query.getArea())
                // basicAmount 已 Money; betweenDate 仅收 Temporal, 改 doBetween 走 Money 边界比较
                .doBetween(ProjectDO::getBasicAmount, query.getBasicStartAmount(), query.getBasicEndAmount())
                .notEmptyLike(ProjectDO::getFlags, query.getFlags())
                .notNullEq(ProjectDO::getInterestNum, query.getInterestNum())
                .notEmptyLike(ProjectDO::getInterestPerson, query.getInterestPerson())
                .between(ProjectDO::getCreateTime, query.getCreateTime())
                ;
    }
}
