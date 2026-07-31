package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 售后操作记录表 Mapper 接口
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Mapper
public interface RefundOperationRecordDAO extends BaseMapper<RefundOperationRecordDO> {

}