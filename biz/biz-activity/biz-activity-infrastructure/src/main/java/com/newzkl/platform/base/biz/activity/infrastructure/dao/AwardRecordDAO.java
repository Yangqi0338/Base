package com.newzkl.platform.base.biz.activity.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.AwardRecordDO;
import com.newzkl.platform.base.biz.activity.model.award.req.RecordAwardOrderReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 奖品发放记录数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface AwardRecordDAO extends BaseMapper<AwardRecordDO> {

    /**
     * 保存奖品单
     *
     * @param req 奖品单列表
     */
    void recordAwardOrder(@Param("list") List<RecordAwardOrderReq> req);
}
