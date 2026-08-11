package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.VideoSpuRelationDO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

/**
 * 视频-商品关联 DAO
 *
 * @author KC
 */
@Mapper
public interface VideoSpuRelationDAO extends BaseMapper<VideoSpuRelationDO> {

    /**
     * 构建按视频 ID 与类型的查询条件
     *
     * @param videoIdList 视频 ID 列表
     * @param type        视频类型
     * @return 查询条件
     */
    default LambdaQueryWrapper<VideoSpuRelationDO> getLwByVideo(Collection<Long> videoIdList, Integer type) {
        return new BaseLambdaQueryWrapper<VideoSpuRelationDO>()
                .notEmptyIn(VideoSpuRelationDO::getVideoId, videoIdList)
                .notNullEq(VideoSpuRelationDO::getType, type);
    }
}
