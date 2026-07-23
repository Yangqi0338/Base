package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkl.scm.finance.model.account.req.ChargeConfigChannelReq;
import com.zkl.scm.finance.model.earnings.res.AmountRateDTO;
import com.zkl.scm.finance.model.purse.req.InitFinanceReq;
import com.zkl.scm.finance.rpc.facade.account.IAccountPurseConfigFacade;
import com.zkl.scm.finance.rpc.facade.purse.IPurseFacade;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.account.model.enums.DictEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.zkl.scm.openapi.facade.IDeveloperFacade;
import com.zkl.scm.openapi.model.DeveloperInitReq;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.zkl.scm.user.model.relation.req.AccountLevelUpReq;
import com.newzkl.platform.base.biz.account.facade.IDictFacade;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final OperatorClientDomain operatorDomain;
    @DubboReference
    private IAccountPurseConfigFacade accountPurseConfigFacade;
    @DubboReference
    private IDeveloperFacade developerFacade;
    private final UserQueryService accountQueryAppService;

    @DubboReference
    private IDictFacade dictFacade;

    @DubboReference
    private IPurseFacade purseFacade;

    public static void main(String[] args) throws JsonProcessingException {
        String operatorConfig = "";
        // ServiceFeeConfigVO operatorConfigObject = JSONObject.parseObject(operatorConfig, ServiceFeeConfigVO.class);

        ObjectMapper objectMapper = new ObjectMapper();

        List<AmountRateDTO> list = objectMapper.readValue(
                operatorConfig,
                new com.fasterxml.jackson.core.type.TypeReference<List<AmountRateDTO>>() {
                }
        );


        //channel.serviceFeeConfigEdit(serviceFeeConfigVO);
    }

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
            throw new ScmException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }
        //获取邀请人信息
        RoleEnum.CompanyRole inviteRole = null;
        Long inviteId = 0L;
        AccountVO inviteAccountVO = accountQueryAppService.accountByYqm(customSaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new ScmException(AccountErrorCode.PARAM_YQM);
            } else {
//                log.info("邀请人身份ID：{}", inviteAccountVO.getSubRoleIdList());
                if (!(inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.DEALER.getCodeStr()) ||
                        inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.OPERATOR.getCodeStr()))) {
                    throw new ScmException(AccountErrorCode.NO_INVITE);
                }
            }
            // 交易师和运营商可以各自招募
            account.setPidList(ScmUtil.getPidList(inviteAccountVO.getPidList(), inviteAccountVO.getId()));
            account.setPRoleList(ScmUtil.getPRoleList(inviteAccountVO.getPRoleList(), inviteAccountVO.getRoleIdList()));

            inviteId = inviteAccountVO.getId();

            UpIdRes res = new UpIdRes();
            res.setRoleIdList(inviteAccountVO.getRoleIdList());
            inviteRole = UpIdRes.getLastEarningUserRoleId(res);
        } //else {
        //dealerId = CommonEnum.COMPANY_DEALER_ID;
        // operatorId = CommonEnum.COMPANY_OPERATOR_ID;
        // 没有有邀请人的时候去掉默认值
        // 查询平台默认设置的平台商服务费
        String dictOpenStr = dictFacade.get(DictEnum.Key.CHANNEL_SERVICE_FEE.getCode());
        List<AmountRateDTO> list = JSONUtil.parseArray(dictOpenStr).toList(AmountRateDTO.class);
        ChargeConfigChannelReq configReq = new ChargeConfigChannelReq();
        configReq.setChannelId(accountId);
        configReq.setPlatformConfig(getTree(list));
        accountPurseConfigFacade.saveChannelChargeConfig(configReq);
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
        channelCustomSaveReq.setBodyType(customSaveReq.getBodyType());
        channelCustomSaveReq.setContactsWay(customSaveReq.getContactsWay());
        channelCustomSaveReq.setStoreName(customSaveReq.getStoreName());
        channelCustomSaveReq.setLicense(customSaveReq.getLicense());
        channelCustomSaveReq.setChannelType(customSaveReq.getChannelType());
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
        //统计变更
        if (!isRegisterOnce) {
            // 修改直属上级的邀请渠道商人数
            if (RoleEnum.CompanyRole.DEALER == inviteRole) {
                operatorDomain.dealerEdit(Collections.singletonList(new EditColumnDTO("invite_channel_number", 1)), inviteId);
            } else if (RoleEnum.CompanyRole.OPERATOR == inviteRole) {
                operatorDomain.operatorEdit(Collections.singletonList(new EditColumnDTO("invite_channel_number", 1)), inviteId);
            }
        }
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(customSaveReq.getUsername());
            initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.CHANNEL);
            initFinanceReq.setParentId(inviteId);
            initFinanceReq.setSubPurseType(null);
            purseFacade.initFinance(initFinanceReq);
        }
        //初始化开发者账号
        DeveloperInitReq developerInitReq = new DeveloperInitReq();
        if (!isRegisterOnce) {
            developerInitReq.setAccountId(accountId);
            developerInitReq.setAppId(String.valueOf(SnowflakeIdAble.getSnowflakeId()));
            developerInitReq.setSecret(ScmUtil.generateCode(32));
            developerFacade.initDeveloper(developerInitReq);
        }
        //初始化默认用户
        IdentitySaveReq memberInitReq = new IdentitySaveReq();
        if (CommonEnum.YesOrNo.NO != customSaveReq.getCreateMember() && !isRegisterOnce) {
            TransferUtils.transfer(customSaveReq, memberInitReq);
            AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.MEMBER.getCode()).customRegister(memberInitReq);
        }

        //注册通知
        channelRepository.registerEvent(account, customSaveReq.getPassword());

        //同步im
        return new IdentityRegisterRes(null, accountId, developerInitReq.getAppId(), developerInitReq.getSecret());
    }
    @Override
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {

    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }

    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 渠道商无法邀请人
        throw new ScmException(AccountErrorCode.NO_INVITE);
    }


    @Override
    public Pair<RoleEnum.CompanyRole, Double> levelUpCheck(AccountLevelUpReq levelUpReq) {
        return null;
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
