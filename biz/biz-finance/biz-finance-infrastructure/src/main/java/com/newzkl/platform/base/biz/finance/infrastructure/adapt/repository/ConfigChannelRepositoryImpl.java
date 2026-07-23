package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConfigChannelRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.ConfigChannelDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.ConfigChannelDO;
import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigChannelVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/1/22 15:17
 */
@RequiredArgsConstructor
@Repository
public class ConfigChannelRepositoryImpl extends RepositorySupport implements ConfigChannelRepository {

    private final ConfigChannelDAO configChannelDAO;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveChannelConfig(ConfigChannelVO channelConfig) {
        ConfigChannelDO configChannel = new ConfigChannelDO();
        configChannel.setChannelId(channelConfig.getChannelId());
        configChannel.setPlatformConfig(channelConfig.getPlatformConfig());
        configChannel.setOperatorConfig(channelConfig.getOperatorConfig());
        configChannel.setPlatformNowValue(channelConfig.getPlatformNowValue());
        configChannel.setOperatorNowValue(channelConfig.getOperatorNowValue());

        LambdaQueryWrapper<ConfigChannelDO> queryWrapper = configChannelDAO.getLw(channelConfig.getChannelId());
        if (configChannelDAO.exists(queryWrapper)) {
            configChannelDAO.insert(configChannel);
        } else {
            configChannelDAO.update(configChannel, queryWrapper);
        }
    }

    @Override
    public ConfigChannelVO queryChannelConfig(Long channelId) {
        LambdaQueryWrapper<ConfigChannelDO> queryWrapper = configChannelDAO.getLw(channelId);
        ConfigChannelDO configChannel = configChannelDAO.selectOne(queryWrapper);
        return TransferUtils.transfer(configChannel, ConfigChannelVO::new);
    }

    @Override
    public List<ConfigChannelVO> queryChannelConfigs(List<Long> channelIds) {
        LambdaQueryWrapper<ConfigChannelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConfigChannelDO::getChannelId, channelIds);
        List<ConfigChannelDO> configChannels = configChannelDAO.selectList(queryWrapper);
        return TransferUtils.transfers(configChannels, ConfigChannelVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterChannelConfigNowValue(ConfigChannelVO channelConfig) {
        configChannelDAO.update(new LambdaUpdateWrapper<ConfigChannelDO>()
                .set(ConfigChannelDO::getPlatformNowValue, channelConfig.getPlatformNowValue())
                .set(ConfigChannelDO::getOperatorNowValue, channelConfig.getOperatorNowValue())
                .set(ConfigChannelDO::getCreateTime, LocalDateTime.now())
                .eq(ConfigChannelDO::getChannelId, channelConfig.getChannelId()));
    }

    @Override
    public List<BatchQueryConfigChannelRes> batchQueryChannelConfig(BatchQueryConfigChannelQuery req) {
        return configChannelDAO.batchQueryChannelConfig(req.getAccountId());
    }
}
