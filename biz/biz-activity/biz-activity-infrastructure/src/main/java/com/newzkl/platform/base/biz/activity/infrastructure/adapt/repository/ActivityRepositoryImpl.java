package com.newzkl.platform.base.biz.activity.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.newzkl.platform.base.biz.activity.domain.adapt.repository.ActivityRepository;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.ActivityDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.ActivityOtherConfigDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.StrategyDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.dao.StrategyDetailDAO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.ActivityDO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.ActivityOtherConfigDO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.StrategyDO;
import com.newzkl.platform.base.biz.activity.infrastructure.entity.StrategyDetailDO;
import com.newzkl.platform.base.biz.activity.infrastructure.utils.CustomerIdGenerator;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityConfigSaveReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.*;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 活动数据仓库实现
 *
 * @author niu
 */
@Repository
public class ActivityRepositoryImpl implements ActivityRepository {

    private static final String OTHER_CONFIG = "otherConfig:";

    private final ActivityDAO activityDao;

    private final StrategyDAO strategyDAO;

    private final StrategyDetailDAO strategyDetailDAO;

    private final ActivityOtherConfigDAO otherConfigDAO;

    final Long CHANNEL_ID = 1L;

    @Autowired
    public ActivityRepositoryImpl(ActivityDAO activityDao, StrategyDAO strategyDAO, StrategyDetailDAO strategyDetailDAO, ActivityOtherConfigDAO otherConfigDAO) {
        this.activityDao = activityDao;
        this.strategyDAO = strategyDAO;
        this.strategyDetailDAO = strategyDetailDAO;
        this.otherConfigDAO = otherConfigDAO;
    }

    @Override
    public ActivityVO queryActivity(ActivityQueryReq req) {
        return activityDao.queryActivity(req);
    }

