package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResourcePictureMenuDAO继承基类
 */
@Mapper
@Repository
public interface ResourcePictureMenuDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<ResourcePictureMenuDO> {

    /**
     * 查询目录
     * @param channelId
     * @return
     */
    List<PictureMenuVO> queryMenu(Long channelId);
}