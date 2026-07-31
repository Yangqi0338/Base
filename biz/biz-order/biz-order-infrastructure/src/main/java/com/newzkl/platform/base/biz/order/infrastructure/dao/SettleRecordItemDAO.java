package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 结算记录明细表
* @author fang
*/
@Mapper
public interface SettleRecordItemDAO extends BaseMapper<SettleRecordItemDO> {

}