    @Override
    public ActivityVO queryNewActivity(ActivateActivityReq req) {
        return activityDao.queryNewActivity(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterActivityState(ActivityQueryReq req, Integer state) {
        activityDao.alterActivityState(req, state);
    }

    @Override
    public void alterNewActivityState(ActivateActivityReq req, String state) {
        activityDao.alterNewActivityState(req.getActivityId(), state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveActivity(ActivityVO activityVO) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityId(activityVO.getActivityId());
        activity.setChannelId(SecurityUtils.getAccountId());
        activity.setActivityName(activityVO.getActivityName());
        activity.setActivityDesc(activityVO.getActivityDesc());
        activity.setClientType(activityVO.getClientType());
        activity.setStrategyId(activityVO.getStrategyId());
        activity.setConditionType(activityVO.getConditionType());
        activity.setConditionValue(activityVO.getConditionValue());
        activity.setRepeatType(activityVO.getRepeatType());
        activity.setRepeatValue(activityVO.getRepeatValue());
        activity.setState(activityVO.getState());
        activity.setOtherConfig(activityVO.getOtherConfigId());
        Date date = new Date();
        activity.setUpdateTime(date);
        if (activityVO.getId() == null) {
            activity.setId(SnowflakeIdAble.getSnowflakeId());
            activity.setCreateTime(date);
            activityDao.insert(activity);
        } else {
            activity.setId(activityVO.getId());
            activityDao.updateById(activity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewActivity(ActivityVO activityVO) {
        ActivityDO activity = new ActivityDO();
        BeanUtils.copyProperties(activityVO, activity);
        Date date = new Date();
        activity.setUpdateTime(date);
        if (activityVO.getId() == null) {
            String prefix = "FHC";
            activity.setActivityId(CustomerIdGenerator.generateActivityId(prefix));
            activity.setId(SnowflakeIdAble.getSnowflakeId());
            activity.setCreateTime(date);
            activity.setChannelId(1L);
            activityDao.insert(activity);
        } else {
            activity.setId(activityVO.getId());
            activityDao.updateById(activity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveActivityStrategy(ActivityConfigSaveReq req) {
        if (req == null) {
            return null;
        }
        StrategyDO strategy = new StrategyDO();
        BeanUtils.copyProperties(req, strategy);
        Date date = new Date();
        strategy.setUpdateTime(date);
        if (req.getStrategyId() == null) {
            strategy.setStrategyId(SnowflakeIdAble.getSnowflakeId());
            strategy.setCreateTime(date);
            strategyDAO.insert(strategy);
        } else {
            strategy.setStrategyId(req.getStrategyId());
            strategyDAO.updateById(strategy);
        }
        return strategy.getStrategyId();
    }

    @Override
    public Long addActivityStrategy(StrategyVO strategyVO) {
        if (strategyVO == null) {
            return null;
        }
        StrategyDO strategy = new StrategyDO();
        strategy.setStrategyDesc(strategyVO.getStrategyDesc());
        strategy.setStrategyMode(strategyVO.getStrategyMode());
        strategy.setGrantType(strategyVO.getGrantType());
        strategy.setGrantDate(strategyVO.getGrantDate());
        strategy.setExtInfo(strategyVO.getExtInfo());
        Date date = new Date();
        strategy.setUpdateTime(date);
        if (strategyVO.getStrategyId() == null) {
            strategy.setStrategyId(SnowflakeIdAble.getSnowflakeId());
            strategy.setCreateTime(date);
            strategyDAO.insert(strategy);
        } else {
            strategy.setStrategyId(strategyVO.getStrategyId());
            strategyDAO.updateById(strategy);
        }
        return strategy.getStrategyId();
    }

    @Override
    public void saveStrategyDetails(List<StrategyDetailVO> strategyDetailList) {
        if (CollectionUtil.isEmpty(strategyDetailList)) {
            return;
        }
        List<StrategyDetailDO> details = addActivityStrategyDetail(strategyDetailList);
        details.forEach(x -> {
            if (x.getId() == null) {
                x.setId(SnowflakeIdAble.getSnowflakeId());
                strategyDetailDAO.insert(x);
            } else {
                strategyDetailDAO.updateById(x);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addActivityOtherConfig(ActivityOtherConfigVO activityOtherConfig) {
        ActivityOtherConfigDO config = new ActivityOtherConfigDO();
        config.setId(SnowflakeIdAble.getSnowflakeId());
        config.setCreateTime(new Date());
        config.setShowConfig(activityOtherConfig.getShowConfig());
        config.setConfigDetails(activityOtherConfig.getConfigDetails());
        config.setChannelId(SecurityUtils.getAccountId());
        otherConfigDAO.insert(config);
        return config.getId();
    }

    public List<StrategyDetailDO> addActivityStrategyDetail(List<StrategyDetailVO> strategyDetailList) {
        List<StrategyDetailDO> details = new ArrayList<>();
        for (StrategyDetailVO strategyDetailVO : strategyDetailList) {
            StrategyDetailDO strategyDetail = new StrategyDetailDO();
            strategyDetail.setId(strategyDetailVO.getDetailId());
            strategyDetail.setStrategyId(strategyDetailVO.getStrategyId());
            strategyDetail.setAwardId(strategyDetailVO.getAwardId());
            strategyDetail.setAwardName(strategyDetailVO.getAwardName());
            strategyDetail.setAwardCount(strategyDetailVO.getAwardCount());
            strategyDetail.setAwardSurplusCount(strategyDetailVO.getAwardSurplusCount());
            strategyDetail.setStrategyContent(strategyDetailVO.getStrategyContent());
            details.add(strategyDetail);
        }
        return details;
    }

    @Override
    public ActivityOtherConfigVO queryBonusPoolOtherConfig(Long otherConfigId, Long channelId) {
        ActivityOtherConfigVO config = RedisUtil.get(OTHER_CONFIG + otherConfigId);
        if (config != null) {
            return config;
        }
        ActivityOtherConfigVO configVO = otherConfigDAO.otherConfig(otherConfigId, channelId);
        RedisUtil.set(OTHER_CONFIG + otherConfigId, configVO);
        return configVO;
    }

    @Override
    public StrategyVO queryStrategy(Long strategyId, Long channelId) {
        StrategyDO strategy = strategyDAO.selectById(strategyId);
        StrategyVO strategyVO = new StrategyVO();
        strategyVO.setStrategyId(strategy.getStrategyId());
        strategyVO.setStrategyDesc(strategy.getStrategyDesc());
        strategyVO.setStrategyMode(strategy.getStrategyMode());
        strategyVO.setGrantType(strategy.getGrantType());
        strategyVO.setGrantDate(strategy.getGrantDate());
        strategyVO.setExtInfo(strategy.getExtInfo());
        return strategyVO;
    }

    @Override
    public ActivityOtherConfigVO queryActivityOtherConfig(Long otherConfigId, Long channelId) {
        ActivityOtherConfigDO config = otherConfigDAO.selectById(otherConfigId);
        ActivityOtherConfigVO activityOtherConfigVO = new ActivityOtherConfigVO();
        activityOtherConfigVO.setShowConfig(config.getShowConfig());
        activityOtherConfigVO.setConfigDetails(config.getConfigDetails());
        return activityOtherConfigVO;
    }

    @Override
    public List<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req) {
        List<ActivityQueryPageVO> activityQueryPageVOS = activityDao.pageList(req);
        if (CollectionUtil.isNotEmpty(activityQueryPageVOS)) {
            return activityQueryPageVOS;
        }
        return Collections.emptyList();
    }

    @Override
    public ActivityQueryDetailVO activityDetail(Long id) {
        return activityDao.activityDetail(id);
    }

    @Override
    public Long insertStrategy(ActivityConfigSaveReq req) {
        if (req == null) {
            return null;
        }
        StrategyDO strategy = new StrategyDO();
        strategy.setStrategyId(SnowflakeIdAble.getSnowflakeId());
        strategy.setStrategyMode(req.getStrategyMode());
        strategy.setStrategyDesc(req.getStrategyDesc());
        strategy.setOrderAmountsRate(req.getOrderAmountsRate());
        strategy.setDividendCycle(req.getDividendCycle());
        strategy.setSettlementStrategy(req.getSettlementStrategy());
        strategy.setDividendMethod(req.getDividendMethod());
        strategy.setDividendRole(req.getDividendRole());
        strategy.setDividendUser(req.getDividendUser());
        Date date = new Date();
        strategy.setCreateTime(date);
        strategy.setUpdateTime(date);
        strategyDAO.insert(strategy);
        return strategy.getStrategyId();
    }

    @Override
    public void insertActivity(ActivityVO vo) {
        ActivityDO activity = new ActivityDO();
        Date date = new Date();
        activity.setUpdateTime(date);
        String prefix = "FHC";
        activity.setStrategyId(vo.getStrategyId());
        activity.setActivityName(vo.getActivityName());
        activity.setActivityDesc(vo.getActivityDesc());
        activity.setState(vo.getState());
        activity.setChannelId(CHANNEL_ID);
        activity.setActivityId(CustomerIdGenerator.generateActivityId(prefix));
        activity.setId(SnowflakeIdAble.getSnowflakeId());
        activity.setCreateTime(date);
        activityDao.insert(activity);
    }

    @Override
    public Long updateStrategy(ActivityConfigSaveReq req) {
        if (req == null) {
            return null;
        }
        StrategyDO strategy = new StrategyDO();
        Date date = new Date();
        strategy.setUpdateTime(date);
        strategy.setStrategyId(req.getStrategyId());
        strategy.setStrategyMode(req.getStrategyMode());
        strategy.setStrategyDesc(req.getStrategyDesc());
        strategy.setOrderAmountsRate(req.getOrderAmountsRate());
        strategy.setDividendCycle(req.getDividendCycle());
        strategy.setSettlementStrategy(req.getSettlementStrategy());
        strategy.setDividendMethod(req.getDividendMethod());
        strategy.setDividendRole(req.getDividendRole());
        strategy.setDividendUser(req.getDividendUser());
        strategyDAO.updateById(strategy);
        return strategy.getStrategyId();
    }

    @Override
    public void updateActivity(ActivityVO vo) {
        ActivityDO activity = new ActivityDO();
        activity.setActivityName(vo.getActivityName());
        activity.setActivityDesc(vo.getActivityDesc());
        activity.setState(vo.getState());
        activity.setId(vo.getId());
        Date date = new Date();
        activity.setUpdateTime(date);
        activityDao.updateById(activity);
    }

    @Override
    public void deleteById(Long id) {
        activityDao.deleteById(id);
    }
}
