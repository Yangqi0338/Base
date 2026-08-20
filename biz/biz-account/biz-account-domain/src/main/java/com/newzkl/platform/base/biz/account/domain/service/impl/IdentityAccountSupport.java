package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.IdentityAccountQuery;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:47
 */
@Slf4j
@Service
public class IdentityAccountSupport {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountAssembler accountAssembler;

    protected List<Long> findIdByQuery(AccountEnum.Client client, IdentityAccountQuery identityAccountQuery) {
        AccountQuery query = accountAssembler.identityQuery2Query(identityAccountQuery);
        query.setClient(client);
        List<Long> idList = new ArrayList<>();
        CollUtil.addAll(idList, accountRepository.findIdList(query));
        return idList;
    }

}
