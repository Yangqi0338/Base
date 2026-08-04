package com.newzkl.platform.base.biz.finance.application.provider;

import cn.hutool.core.bean.BeanUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConfigChannelRepository;
import com.newzkl.platform.base.biz.finance.facade.AccountConfigFacade;
import com.newzkl.platform.base.biz.finance.facade.model.ChannelConfigRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigChannelVO;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @author niu
 * @description:
 * @date 2024/1/22 15:12
 */
@DubboService
@Component
@RequiredArgsConstructor
public class AccountConfigProvider implements AccountConfigFacade {

    private final ConfigChannelRepository configChannelRepository;

    @Override
    public ChannelConfigRes queryChannelConfig(Long channelId) {
        ConfigChannelVO channelConfigVO = configChannelRepository.queryChannelConfig(channelId);
        return BeanUtil.copyProperties(channelConfigVO, ChannelConfigRes.class);
    }
}
