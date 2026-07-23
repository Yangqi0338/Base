package com.newzkl.platform.base.biz.finance.domain.account.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseConfigRepository;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConfigChannelRepository;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.req.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigChannelVO;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.core.utils.common.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author niu
 * @description:
 * @date 2024/4/3 10:13
 */
@Service
@RequiredArgsConstructor
public class AccountPurseConfigDomainImpl implements AccountPurseConfigDomain {

    private final AccountPurseConfigRepository accountPurseConfigRepository;

    private final ConfigChannelRepository channelConfigRepository;

    @Override
    public void saveOperatorLeverConfig(Long accountId, Integer config) {
        accountPurseConfigRepository.saveOperatorLeverConfig(accountId, config);
    }

    @Override
    public Integer queryOperatorLever(Long operatorId) {
        return accountPurseConfigRepository.queryOperatorLever(operatorId);
    }

    @Override
    public void saveChannelChargeConfig(ChargeConfigChannelReq req) {
        channelConfigRepository.saveChannelConfig(buildChannelConfig(req));
    }

    @Override
    public void alterChannelNowChargeConfig(Long channelId, Integer rechargeAmount) {
        // 获取渠道商配置
        ConfigChannelVO channelConfigVO = channelConfigRepository.queryChannelConfig(channelId);
        if (channelConfigVO == null) {
            return;
        }
        TreeMap<String, Double> platformConfig = JSONUtil.toBean(channelConfigVO.getPlatformConfig(), TreeMap.class);
        HashMap<Integer, Double> operatorConfig = JSONUtil.toBean(channelConfigVO.getOperatorConfig(), HashMap.class);
        assert platformConfig != null;
        assert operatorConfig != null;
        // treeMap key自然排序，降序后遍历，取首次对比匹配的值
        for (Map.Entry<String, Double> entry : platformConfig.descendingMap().entrySet()) {
            Integer key = NumberUtil.parseInt(entry.getKey());
            if (key >= rechargeAmount) {
                channelConfigVO.setPlatformNowValue(entry.getValue());
                channelConfigVO.setOperatorNowValue(MapUtil.get(operatorConfig, key, Double.class, 0.0));
                channelConfigVO.setAlterTime(LocalDateTime.now());
                // 更新服务费配置
                channelConfigRepository.alterChannelConfigNowValue(channelConfigVO);
                break;
            }
        }
    }

    @Override
    public ConfigChannelVO queryChannelConfig(Long channelId) {
        return channelConfigRepository.queryChannelConfig(channelId);
    }

    @Override
    public List<ConfigChannelVO> queryChannelConfigs(List<Long> channelIds) {
        return channelConfigRepository.queryChannelConfigs(channelIds);
    }

    @Override
    public List<BatchQueryConfigChannelRes> batchQueryChannelConfig(BatchQueryConfigChannelQuery req) {
        return channelConfigRepository.batchQueryChannelConfig(req);
    }

    @Override
    public ConfigSupplierVO querySupplierConfig() {
        return accountPurseConfigRepository.querySupplierConfig();
    }

    private ConfigChannelVO buildChannelConfig(ChargeConfigChannelReq req) {
        ConfigChannelVO channelConfigVO = new ConfigChannelVO();
        channelConfigVO.setChannelId(req.getChannelId());
        channelConfigVO.setPlatformConfig(JsonUtils.toJson(req.getPlatformConfig()));
        channelConfigVO.setOperatorConfig(JsonUtils.toJson(req.getPlatformConfig()));
        if (CollectionUtil.isNotEmpty(req.getPlatformConfig())) {
            channelConfigVO.setPlatformNowValue(req.getPlatformConfig().firstEntry().getValue());
        }
        if (CollectionUtil.isNotEmpty(req.getPlatformConfig())) {
            channelConfigVO.setOperatorNowValue(req.getPlatformConfig().firstEntry().getValue());
        }
        channelConfigVO.setAlterTime(LocalDateTime.now());
        return channelConfigVO;
    }
}
