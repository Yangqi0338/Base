package com.newzkl.platform.base.biz.activity.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolPartakeDO;
import com.newzkl.platform.base.biz.activity.model.event.req.BonusPoolPartakeReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleDetailDataListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 奖金池参与记录数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface BonusPoolPartakeDAO extends BaseMapper<BonusPoolPartakeDO> {

    /**
     * 添加奖金池参与记录
     *
     * @param req 参与记录
     */
    void addBonusPoolPartake(BonusPoolPartakeReq req);

    List<SettleDetailDataListVO> selectListByQueryWrapper(@Param("ew") QueryWrapper<BonusPoolPartakeDO> queryWrapper);


    void batchInsert(@Param("list") List<BonusPoolPartakeDO> takeList);
}
