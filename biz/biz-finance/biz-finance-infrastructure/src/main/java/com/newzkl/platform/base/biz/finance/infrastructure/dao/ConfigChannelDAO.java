package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.ConfigChannelDO;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ConfigChannelDAO继承基类
 */
@Mapper
public interface ConfigChannelDAO extends BaseMapper<ConfigChannelDO> {

    default LambdaQueryWrapper<ConfigChannelDO> getLw(Long channelId) {
        return new LambdaQueryWrapper<ConfigChannelDO>()
                .eq(ConfigChannelDO::getChannelId, channelId);
    }

    /**
     * 批量查询渠道商配置
     *
     * @param channelIds
     * @return
     */
    List<BatchQueryConfigChannelRes> batchQueryChannelConfig(@Param("list") List<Long> channelIds);

}