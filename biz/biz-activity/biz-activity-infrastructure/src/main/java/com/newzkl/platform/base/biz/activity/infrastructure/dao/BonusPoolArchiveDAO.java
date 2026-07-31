package com.newzkl.platform.base.biz.activity.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolArchiveDO;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolDataVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.HistoryBonusPoolDataVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleHistoryBonusPoolDataListVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleSumDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 往期奖金池归档数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface BonusPoolArchiveDAO extends BaseMapper<BonusPoolArchiveDO> {

    /**
     * 查询往期奖金池数据
     *
     * @param bonusPoolId 奖金池id
     * @return 奖金池数据
     */
    BonusPoolDataVO queryOldBonusPoolData(Long bonusPoolId);

    /**
     * 查询历史奖金池
     *
     * @param req 查询条件
     * @return 历史奖金池
     */
    List<HistoryBonusPoolDataVO> queryHistory(HistoryBonusPoolReq req);

    /**
     * 查询结算奖金池
     *
     * @param queryWrapper 查询条件
     * @return 结算奖金池
     */
    List<SettleHistoryBonusPoolDataListVO> querySettleHistory(@Param("ew") QueryWrapper<SettleHistoryBonusPoolDataListVO> queryWrapper);

    void updateStateBySettleId(@Param("settlementId") String settlementId, @Param("state") String state);

    SettleSumDataVO queryHistoryBySettleId(@Param("settlementId") String settlementId);

    Long selectBonusPoolArchiveByCondition(@Param("ew") QueryWrapper<BonusPoolArchiveDO> queryWrapper);


    List<BonusPoolArchiveDO> selectBonusPoolArchiveList(@Param("ew") QueryWrapper<BonusPoolArchiveDO> queryWrapper);

    int updateDividend(BonusPoolArchiveDO bonusPoolArchive);
}
