package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.*;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.identity.*;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@RequiredArgsConstructor
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final AccountRepository accountRepository;

    private final SupplierClientDomain supplierClientDomain;
    private final UserClientDomain userDomain;

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
    public Page<SupplierRes> supplierPage(SupplierQuery supplierQuery) {
        AccountEnum.Identity identity = SecurityUtils.getIdentity();

        Page<SupplierRes> page = supplierClientDomain.supplierPage(supplierQuery);


        return page;
    }

}
