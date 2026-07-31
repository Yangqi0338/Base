package com.newzkl.platform.base.biz.activity.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.BonusPoolNowDO;
import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 本期奖金池数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface BonusPoolNowDAO extends BaseMapper<BonusPoolNowDO> {

    /**
     * 查询当前奖金池数据
     *
     * @param channelId 渠道商id
     * @return 奖金池数据
     */
    BonusPoolDataVO queryNowBonusPoolData(Long channelId);

    BonusPoolDataVO queryNewNowBonusPoolData(@Param("channelId") Long channelId, @Param("activityId") String activityId);

    /**
     * 更新当前奖金池订单奖金
     *
     * @param channelId 渠道商id
     * @param amount    更新金额
     */
    void alterNowBonusPoolOrderBonus(@Param("channelId") Long channelId, @Param("amount") Integer amount);

    /**
     * 更新当前奖金池自定义奖金
     *
     * @param channelId 渠道商id
     * @param amount    更新金额
     */
    void alterNowBonusPoolCustomBonus(@Param("channelId") Long channelId, @Param("amount") Integer amount);

    /**
     * 删除当前奖金池
     *
     * @param channelId   渠道商id
     * @param bonusPoolId 奖金池id
     */
    void delNowBonusPool(@Param("channelId") Long channelId, @Param("bonusPoolId") Long bonusPoolId);

    /**
     * 查询待结算的奖金池
     *
     * @param time 时间
     * @return 待结算渠道商id列表
     */
    List<Long> queryWaitSettleBonusPoolDataList(Long time);


}
