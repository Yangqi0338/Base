package com.newzkl.platform.base.biz.socialbang.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.ActivityOtherConfigDO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityOtherConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 活动其他配置数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface ActivityOtherConfigDAO extends BaseMapper<ActivityOtherConfigDO> {

    /**
     * 查询其他配置
     *
     * @param otherConfigId 配置id
     * @param channelId     渠道商id
     * @return 其他配置
     */
    ActivityOtherConfigVO otherConfig(@Param("otherConfigId") Long otherConfigId, @Param("channelId") Long channelId);
}
