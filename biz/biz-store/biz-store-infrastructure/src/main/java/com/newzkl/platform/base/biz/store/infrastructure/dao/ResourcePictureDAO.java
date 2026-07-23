package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.model.resource.req.QueryPictureResourceReq;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResourcePictureDAO继承基类
 */
@Mapper
@Repository
public interface ResourcePictureDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<ResourcePictureDO> {

    /**
     * 查询图片资源
     * @param req
     * @return
     */
    List<PictureResourceVO> queryResource(QueryPictureResourceReq req);
}