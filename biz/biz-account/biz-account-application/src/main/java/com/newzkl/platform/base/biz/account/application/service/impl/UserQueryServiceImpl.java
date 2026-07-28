package com.newzkl.platform.base.biz.account.application.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.adapt.api.AccountContributeRpcQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.AccountPurseReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.EarningContributeRpcVO;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceEarningApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRPCVO;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.*;
import com.newzkl.platform.base.biz.account.infrastructure.entity.DealerDO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.OperatorDO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.SelectorDO;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.rpc.StoreOutVO;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.identity.*;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.model.exception.BaseErrorCode.PARAM;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@RequiredArgsConstructor
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final AccountAssembler accountAssembler;

    private final OperatorClientDomain operatorDomain;

    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;

    private final SupplierClientDomain supplierClientDomain;
    private final UserClientDomain userDomain;

    private final ChannelClientDomain channelClientDomain;

    private final MemberAssembler memberAssembler;
    private final ChannelAssembler channelAssembler;
    private final DealerAssembler dealerAssembler;
    private final EmpAssembler empAssembler;
    private final OperatorAssembler operatorAssembler;
    private final SupplierAssembler supplierAssembler;
    private final SelectorAssembler selectorAssembler;

    private final FinanceEarningApi financeEarningApi;
    private final FinancePurseApi financePurseApi;
    private final GoodsStoreApi goodsStoreApi;

    private final CdkDomain cdkDomain;
    private final CountSaleDomain countSaleDomain;

    /**
     * 开通码分页。
     *
     * <p>迁移补充: 旧 {@code cdkPage} 返回 PageHelper 的 {@code PageInfo}, 中台统一返回
     * MyBatis-Plus 分页, 记录类型为出参对象。</p>
     *
     * @param cdkQuery 开通码查询
     * @return 开通码分页
     * @author KC
     */
    @Override
    public Page<CdkRes> cdkPage(CdkQuery cdkQuery) {
        return cdkDomain.pageList(cdkQuery);
    }

    @Override
    public AccountOutRes accountOutVO(CommonEnum.Client client, Long id) {
        //查询账号
        AccountVO accountVO = accountDomain.account(client, id);
        AccountOutRes accountOutVO = accountAssembler.vo2OutRes(accountVO);

        //设置角色信息
        String roleIdList = accountVO.getRoleIdList();
        List<RoleEnum.CompanyRole> companyRoleList = RoleEnumUtil.getLevelUpEnumList(client, roleIdList);
        for (RoleEnum.CompanyRole role : companyRoleList) {
            Object identityVO = userIdentityDetail(id, role);
            switch (role) {
                case EMP:
                    accountOutVO.setEmp(empAssembler.vo2OutRes((EmpVO) identityVO));
                    break;
                case MEMBER:
                    accountOutVO.setMember(memberAssembler.vo2OutRes((MemberVO) identityVO));
                    break;
                case SUPPLIER:
//                    accountOutVO.setSupplier(supplierAssembler.vo2OutRes((SupplierVO) identityVO));
                    break;
                case CHANNEL:
                    accountOutVO.setChannel(channelAssembler.vo2OutRes((ChannelVO) identityVO));
                    // 渠道商额外拿店铺信息
                    StoreRPCVO storeRPCVO = goodsStoreApi.storeByChannelId(id);
                    accountOutVO.setStore(TransferUtils.transfer(storeRPCVO, StoreOutVO::new));
                    break;
                case OPERATOR:
                    accountOutVO.setOperator(operatorAssembler.vo2OutRes((OperatorVO) identityVO));
                    break;
                case DEALER:
                    accountOutVO.setDealer(dealerAssembler.vo2OutRes((DealerVO) identityVO));
                    break;
                case SELECTOR:
                    accountOutVO.setSelector(selectorAssembler.vo2OutRes((SelectorVO) identityVO));
                    break;
                default:
                    break;
            }
        }
        // 因为登录账户就是手机号, 所以默认已绑定手机号,获得50分.
        int safeIndex = 50;
        if (StringUtils.isNotEmpty(accountVO.getPassword())) {
            accountOutVO.setIsSetPassword(CommonEnum.YesOrNo.YES);
            safeIndex = safeIndex + 50;
        }
        accountOutVO.setSafeIndex(safeIndex);
        return accountOutVO;
    }

    @Override
    public AccountOutRes accountOutVO(String phone) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setPhone(phone);
        AccountInfo accountInfo = accountDomain.accountInfo(accountQuery);
        return accountOutVO(accountInfo.getClient(), accountInfo.getId());
    }

    @Override
    public Object userIdentityDetail(Long id, RoleEnum.CompanyRole role) {
        return AbsIdentityPolicySupport.getPolicy(role).detail(id);
    }

    @Override
    public AccountVO accountByYqm(String yqm) {
        if (StringUtils.isEmpty(yqm)) {
            return null;
        }
//        return accountDAO.accountByYqm(yqm);
        return null;
    }

    @Override
    public UserCountRes userCount() {
        return accountRepository.userCount();
    }

    @Override
    public List<GroupCountRes> groupCount(TimeQuery timeQuery) {
        return accountRepository.groupCount(timeQuery);
    }

    @Override
    public Long accountIdByUsername(String username) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
        accountQuery.setUsername(username);
        accountQuery.setState(AccountEnum.State.ENABLE);
        List<AccountVO> idList = accountRepository.accountList(accountQuery);
        if (ObjectUtil.isEmpty(idList)) {
            return null;
        } else {
            return idList.get(0).getId();
        }
    }

    @Override
    public Long selectorIdByUsername(String username) {
//        return operatorDomain.selectorIdByUsername(username);
        return null;
    }

    @Override
    public Long selectorPid(Long accountId) {
//        return operatorDomain.inviteIdByQuery(accountId);
        return null;
    }

    @Override
    public MemberVO memberVO(Long memberId) {
        MemberQuery query = new MemberQuery();
        query.setId(memberId);
        MemberVO memberVO = userDomain.member(memberId);
        return memberVO;
    }

    @Override
    public Page<MemberVO> memberPage(MemberQuery memberQuery) {
        List<MemberVO> memberVO = userDomain.queryMember(null);
        return new Page<>();
    }

    @Override
    public List<SupplierDescVO> supplierDescVOList(List<Long> supplierIdList) {
//        return supplierClientDomain.supplierDescVOList(supplierIdList);
        return null;
    }

    /**
     * 销售统计分页。
     *
     * <p>迁移补充: 旧实现直连 {@code CountSaleDAO.listByQuery} 并返回 PageHelper 的
     * {@code PageInfo}, 中台改由 {@link CountSaleDomain} 承接, 统一返回 MyBatis-Plus 分页。</p>
     *
     * @param countSaleQuery 销售统计查询
     * @return 销售统计分页
     * @author KC
     */
    @Override
    public Page<CountSaleVO> countSalePage(CountSaleQuery countSaleQuery) {
        return countSaleDomain.pageList(countSaleQuery);
    }

    /**
     * 按条件取单条销售统计。
     *
     * <p>迁移补充: 旧实现直连 {@code CountSaleDAO.voByQuery}, 中台改由
     * {@link CountSaleDomain} 承接; 多条命中时取第一条 (与旧 SQL 无 limit 的宽松语义对齐)。</p>
     *
     * @param todayCountSaleQuery 销售统计查询
     * @return 销售统计视图, 无则 null
     * @author KC
     */
    @Override
    public CountSaleVO countSaleByQuery(CountSaleQuery todayCountSaleQuery) {
        return countSaleDomain.findByQuery(todayCountSaleQuery);
    }

    @Override
    public UpIdRes channelUpId(Long accountId) {
//        return channelDAO.channelUpId(accountId);
        return null;
    }

    @Override
    public OperatorDomainInfo getOperatorDomainInfoBySub() {
        // 迁移说明: SecurityUtils.getRole() 已随业务枚举下沉, 通用层仅保留 getRoleId(), 故在业务层做码->枚举转换
        RoleEnum.CompanyRole currentRole = RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId());
        if (RoleEnum.CompanyRole.DEALER == currentRole) {
            DealerVO dealerVO = dealerVO(SecurityUtils.getAccountId());
            return operatorDomain.getOperatorDomainInfo(dealerVO.getOperatorId());
        } else if (RoleEnum.CompanyRole.CHANNEL == currentRole) {
            UpIdRes upIdRes = channelUpId(SecurityUtils.getAccountId());
            return operatorDomain.getOperatorDomainInfo(upIdRes.getOneId());
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }

    @Override
    public Page<ChannelVO> channelPage(ChannelQuery channelQuery) {

//        List<ChannelVO> channelVO = channelClientDomain.listByQuery(channelQuery);
        List<ChannelVO> channelVO = new ArrayList<>();


        List<Long> channelList = channelVO.stream().map(ChannelVO::getId).collect(Collectors.toList());
        AccountContributeRpcQuery req = new AccountContributeRpcQuery();
        req.setAccountIds(channelList);
        req.setAccountType(PurseEnum.FinanceUser.CHANNEL);

        // 批量查询渠道商贡献收益
        List<EarningContributeRpcVO> earningContributeRpcVOS = financeEarningApi.queryEarningContribute(req);

        Map<Long, EarningContributeRpcVO> contributeRpcMap = earningContributeRpcVOS.stream().collect(Collectors.toMap(EarningContributeRpcVO::getAccountId, Function.identity()));

        for (ChannelVO vo : channelVO) {
            EarningContributeRpcVO earningContributeRpcVO = contributeRpcMap.getOrDefault(vo.getId(), new EarningContributeRpcVO());
            vo.setEarningContribute(Optional.ofNullable(earningContributeRpcVO.getEarningContribute()).orElse(0));
        }
        //
        // List<EarningContributeRpcVO> list = .queryEarningContributeByAccountId(req);


        return new Page<>();
    }

    @Override
    public Page<ChannelPageRes> queryChannelPage(ChannelQuery channelQuery) {
//        Page<ChannelPageVO> channelPageVOPage = iChannelRepository.queryChannelPage(channelQuery);
//        List<ChannelPageVO> channelPageVOList = channelPageVOPage.getRecords();
        List<ChannelPageRes> channelPageVOList = new ArrayList<>();
        if (CollUtil.isEmpty(channelPageVOList)) {
            // 返回一个空的 Page 对象
            return new Page<>();
        }

        List<Long> channelList = channelPageVOList.stream().map(ChannelPageRes::getId).collect(Collectors.toList());
        // 客户账户查询对象
        AccountPurseReq accountPurseReq = new AccountPurseReq();
        accountPurseReq.setAccountIdList(channelList);
        accountPurseReq.setAccountType(PurseEnum.FinanceUser.CHANNEL.getType());

        //查询采购金
        accountPurseReq.setPurseType(PurseEnum.PurseType.PURCHASE.getType());
        List<PurseAmountRes> purseAmountResList = financePurseApi.queryPurse(accountPurseReq);
        Map<Long, PurseAmountRes> purseAmountMap = purseAmountResList.stream().collect(Collectors.toMap(PurseAmountRes::getAccountId, Function.identity()));

        //查询商品位
        accountPurseReq.setPurseType(PurseEnum.PurseType.GOODS_SEAT.getType());
        List<PurseAmountRes> goodsSeatList = financePurseApi.queryPurse(accountPurseReq);
        Map<Long, PurseAmountRes> goodsSeatMap = goodsSeatList.stream().collect(Collectors.toMap(PurseAmountRes::getAccountId, Function.identity()));

        for (ChannelPageRes vo : channelPageVOList) {
            //填充采购金额
            PurseAmountRes purseAmountRes = purseAmountMap.getOrDefault(vo.getId(), new PurseAmountRes());
            vo.setEarnings(Optional.ofNullable(purseAmountRes.getEarnings()).orElse(0));
            //填充商品位
            PurseAmountRes goodsSeat = goodsSeatMap.getOrDefault(vo.getId(), new PurseAmountRes());
            vo.setProductSeatCount(Optional.ofNullable(goodsSeat.getTotalEarnings()).orElse(0));
            vo.setUsedProductSeat(vo.getProductSeatCount() - Optional.ofNullable(goodsSeat.getEarnings()).orElse(0));
        }
        return null;
    }

    @Override
    public int countByQuery(ChannelQuery channelQuery) {
//        return channelDAO.countByQuery(channelQuery);
        return 0;
    }

    @Override
    public ChannelEarningsConfigVO serviceFeeConfigVO(Long channelId) {
//        return channelDAO.serviceFeeConfigVO(channelId);
        return null;
    }

    @Override
    public DealerVO dealerVO(Long dealerId) {
        DealerQuery query = new DealerQuery();
        query.setId(dealerId);
        return operatorDomain.dealer(dealerId);
    }

    @Override
    public Page<DealerVO> dealerPage(DealerQuery dealerQuery) {
        return null;
    }

    @Override
    public OperatorVO operatorVO(Long operatorId) {
        OperatorQuery query = new OperatorQuery();
        query.setId(operatorId);
        OperatorVO operatorVO = operatorDomain.operator(operatorId);
        return operatorVO;
    }

    @Override
    public Page<OperatorVO> operatorPage(OperatorQuery operatorQuery) {
//        List<OperatorVO> operatorVO = operatorDAO.listByQuery(operatorQuery);
//        Set<Long> operatorIds = operatorVO.stream().map(OperatorVO::getId).collect(Collectors.toSet());
        // 去供应商表里查询运营商邀请供应商的数量
//        List<OperatorInviteCountVO> listVO = supplierDAO.selectCountByInviteId(operatorIds);
        List<OperatorInviteCountVO> listVO = new ArrayList<>();
        Map<Long, Integer> listMap = listVO.stream().collect(Collectors.toMap(OperatorInviteCountVO::getInviteId, OperatorInviteCountVO::getInviteCount));
//        for (OperatorVO vo : operatorVO) {
//            vo.setInviteSupplierNumber(listMap.getOrDefault(vo.getId(), 0));
//        }
        Page<OperatorVO> page = new Page<OperatorVO>();
        return page;
    }

    @Override
    public SelectorVO selectorVO(Long selectorId) {
        SelectorQuery query = new SelectorQuery();
        query.setId(selectorId);
        SelectorVO selectorVO = operatorDomain.selector(selectorId);
        return selectorVO;
    }

    @Override
    public Page<SelectorVO> selectorPage(SelectorQuery selectorQuery) {

//        List<SelectorVO> selectorVO = selectorDAO.listByQuery(selectorQuery);

        Page<SelectorVO> page = new Page<SelectorVO>();
        return page;
    }

    @Override
    public SupplierVO supplierVO(Long supplierId) {
        SupplierQuery query = new SupplierQuery();
        query.setId(supplierId);
        SupplierVO supplierVO = supplierClientDomain.supplier(supplierId);
        return supplierVO;
    }

    @Override
    public Page<SelectorSupplierVO> selectorSupplierVO(SupplierQuery supplierQuery) {

//        List<SelectorSupplierVO> supplierVO = supplierDAO.listSelectorSupplierVOByQuery(supplierQuery);

        Page<SelectorSupplierVO> page = new Page<SelectorSupplierVO>();
        return page;
    }

    @Override
    public List<SupplierRelationVO> supplierRelationVO(List<Long> supplierIdList) {
        if (ObjectUtil.isEmpty(supplierIdList)) {
            return new ArrayList<>();
        }
//        return supplierDAO.supplierRelationVO(supplierIdList);
        return null;
    }

    @Override
    public IndexCountRes indexCount() {
        Long accountId = SecurityUtils.getAccountId();
        SelectorVO selectorVO = selectorVO(accountId);
        IndexCountRes indexCountRes = new IndexCountRes();
        indexCountRes.setTeamCount(selectorVO.getTeamCount());
        indexCountRes.setTeamSupplierCount(selectorVO.getTeamSupplierCount());
        indexCountRes.setTeamSelectorCount(selectorVO.getTeamSelectorCount());
        ChannelQuery channelQuery = new ChannelQuery();
        channelQuery.setStateList(Arrays.asList(ChannelEnum.State.OPEN, ChannelEnum.State.IN));
        int channelCount = countByQuery(channelQuery);
        indexCountRes.setChannelCount(channelCount);
        indexCountRes.setWeekOrderCount(0);
        indexCountRes.setMonthOrderCount(0);
        return indexCountRes;
    }

    @Override
    public String supplierRegistrationInvitationLink(String headHost) {
        Long accountId = SecurityUtils.getAccountId();
        if (accountId == null) {
            throw new PlatformException(PARAM, "请先登录");
        }

//        OperatorDO operator = operatorDAO.selectByPrimaryKey(accountId);
//        if (operator == null) {
//            throw new PlatformException(PARAM, "未找到当前域名的运营商信息");
//        }
//        String domain = operator.getDomain();
//
//        boolean innerIP = Validator.isIpv4(domain) || Validator.isIpv6(domain);
//        if (!innerIP) {
//            try {
//                String firstPointPrefix = getPrefix(domain);
//                String middleDomain = toApex(domain);
//                domain = firstPointPrefix + "." + "f" + "." + middleDomain;
//            } catch (Exception ignored) {
//            }
//        }

        return "https://" + "domain" + "/#/register";

    }

    @Override
    public OperatorVO getOperatorByDomain(String domain) {

        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setDomain(domain);

//        List<OperatorVO> operatorVOS = operatorDAO.listByQuery(operatorQuery);

//        if (CollectionUtil.isEmpty(operatorVOS)) {
//            return null;
//        }

//        return operatorVOS.get(0);
        return null;
    }

    @Override
    public AppAccountVO appVO(AccountKeyQuery query) {
        CommonEnum.Client client = query.getClient();
        Long id = query.getAccountId();
        AccountVO accountVO = accountDomain.account(client, id);
        AppAccountVO appAccountVO = accountAssembler.account2AppVO(accountVO);
        appAccountVO.setRole(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));

        // 获取channel角色表里的storePermission
