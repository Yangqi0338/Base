package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordItemPageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 结算记录明细表
 * @author fang
 */
@Mapper
public interface SettleRecordItemDAO extends BaseMapper<SettleRecordItemDO> {

    /**
     * 根据查询条件统计记录数量
     */
    Integer countByQuery(@Param("query") SettleRecordItemPageReq query);
}