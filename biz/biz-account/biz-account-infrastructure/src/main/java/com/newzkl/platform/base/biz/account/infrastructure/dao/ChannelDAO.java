package com.newzkl.platform.base.biz.account.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.ChannelDO;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelEarningsConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道商
 *
 * @author fang
 */
@Mapper
public interface ChannelDAO extends BaseMapper<ChannelDO> {

    default BaseLambdaQueryWrapper<ChannelDO> getLw(ChannelQuery query) {
        return new BaseLambdaQueryWrapper<ChannelDO>()
                .notEmptyIn(ChannelDO::getId, query.getIdList())
                .notEmptyIn(ChannelDO::getState, query.getStateList())
                .notEmptyGe(ChannelDO::getState, query.getStateOver())
                .between(ChannelDO::getCreateTime, query.getCreateTime())
                .notEmptyEq(ChannelDO::getContactsName, query.getContactsName())
                .notEmptyEq(ChannelDO::getName, query.getChannelName())
                ;
    }

    ChannelEarningsConfigVO serviceFeeConfigVO(@Param("accountId") Long accountId);

    UpIdRes channelUpId(@Param("accountId") Long accountId);

    void resetUserOrderCount();

    void columnByQuery(@Param("columnList") List<EditColumnVO> columnList, @Param(Constants.WRAPPER) AbstractWrapper<ChannelDO, ?, ?> wrapper);

    Integer hasStore(@Param("accountId") Long accountId);

}
