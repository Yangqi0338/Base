package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.DealerDO;
import com.newzkl.platform.base.biz.account.model.res.DealerOutRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 市场交易师
 *
 * @author fang
 */
@Mapper
public interface DealerDAO extends BaseMapper<DealerDO> {

    Long selectCountByCondition(@Param("ew") QueryWrapper<DealerDO> queryWrapper);

    List<Long> selectListByCondition(@Param("ew") QueryWrapper<DealerDO> queryWrapper);

    List<DealerOutRes> selectList(@Param("ew") QueryWrapper<DealerOutRes> queryWrapper);
}