package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.AccountLoginLogDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.AccountLoginLogDO;
import com.newzkl.platform.base.biz.auth.model.oauth.dto.AccountLoginLogDTO;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountLoginRepositoryImpl implements AccountLoginRepository {

    private final AccountLoginLogDAO accountLoginLogDAO;

    @Override
    public void accountLoginLogSave(AccountLoginLogDTO accountLoginLog) {
        accountLoginLogDAO.insertOrUpdate(TransferUtils.transfer(accountLoginLog, AccountLoginLogDO::new));
    }

    @Override
    public Page<AccountLoginLogDTO> selectPage(AccountLoginLogQuery query) {
        BaseLambdaQueryWrapper<AccountLoginLogDO> queryWrapper = accountLoginLogDAO.getLw(query);
        Page<AccountLoginLogDO> pageList = accountLoginLogDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transferPage(pageList, AccountLoginLogDTO::new);
    }
}
