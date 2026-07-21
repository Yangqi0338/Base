package com.newzkl.platform.base.biz.account.application.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.finance.model.earnings.req.IncomeQuery;
import com.zkl.scm.finance.rpc.facade.earnings.EarningFacade;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountFinanceVO;
import com.newzkl.platform.base.biz.account.model.res.AppHomePageDataVO;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.CustomSaveBatchReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;
    private final UserClientDomain userClientDomain;
    private final AccountAssembler accountAssembler;

    @DubboReference
    private EarningFacade earningFacade;


    @Override
    public AppHomePageDataVO appHomePageData(Long accountId) {
        CommonEnum.Client client = SecurityUtils.getClient();
        IncomeQuery incomeReq = new IncomeQuery();
        incomeReq.setAccountId(accountId);

        List<RoleEnum.CompanyRole> roleList = RoleEnumUtil.findClientRoleList(client).collect(Collectors.toList());

        incomeReq.setRoleList(roleList);
        incomeReq.setConsumeType(EarningsEnum.ConsumeType.PICK_PACK);

        AppHomePageDataVO resVO = new AppHomePageDataVO();
        // 查收益
        Map<EarningsEnum.ConsumeType, Integer> incomeMap = earningFacade.queryIncome(incomeReq);
        resVO.setPackIncome(MapUtil.get(incomeMap, EarningsEnum.ConsumeType.PICK_PACK, Integer.class, 0));
        // 查询直推用户
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setInviteAccountId(accountId);
        resVO.setDirectReferralUserCount(accountRepository.selectCount(accountQuery));
        // 查询直推供应商
        resVO.setSupplierCount(supplierRepository.selectCount(new SupplierQuery().setIndustryId(accountId)));
        return resVO;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void submitNameAuthInfo(NameAuthVO nameAuthVO) {
        AuditAccountVO auditAccountVO = new AuditAccountVO();
        auditAccountVO.setAccountId(SecurityUtils.getAccountId());
        auditAccountVO.setUsername(SecurityUtils.getUsername());
        auditAccountVO.setRole(SecurityUtils.getRole());
        auditAccountVO.setName(nameAuthVO.getName());
        AuditDataNameAuthVO auditDataNameAuthVO = new AuditDataNameAuthVO();
        auditDataNameAuthVO.setName(nameAuthVO.getName());
        auditDataNameAuthVO.setNameAuthInfo(JSONObject.toJSONString(nameAuthVO));
//        auditFacade.submitNameAuth(AuditEnum.TemplateType.NAME_AUTH.getCode(), auditAccountVO, auditDataNameAuthVO);
        //
        accountDomain.nameAuthSubmit(SecurityUtils.getAccountId(), nameAuthVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateEmp(Long pid, List<SubProxySaveReq> subProxySaveReqList) {
        for (SubProxySaveReq subProxySaveReq : subProxySaveReqList) {
            accountDomain.proxySave(pid, subProxySaveReq);
        }
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void customSaveBatch(List<CustomSaveBatchReq> customSaveBatchReqList) {
        for (CustomSaveBatchReq customSaveBatchReq : customSaveBatchReqList) {
            IdentitySaveReq customSaveReq = TransferUtils.transfer(customSaveBatchReq, IdentitySaveReq::new);
            for (Long roleId : customSaveBatchReq.getRoleIdList()) {
//                customSaveReq.setRoleId(roleId);
                AbsIdentityPolicySupport.getPolicy(roleId).customRegister(customSaveReq);
            }
        }
    }

    @Override
    public AccountFinanceVO accountFinanceVO(CommonEnum.Client client, Long accountId) {
        AccountFinanceVO resVO = new AccountFinanceVO();

        // OPTIMIZE 保险起见, 获取当前端的所有角色去查询,以应对升级逻辑
        List<RoleEnum.CompanyRole> roleList = RoleEnumUtil.findClientRoleList(client).collect(Collectors.toList());

        // 查询实打实到账的收益
        IncomeQuery incomeReq = new IncomeQuery();
        incomeReq.setAccountId(accountId);
        incomeReq.setRoleList(roleList);
        incomeReq.setState(1);
        incomeReq.setConsumeTypeList(CollUtil.newArrayList(
                EarningsEnum.ConsumeType.PICK_PACK,
                EarningsEnum.ConsumeType.DIVIDEND_BONUS,
                EarningsEnum.ConsumeType.GOODS
        ));
        // 根据消费类型分组
        Map<EarningsEnum.ConsumeType, Integer> incomeMap = earningFacade.queryIncome(incomeReq);
        // 收益
        resVO.setPackIncome(MapUtil.get(incomeMap, EarningsEnum.ConsumeType.PICK_PACK, Integer.class, 0));
        // 分红
        resVO.setDividendBonus(MapUtil.get(incomeMap, EarningsEnum.ConsumeType.DIVIDEND_BONUS, Integer.class, 0));
        // 商品消费
        resVO.setGoodsIncome(MapUtil.get(incomeMap, EarningsEnum.ConsumeType.GOODS, Integer.class, 0));

        // 查待结算的收益
        IncomeQuery settleIncomeReq = new IncomeQuery();
        settleIncomeReq.setAccountId(accountId);
        settleIncomeReq.setRoleList(roleList);
        settleIncomeReq.setStateNot(1);
        settleIncomeReq.setConsumeType(EarningsEnum.ConsumeType.GOODS);
        Map<EarningsEnum.ConsumeType, Integer> settleIncomeMap = earningFacade.queryIncome(settleIncomeReq);
        Integer goodSettleIncome = MapUtil.get(settleIncomeMap, EarningsEnum.ConsumeType.GOODS, Integer.class, 0);
        resVO.setGoodsSettleIncome(goodSettleIncome);

        // 计算总收益
        resVO.setTotalIncome(resVO.getGoodsIncome() + resVO.getPackIncome() + resVO.getDividendBonus());
        resVO.setId(accountId);

        return resVO;
    }

    @Override
    public Page<AccountAwardUserVO> listOperatorUser(AccountAwardUserQuery req) {



        QueryWrapper<AccountAwardUserVO> accountWrapper = new QueryWrapper<>();
        accountWrapper.nested(StringUtils.isNotBlank(req.getName()), wrapper ->
                wrapper.like("username", req.getName())
                        .or()
                        .like("realName", req.getName())
                        .or()
                        .like("nickname", req.getName())
        );

        accountWrapper.and(wrapper -> wrapper
                .like("role_id_list", RoleEnum.CompanyRole.SELECTOR.getCode())
                .or()
                .like("role_id_list", RoleEnum.CompanyRole.DEALER.getCode())
                .or()
                .like("role_id_list", RoleEnum.CompanyRole.OPERATOR.getCode())).eq("state", 1);

//        List<AccountAwardUserVO> list = accountDAO.selectList(accountWrapper);
        List<AccountVO> list = accountRepository.accountList(new AccountQuery());
        // List<AccountAwardUserVO> list = new ArrayList<>();
        for (AccountVO entry : list) {
//            // 有个数组，如果发现匹配任何一个-1、1004、1005、1006任何一个返回
//            Long roleId = Arrays.stream(entry.getRoleIdList().split(","))
//                    .map(String::trim)
//                    .filter(str -> !str.isEmpty())
//                    .map(Long::valueOf)
//                    .filter(it -> RoleEnum.GuestCompanyRole.OPERATOR.getCode().equals(it) || RoleEnum.CompanyRole.findClientRoleIdList(CommonEnum.Client.OPERATOR).contains(it))
//                    .findFirst()
//                    .orElse(RoleEnum.GuestCompanyRole.OPERATOR.getCode());
//            entry.setRoleName(RoleEnum.CompanyRole.getByCode(roleId).getValue());
        }
        return new Page<>(list.size(), req.getPageSize(), req.getPageNo(), true);
    }

    @Override
    public Page<MemberAccountVO> pageAccount(AccountQuery query) {
        Page<AccountVO> accountPage = null;
        if (CollectionUtils.isEmpty(accountPage.getRecords())) {
            return new Page<>();
        }
        List<AccountVO> accountVOList = accountPage.getRecords();

        List<String> userAccounts = accountVOList.stream()
                .map(AccountVO::getUserAccount)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        Map<String, Integer> groupCountMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(userAccounts)) {
//            List<MemberGroupCountVO> memberGroupCountVOS = imUserRpcFacade.countGroupNum(userAccounts);
//            memberGroupCountVOS.forEach(item -> groupCountMap.put(item.getUserAccount(), item.getGroupCount()));
//        }

        Set<Long> pidSet = accountVOList.stream()
                .map(AccountVO::getPid)
                .filter(pid -> ObjectUtils.isNotEmpty(pid) && pid != 0)
                .collect(Collectors.toSet());

        Map<Long, AccountVO> parentAccountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(pidSet)) {
            List<AccountVO> parentAccountList = accountRepository.accountList(null);
            parentAccountList.forEach(parentVO -> parentAccountMap.put(parentVO.getId(), parentVO));
        }

        List<MemberAccountVO> memberAccountVOList = accountVOList.stream().map(accountVO -> {
            MemberAccountVO memberVO = new MemberAccountVO();
            memberVO.setId(accountVO.getId());
            memberVO.setUserAccount(accountVO.getUserAccount());
            memberVO.setHead(accountVO.getHead());
            memberVO.setNickname(accountVO.getNickname());
            memberVO.setState(accountVO.getState());
            memberVO.setCreateTime(accountVO.getCreateTime());
            memberVO.setPhone(accountVO.getPhone());
            memberVO.setGroupNum(groupCountMap.getOrDefault(accountVO.getUserAccount(), 0));

            Long pid = accountVO.getPid();
            if (ObjectUtils.isNotEmpty(pid) && pid != 0) {
                AccountVO parentAccount = parentAccountMap.get(pid);
                if (ObjectUtils.isNotEmpty(parentAccount)) {
                    memberVO.setPid(parentAccount.getId());
                    memberVO.setPUserAccount(parentAccount.getUserAccount());
                    memberVO.setPNickname(parentAccount.getNickname());
                }
            }
            return memberVO;
        }).collect(Collectors.toList());

        Page<MemberAccountVO> resultPage = new Page<>();


        resultPage.setTotal(accountPage.getTotal());
        resultPage.setPages(accountPage.getPages());
        resultPage.setSize(accountPage.getSize());

        return resultPage;
    }

    @Override
    public void disableAccount(AdminDisableAccountReq req) {
        AccountVO account = accountDomain.account(req.getClient(), req.getId());

        if (account.getState() != AccountEnum.State.ENABLE) {
            throw new ScmException(AccountErrorCode.PARAM_ERROR, "用户已注销或被平台禁用，不可操作");
        }
        if (req.getState() != AccountEnum.State.DESTROY && req.getState() != AccountEnum.State.ENABLE) {
            throw new ScmException(AccountErrorCode.PARAM_ERROR, "只能进行启用或者禁用");
        }
        account.setState(req.getState());
//        accountDomain.accountEdit(account);
        MemberReq command = new MemberReq();
    }

    @Override
    public Long identityCreate(AdminRegisterIdentityReq req) {
        //  构建注册参数并执行会员注册
        IdentityProxySaveReq memberRegisterReq = accountAssembler.adminRegisterReq2ProxyRegisterReq(req);
        RoleEnum.CompanyRole role = req.getRole();

        // 用上级账号查询id（非邀请人）
        if (StrUtil.isNotBlank(req.getSuperiorAccount())) {
            AccountQuery accountQuery = new AccountQuery()
                    .setUserAccount(req.getSuperiorAccount());
            AccountVO account = accountRepository.account(accountQuery);
            if (Objects.isNull(account)) {
                throw new ScmException(BaseErrorCode.NODATA, "上级账号");
            }
            // 若上级和当前不是同客户端，则视为邀请人
            if (account.getClient() != role.getClient()) {
                memberRegisterReq.setInviteId(account.getId());
            } else {
                memberRegisterReq.setPid(account.getId());
            }
        }

        // 代理注册
        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(memberRegisterReq.getRole())
                .proxyRegister(memberRegisterReq);

        // 注册失败则抛出异常，成功则重新执行登录
        if (registerRes.getErrorCode() != null) {
            throw new ScmException(registerRes.getErrorCode());
        }

        return registerRes.getId();

    }

    @Override
    public void accountDelete(List<Long> idList) {
        accountDomain.accountDelete(idList);
    }

}
