package com.newzkl.platform.base.biz.account.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper
public interface InitDAO {

    @Select("select * from admin_account")
    List<Map<String, Object>> selectAdminAccountAll();

}