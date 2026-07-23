package com.newzkl.platform.base.biz.account.infrastructure.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthRelationsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRelationsDAO extends BaseMapper<AuthRelationsDO> {

}
