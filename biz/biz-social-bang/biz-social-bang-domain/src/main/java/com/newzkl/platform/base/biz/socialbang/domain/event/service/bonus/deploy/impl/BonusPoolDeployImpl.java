package com.newzkl.platform.base.biz.socialbang.domain.event.service.bonus.deploy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.socialbang.model.event.aggregates.ActivityConfigRich;
import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.BonusPoolRepository;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.socialbang.model.event.req.*;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.*;
import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.ActivityRepository;
import com.newzkl.platform.base.biz.socialbang.domain.event.service.bonus.deploy.BonusPoolDeploy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import jakarta.annotation.Resource;


/**
 * 奖金池配置服务实现
 * @Author: niu
 * @Date: 2024/1/2 14:57
 */
@Service
public class BonusPoolDeployImpl implements BonusPoolDeploy {

    private final ActivityRepository activityRepository;

    @Resource
    private BonusPoolRepository bonusPoolRepository;

    @Autowired
    public BonusPoolDeployImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    @Override
    public ActivityVO queryActivity(ActivityQueryReq req) {
        return activityRepository.queryActivity(req);
    }

    @Override
    public ActivityVO queryNewActivity(ActivateActivityReq req) {

        return activityRepository.queryNewActivity(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBonusPoolConfig(ActivityConfigReq req) {
        Integer activityId = req.getActivityId();
        ActivityConfigRich configRich = req.getConfigRich();

        // 保存活动策略配置
        StrategyVO strategy = configRich.getStrategy();
        Long strategyId = activityRepository.addActivityStrategy(strategy);
        activityRepository.saveStrategyDetails(strategy == null ? null : strategy.getStrategyDetails());

        ActivityOtherConfigVO activityOtherConfig = configRich.getActivityOtherConfig();
        Long otherConfigId = activityRepository.addActivityOtherConfig(activityOtherConfig);
        // 保存活动配置
        ActivityVO activity = configRich.getActivity();
      //  activity.setActivityId(activityId);
        activity.setStrategyId(strategyId);
        activity.setOtherConfigId(otherConfigId);
        activityRepository.saveActivity(activity);
    }


    /**
     * 新增和保存奖金池配置,根据activityId 判断是新增还是修改
     * @param req  AVERAGE
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewBonusPoolConfig(ActivityConfigSaveReq req){
        Long strategyId = activityRepository.saveActivityStrategy(req);
        ActivityVO vo = new ActivityVO();
        BeanUtils.copyProperties(req,vo);
        vo.setStrategyId(strategyId);
        activityRepository.saveNewActivity(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterActivityState(ActivityQueryReq req, Enum<ActivityEnum.ActivityState> activityState) {
        Integer state = ((ActivityEnum.ActivityState) activityState).getState();
        activityRepository.alterActivityState(req,state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterNewActivityState(ActivateActivityReq req, ActivityEnum.ExecuteState activityState) {
        String code = activityState.getCode();
        activityRepository.alterNewActivityState(req,code);
    }

    @Override
    public ActivityOtherConfigVO queryBonusPoolOtherConfig(Long otherConfig, Long channelId) {
        return activityRepository.queryBonusPoolOtherConfig(otherConfig, channelId);
    }

    @Override
    public StrategyVO queryStrategy(Long strategyId, Long channelId) {
        return activityRepository.queryStrategy(strategyId, channelId);
    }

    @Override
    public ActivityOtherConfigVO queryActivityOtherConfig(Long otherConfigId, Long channelId) {
        return activityRepository.queryActivityOtherConfig(otherConfigId, channelId);
    }

    @Override
    public Page<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req) {
        // TODO[PageHelper->MP]: 原 new PageInfo<>(list) 依赖 PageHelper 拦截器回填 total/pages,
        //  MP 语义不同(分页应在 repository 层用 Page 参数由 MP 分页插件回填). 此处先构造 Page 保证编译,
        //  total 暂取 list.size(), 待 T4(RepositoryImpl)/分页逻辑落地后由 KC 复核修正.
        List<ActivityQueryPageVO> list = activityRepository.listActivity(req);
        Page<ActivityQueryPageVO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(list == null ? 0 : list.size());
        return page;
    }

    @Override
    public ActivityQueryDetailVO activityDetail(Long id) {


        return  activityRepository.activityDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addActivityConfig(ActivityConfigSaveReq req) {
        Long strategyId = activityRepository.insertStrategy(req);
        ActivityVO vo = new ActivityVO();
        vo.setActivityId(req.getActivityId());
        vo.setActivityName(req.getActivityName());
        vo.setActivityDesc(req.getActivityDesc());
        vo.setState(req.getState());
       // BeanUtils.copyProperties(req,vo);
        vo.setStrategyId(strategyId);
        activityRepository.insertActivity(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActivityConfig(ActivityConfigSaveReq req) {
        Long strategyId = activityRepository.updateStrategy(req);
        ActivityVO vo = new ActivityVO();
        vo.setActivityId(req.getActivityId());
        vo.setActivityName(req.getActivityName());
        vo.setActivityDesc(req.getActivityDesc());
        vo.setId(req.getId());
        vo.setState(req.getState());
        //BeanUtils.copyProperties(req,vo);
        vo.setStrategyId(strategyId);
        activityRepository.updateActivity(vo);
    }

    @Override
    public Boolean checkCancelActivity(ActivateActivityReq req) {
      return   bonusPoolRepository.checkCancelActivity(req);
    }


}
