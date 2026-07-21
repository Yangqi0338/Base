package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.SelectorDO;
import com.newzkl.platform.base.biz.account.model.res.SelectorOutRes;
// TODO[cross-domain relation]: import relation.vo.TeamUserCountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 甄选师
 *
 * @author fang
 */
@Mapper
public interface SelectorDAO extends BaseMapper<SelectorDO> {

    Long selectCountByCondition(@Param("ew") QueryWrapper<SelectorDO> queryWrapper);

    List<Long> selectListByCondition(@Param("ew") QueryWrapper<SelectorDO> queryWrapper);

    List<SelectorOutRes> selectList(@Param("ew") QueryWrapper<SelectorOutRes> queryWrapper);

    // TODO[cross-domain relation]: List<TeamUserCountRes> countLevelNumber(@Param("accountId") Long accountId);

    Long inviteIdByQuery(@Param("accountId") Long accountId);

    Long selectorIdByUsername(@Param("username") String username);


}