package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.auth.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.account.infrastructure.auth.dao.AccountLoginLogDAO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AccountLoginLogDO;
import com.newzkl.platform.base.biz.account.model.req.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLog;
import com.newzkl.platform.base.biz.account.model.auth.vo.AccountLoginLogVO;
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
    public void accountLoginLogSave(AccountLoginLog accountLoginLog) {
        accountLoginLogDAO.insertOrUpdate(TransferUtils.transfer(accountLoginLog, AccountLoginLogDO::new));
    }

    @Override
    public Page<AccountLoginLogVO> selectPage(AccountLoginLogQuery query) {
        BaseLambdaQueryWrapper<AccountLoginLogDO> queryWrapper = accountLoginLogDAO.getLw(query);
        Page<AccountLoginLogDO> pageList = accountLoginLogDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transferPage(pageList, AccountLoginLogVO::new);
    }
}
