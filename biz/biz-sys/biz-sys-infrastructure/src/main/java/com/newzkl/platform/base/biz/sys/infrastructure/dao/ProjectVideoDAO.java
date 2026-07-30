package com.newzkl.platform.base.biz.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.ProjectVideoDO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目视频 DAO
 *
 * @author KC
 */
@Mapper
public interface ProjectVideoDAO extends BaseMapper<ProjectVideoDO> {

    /**
     * 构建按项目 id 的视频查询条件 (按 index 升序)
     *
     * @param projectId 项目 id
     * @return 查询条件
     */
    default LambdaQueryWrapper<ProjectVideoDO> getLwByProjectId(Long projectId) {
        return new BaseLambdaQueryWrapper<ProjectVideoDO>()
                .notNullEq(ProjectVideoDO::getProjectId, projectId)
                .orderByAsc(ProjectVideoDO::getIndex);
    }
}