//        CommonEnum.YesOrNo storePermission = channelDAO.hasStore(id);
//        appAccountVO.setHasStore(CommonEnum.YesOrNo.YES == storePermission);
        appAccountVO.setHasChannel(accountVO.getRoleIdList().contains(RoleEnum.CompanyRole.CHANNEL.getCode() + ""));
        switch (client) {
            case OPERATOR:
                doOperatorAppInfo(accountVO, appAccountVO);
                break;
            default:
                break;
        }
        return appAccountVO;
    }

    private void doOperatorAppInfo(AccountVO accountVO, AppAccountVO appAccountVO) {
        Long id = appAccountVO.getId();
        RoleEnum.CompanyRole role = appAccountVO.getRole();

        Object detail = AbsIdentityPolicySupport.getPolicy(role).detail(id);

        AccountParentQuery accountParentQuery = new AccountParentQuery();
        accountParentQuery.setId(id);
        accountParentQuery.setScope(0);
        accountParentQuery.setPidList(accountVO.getPidList());
        accountParentQuery.setRoleIdList(RoleEnumUtil.findClientRoleIdList(CommonEnum.Client.OPERATOR));
        List<SubAccountVO> subAccountList = accountDomain.subAccountList(accountParentQuery);

        appAccountVO.setOperatorCount((int) subAccountList.stream().filter(it -> it.getRole() == RoleEnum.CompanyRole.OPERATOR).count());
        appAccountVO.setSelectorCount((int) subAccountList.stream().filter(it -> it.getRole() == RoleEnum.CompanyRole.SELECTOR).count());
        appAccountVO.setDealerCount((int) subAccountList.stream().filter(it -> it.getRole() == RoleEnum.CompanyRole.DEALER).count());

        appAccountVO.setSubAccountCount(subAccountList.stream().mapToInt(SubAccountVO::getSubAccountCount).sum());
        appAccountVO.setOperatorClientCount(appAccountVO.getSelectorCount() + appAccountVO.getDealerCount() + appAccountVO.getOperatorCount());

        ChannelQuery channelQuery = new ChannelQuery();
        channelQuery.setInvitedId(id);
        channelQuery.resetOnlyPage();

        Page<ChannelVO> channelPage = channelClientDomain.channelPageList(channelQuery);
        appAccountVO.setChannelCount((int) channelPage.getTotal());

        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.setInviteId(id);
        supplierQuery.resetOnlyPage();

//        supplierDAO.listByQuery(supplierQuery);
//        appAccountVO.setSupplierCount((int) supplierPage.getTotal());

        if (detail != null) {
            switch (role) {
                case OPERATOR:
                    OperatorDO operatorDO = (OperatorDO) detail;
                    TransferUtils.transfer(operatorDO, appAccountVO);
                    appAccountVO.setHeadImg(operatorDO.getLogo());
                    break;
                case DEALER:
                    DealerDO dealerDO = (DealerDO) detail;
                    TransferUtils.transfer(dealerDO, appAccountVO);
                    break;
                case SELECTOR:
                    SelectorDO selectorDO = (SelectorDO) detail;
                    TransferUtils.transfer(selectorDO, appAccountVO);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Page<SupplierVO> operatorSupplierPage(SupplierQuery supplierQuery) {
        Long accountId = SecurityUtils.getAccountId();
//        List<Long> accountIdList = operatorDomain.supplierIdListByType(accountId, supplierQuery.getType());
//        if (CollUtil.isNotEmpty(supplierQuery.getIdList())) {
//            accountIdList = accountIdList.stream().filter(it -> supplierQuery.getIdList().contains(it)).collect(Collectors.toList());
//        }
//        if (CollUtil.isEmpty(accountIdList)) {
//            return new Page<>();
//        }
//        supplierQuery.setIdList(accountIdList);
//
//        List<SupplierVO> supplierVO = supplierDAO.listByQuery(supplierQuery);
//
//        supplierVO.forEach(item -> {
//            if (accountId.equals(item.getInviteId())) {
//                item.setName(PatternUtil.desensitized(item.getName(), 3, 2));
//                item.setCompanyName(PatternUtil.desensitized(item.getCompanyName(), 3, -2));
//            }
//        });
        return null;
    }

}
