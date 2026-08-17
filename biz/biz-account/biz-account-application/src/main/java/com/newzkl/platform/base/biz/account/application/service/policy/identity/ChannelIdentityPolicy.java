package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.*;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;
import com.newzkl.platform.base.common.ddd.facade.InitFinanceReq;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final AccountDomain accountDomain;
    private final FinanceConfigApi financeConfigApi;
    private final OpenapiDeveloperApi openapiDeveloperApi;
    private final UserQueryService accountQueryAppService;

    private final DictApi dictApi;

    private final FinancePurseApi financePurseApi;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.CHANNEL;
    }

    private TreeMap<Integer, Double> getTree(List<AmountRateDTO> itemList) {
        TreeMap<Integer, Double> treeMap = new TreeMap<Integer, Double>();
        for (AmountRateDTO amountRateDTO : itemList) {
            treeMap.put(amountRateDTO.getAmount(), amountRateDTO.getRate());
        }
        return treeMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountCustomSaveReq accountCustomSaveReq = identityAssembler.identityCustomReq2AccountCustomReq(customSaveReq);
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(accountCustomSaveReq);
        Long accountId = account.getId();
        //判断之前是否注册过该角色
        boolean isRegisterOnce = false;
        //检查角色 OPTIMIZE
        ChannelVO channelVO = channelDomain.channel(accountId);
        if (channelVO != null) {
            if (ChannelEnum.State.DESTORY.equals(channelVO.getState())) {
                isRegisterOnce = true;
            } else {
                return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
            }
        }
        //如果已有供应商角色则不能注册其他角色
        if (account.getRoleIdList().contains(RoleEnum.CompanyRole.SUPPLIER.getCodeStr())) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }
        //获取邀请人信息
        RoleEnum.CompanyRole inviteRole = null;
        Long inviteId = 0L;
        AccountVO inviteAccountVO = accountQueryAppService.accountByYqm(customSaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            }
            account.setPidList(BizUtil.getPidList(inviteAccountVO.getPidList(), inviteAccountVO.getId()));
            account.setPRoleList(BizUtil.getPRoleList(inviteAccountVO.getPRoleList(), inviteAccountVO.getRoleIdList()));

            inviteId = inviteAccountVO.getId();

            UpIdRes res = new UpIdRes();
            res.setRoleIdList(inviteAccountVO.getRoleIdList());
//            inviteRole = UpIdRes.getLastEarningUserRoleId(res);
        } //else {
        //dealerId = CommonEnum.COMPANY_DEALER_ID;
        // operatorId = CommonEnum.COMPANY_OPERATOR_ID;
        // 没有有邀请人的时候去掉默认值
        // 查询平台默认设置的平台商服务费
        String dictOpenStr = dictApi.get(DictEnum.Key.CHANNEL_SERVICE_FEE.getCode());
        List<AmountRateDTO> list = JSONUtil.parseArray(dictOpenStr).toList(AmountRateDTO.class);
        ChargeConfigChannelReq configReq = new ChargeConfigChannelReq();
        configReq.setChannelId(accountId);
        configReq.setPlatformConfig(getTree(list));
        financeConfigApi.saveChannelChargeConfig(configReq);
        //}
        //注册角色
        ChannelCustomSaveReq channelCustomSaveReq = new ChannelCustomSaveReq();
        channelCustomSaveReq.setId(accountId);
        channelCustomSaveReq.setUsername(customSaveReq.getUsername());
        channelCustomSaveReq.setName(customSaveReq.getNickname() != null ? customSaveReq.getNickname() : customSaveReq.getName());
        channelCustomSaveReq.setHeadImg(customSaveReq.getHeadImg());
//        channelCustomSaveReq.setUpDealerId(inviteRoleId);
        channelCustomSaveReq.setUpOperatorId(inviteId);
        channelCustomSaveReq.setCompanyInfo(JSONUtil.toJsonStr(customSaveReq.getCompanyInfo()));
        channelCustomSaveReq.setContactsName(customSaveReq.getContactsName());
        channelCustomSaveReq.setContactsWay(customSaveReq.getContactsWay());
        channelCustomSaveReq.setStoreName(customSaveReq.getStoreName());
        channelCustomSaveReq.setLicense(customSaveReq.getLicense());
        channelCustomSaveReq.setStorePermission(customSaveReq.getStorePermission());
        channelDomain.channelCustomSave(channelCustomSaveReq);

        // 保存account
        AccountReq accountBaseReq = new AccountReq();
        accountBaseReq.setId(accountId);
        accountBaseReq.setPid(account.getPid());
        accountBaseReq.setPidList(account.getPidList());
        accountBaseReq.setPRoleList(account.getPRoleList());
        accountDomain.accountEdit(accountBaseReq);

        //账号添加角色 (inviteAccount是在运营商体系下的,这里不能设置 TODO)
        accountService.roleAddEvent(account, RoleEnum.CompanyRole.CHANNEL, customSaveReq.getPassword());
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(customSaveReq.getUsername());
            initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.CHANNEL);
            initFinanceReq.setParentId(inviteId);
            initFinanceReq.setSubPurseType(null);
            financePurseApi.initFinance(initFinanceReq);
        }
        //初始化开发者账号
        DeveloperInitReq developerInitReq = new DeveloperInitReq();
        if (!isRegisterOnce) {
            developerInitReq.setAccountId(accountId);
            developerInitReq.setAppId(String.valueOf(SnowflakeGenerator.getSnowflakeId()));
            developerInitReq.setSecret(BizUtil.generateCode(32));
            openapiDeveloperApi.initDeveloper(developerInitReq);
        }
        //初始化默认用户
        IdentityCustomSaveReq memberInitReq = new IdentityCustomSaveReq();
        if (CommonEnum.YesOrNo.NO != customSaveReq.getCreateMember() && !isRegisterOnce) {
            TransferUtils.transfer(customSaveReq, memberInitReq);
            AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.MEMBER.getCode()).customRegister(memberInitReq);
        }

        //同步im
        return new IdentityRegisterRes(null, accountId, developerInitReq.getAppId(), developerInitReq.getSecret());
    }
    /**
     * 渠道商不支持代理注册
     *
     * <p>迁移说明: 旧实现为 {@code void proxyRegister(String)} 空方法体(静默无操作)。
     * 新签名要求返回 {@code IdentityRegisterRes}, 无法保留"空实现"语义,
     * 故显式抛出不支持异常, 避免调用方误判为注册成功。</p>
     *
     * @param proxySaveReq 代理注册参数
     * @return 不会正常返回
     * @author KC
     */
    @Override
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        throw new PlatformException(BaseErrorCode.CUSTOM, "渠道商不支持代理注册");
    }

    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 渠道商无法邀请人
        throw new PlatformException(AccountErrorCode.NO_INVITE);
    }


    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        return false;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
