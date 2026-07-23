package com.newzkl.platform.base.biz.account.infrastructure.auth.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthFunctionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthFunctionDAO extends BaseMapper<AuthFunctionDO> {

    /**
     * 构建LimitFunctionDO的Lambda查询条件
     * 支持所有字段的动态查询
     *
     * @param limitFunction 功能点查询对象
     * @return BaseLambdaQueryWrapper查询包装器
     */
    default BaseLambdaQueryWrapper<AuthFunctionDO> buildQueryWrapper(AuthFunctionDO limitFunction) {
        if (limitFunction == null) {
            return new BaseLambdaQueryWrapper<>();
        }

        BaseLambdaQueryWrapper<AuthFunctionDO> wrapper = new BaseLambdaQueryWrapper<>();
        wrapper.notNullEq(AuthFunctionDO::getId, limitFunction.getId());
        wrapper.notEmptyLike(AuthFunctionDO::getName, limitFunction.getName());
        wrapper.notEmptyLike(AuthFunctionDO::getUrlPath, limitFunction.getUrlPath());
        wrapper.notEmptyEq(AuthFunctionDO::getUrlMethod, limitFunction.getUrlMethod());
        wrapper.notEmptyEq(AuthFunctionDO::getType, limitFunction.getType());
        return wrapper;
    }
}
