package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.auth.infrastructure.dao.po.AuthMenuDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMenuDAO extends BaseMapper<AuthMenuDO> {

}
