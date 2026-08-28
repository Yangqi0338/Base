package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.*;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.TreeMap;

/**
 * @author muc_fang
 * @Description: 运营商角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelIdentityPolicy extends AbsIdentityPolicy {

    private final ChannelClientDomain channelDomain;
    private final ChannelRepository channelRepository;
    private final DictApi dictApi;
    private final FinanceConfigApi financeConfigApi;
    private final PurseApi purseApi;
    private final DeveloperApi developerApi;

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.CHANNEL;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        Long accountId = customSaveReq.getId();

        TreeMap<Integer, Double> platformConfig = dictApi.getChannelServiceFee();
        ChargeConfigChannelReq configReq = new ChargeConfigChannelReq();
        configReq.setChannelId(accountId);
        configReq.setPlatformConfig(platformConfig);
        financeConfigApi.saveChannelChargeConfig(configReq);

        ChannelCustomSaveReq channelCustomSaveReq = new ChannelCustomSaveReq();
        channelCustomSaveReq.setId(accountId);
        channelCustomSaveReq.setName(customSaveReq.getNickname() != null ? customSaveReq.getNickname() : customSaveReq.getName());
        channelCustomSaveReq.setCompanyInfo(JSONUtil.toJsonStr(customSaveReq.getCompanyInfo()));
        channelCustomSaveReq.setContactsName(customSaveReq.getContactsName());
        channelCustomSaveReq.setContactsWay(customSaveReq.getContactsWay());
        channelCustomSaveReq.setStoreName(customSaveReq.getStoreName());
        channelCustomSaveReq.setLicense(customSaveReq.getLicense());
        channelCustomSaveReq.setStorePermission(customSaveReq.getStorePermission());
        channelDomain.channelCustomSave(channelCustomSaveReq);

        IdentityRegisterRes registerRes = new IdentityRegisterRes(null, accountId);
        // 开发者账号初始化仅首次注册执行, 复用注销账号覆盖注册时跳过
        if (customSaveReq.isRegisterOnce()) {
            String appId = String.valueOf(SnowflakeGenerator.getSnowflakeId());
            String secret = BizUtil.generateCode(32);
            DeveloperInitReq developerInitReq = new DeveloperInitReq();
            developerInitReq.setAccountId(accountId);
            developerInitReq.setAppId(appId);
            developerInitReq.setSecret(secret);
            developerApi.initDeveloper(developerInitReq);
            registerRes.setAppId(appId);
            registerRes.setSecret(secret);
        }
        return registerRes;
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        // 移除渠道商身份: 删 channel 行
        channelRepository.channelDelete(Collections.singletonList(accountVO.getId()));
        return true;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
