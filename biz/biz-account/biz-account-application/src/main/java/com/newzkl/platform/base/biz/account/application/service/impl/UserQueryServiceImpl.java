package com.newzkl.platform.base.biz.account.application.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRPCVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.*;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.rpc.StoreOutVO;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.identity.*;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@RequiredArgsConstructor
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final AccountAssembler accountAssembler;


    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;

    private final SupplierClientDomain supplierClientDomain;
    private final UserClientDomain userDomain;

    private final ChannelClientDomain channelClientDomain;

    private final MemberAssembler memberAssembler;
    private final ChannelAssembler channelAssembler;
    private final EmpAssembler empAssembler;

    private final FinancePurseApi financePurseApi;
    private final GoodsStoreApi goodsStoreApi;

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


    @Override
    public Page<ChannelVO> channelPage(ChannelQuery channelQuery) {

//        List<ChannelVO> channelVO = channelClientDomain.listByQuery(channelQuery);
        List<ChannelVO> channelVO = new ArrayList<>();


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
            vo.setEarnings(Money.of(Optional.ofNullable(purseAmountRes.getEarnings()).orElse(0)));
            //填充商品位
            PurseAmountRes goodsSeat = goodsSeatMap.getOrDefault(vo.getId(), new PurseAmountRes());
            vo.setProductSeatCount(Optional.ofNullable(goodsSeat.getEarnings()).orElse(0));
            vo.setUsedProductSeat(0);
        }
        return null;
    }

    @Override
    public ChannelEarningsConfigVO serviceFeeConfigVO(Long channelId) {
//        return channelDAO.serviceFeeConfigVO(channelId);
        return null;
    }



















    @Override
    public SupplierVO supplierVO(Long supplierId) {
        SupplierQuery query = new SupplierQuery();
        query.setId(supplierId);
        SupplierVO supplierVO = supplierClientDomain.supplier(supplierId);
        return supplierVO;
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
    public AppAccountVO appVO(AccountKeyQuery query) {
        CommonEnum.Client client = query.getClient();
        Long id = query.getAccountId();
        AccountVO accountVO = accountDomain.account(client, id);
        AppAccountVO appAccountVO = accountAssembler.account2AppVO(accountVO);
        appAccountVO.setRole(SecurityUtils.getRole());

        // 获取channel角色表里的storePermission
//        CommonEnum.YesOrNo storePermission = channelDAO.hasStore(id);
//        appAccountVO.setHasStore(CommonEnum.YesOrNo.YES == storePermission);
        appAccountVO.setHasChannel(accountVO.getRoleIdList().contains(RoleEnum.CompanyRole.CHANNEL.getCode() + ""));
        switch (client) {
            default:
                break;
        }
        return appAccountVO;
    }



    @Override
    public Page<SupplierRes> supplierPage(SupplierQuery supplierQuery) {
        RoleEnum.CompanyRole role = SecurityUtils.getRole();

        Page<SupplierRes> page = supplierClientDomain.supplierPage(supplierQuery);


        return page;
    }

}
