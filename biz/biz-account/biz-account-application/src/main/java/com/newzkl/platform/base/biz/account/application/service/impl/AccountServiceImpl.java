package com.newzkl.platform.base.biz.account.application.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DistributionRandomInfo;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.IncomeQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.MarketDistributionApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRPCVO;
import com.newzkl.platform.base.biz.account.domain.adapt.api.UserSocialApi;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountFinanceVO;
import com.newzkl.platform.base.biz.account.model.res.AppHomePageDataVO;
import com.newzkl.platform.base.biz.account.model.res.UserHomePageRes;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.CustomSaveBatchReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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

    /**
     * 用户主页展示的门店商品条数上限 (旧实现硬编码 3)
     */
    private static final int USER_HOME_PAGE_GOODS_LIMIT = 3;

    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;
    private final UserClientDomain userClientDomain;
    private final AccountAssembler accountAssembler;

    private final UserSocialApi userSocialApi;
    private final MarketDistributionApi marketDistributionApi;
    private final GoodsStoreApi goodsStoreApi;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitNameAuthInfo(NameAuthVO nameAuthVO) {
        AuditAccountVO auditAccountVO = new AuditAccountVO();
        auditAccountVO.setAccountId(SecurityUtils.getAccountId());
        auditAccountVO.setUsername(SecurityUtils.getUsername());
        // 迁移说明: SecurityUtils.getRole() 已随业务枚举下沉, 通用层仅保留 getRoleId(), 故在业务层做码->枚举转换
        auditAccountVO.setRole(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));
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
    @Transactional(rollbackFor = Exception.class)
    public void customSaveBatch(List<CustomSaveBatchReq> customSaveBatchReqList) {
        for (CustomSaveBatchReq customSaveBatchReq : customSaveBatchReqList) {
            IdentityCustomSaveReq customSaveReq = TransferUtils.transfer(customSaveBatchReq, IdentityCustomSaveReq::new);
            for (Long roleId : customSaveBatchReq.getRoleIdList()) {
//                customSaveReq.setRoleId(roleId);
                AbsIdentityPolicySupport.getPolicy(roleId).customRegister(customSaveReq);
            }
        }
    }



    @Override
    public Page<MemberAccountVO> pageAccount(AccountQuery query) {
        Page<AccountVO> accountPage = accountRepository.accountPage(query);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
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
        resultPage.setRecords(memberAccountVOList);
        resultPage.setTotal(accountPage.getTotal());
        resultPage.setPages(accountPage.getPages());
        resultPage.setSize(accountPage.getSize());
        resultPage.setCurrent(accountPage.getCurrent());

        return resultPage;
    }

    @Override
    public void disableAccount(AdminDisableAccountReq req) {
        AccountVO account = accountDomain.account(req.getClient(), req.getId());

        if (account.getState() != AccountEnum.State.ENABLE) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户已注销或被平台禁用，不可操作");
        }
        if (req.getState() != AccountEnum.State.DESTROY && req.getState() != AccountEnum.State.ENABLE) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "只能进行启用或者禁用");
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
                throw new PlatformException(BaseErrorCode.NODATA, "上级账号");
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
            throw new PlatformException(registerRes.getErrorCode());
        }

        return registerRes.getId();

    }

    @Override
    public void accountDelete(List<Long> idList) {
        accountDomain.accountDelete(idList);
    }

    @Override
    public UserHomePageRes getUserHomePage(Long userId, Long currentUserId) {
        if (userId == null) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户ID不能为空");
        }
        UserHomePageRes res = new UserHomePageRes();
        res.setUserId(userId);
        // 当前用户是否关注被查看用户 (看自己恒 false)
        if (currentUserId != null && !currentUserId.equals(userId)) {
            res.setIsFollowed(userSocialApi.isFollowed(currentUserId, userId));
        } else {
            res.setIsFollowed(Boolean.FALSE);
        }
        MemberVO member = userClientDomain.member(userId);
        if (Objects.nonNull(member)) {
            res.setNickname(member.getNickname());
            res.setHead(member.getHead());
            res.setBackgroundImg(member.getBackgroundImg());
        }
        res.setFollowingCount(userSocialApi.countFollowing(userId));
        res.setFollowerCount(userSocialApi.countFollower(userId));
        // 被查看用户的门店 + 门店下最多 3 个铺货商品
        StoreRPCVO store = goodsStoreApi.storeByChannelId(userId);
        if (store != null && store.getId() != null) {
            res.setHasStore(Boolean.TRUE);
            UserHomePageRes.StoreInfo storeInfo = new UserHomePageRes.StoreInfo();
            storeInfo.setStoreId(store.getId());
            storeInfo.setStoreName(store.getName());
            storeInfo.setStoreLogo(store.getLogo());
            List<DistributionRandomInfo> goodsList = marketDistributionApi
                    .queryRandomDistributionByStoreIdList(Collections.singletonList(store.getId()), USER_HOME_PAGE_GOODS_LIMIT)
                    .get(store.getId());
            if (CollUtil.isNotEmpty(goodsList)) {
                storeInfo.setGoodsList(goodsList.stream().map(item -> {
                    UserHomePageRes.GoodsInfo goodsInfo = new UserHomePageRes.GoodsInfo();
                    goodsInfo.setDistributionId(item.getId());
                    goodsInfo.setGoodsId(item.getGoodsId());
                    goodsInfo.setName(item.getName());
                    goodsInfo.setImg(item.getImg());
                    goodsInfo.setSellPrice(Money.of(item.getSellPrice()));
                    return goodsInfo;
                }).collect(Collectors.toList()));
            }
            res.setStoreInfo(storeInfo);
        } else {
            res.setHasStore(Boolean.FALSE);
        }
        res.setLikeCount(userSocialApi.totalLikeCount(userId));
        return res;
    }

}
