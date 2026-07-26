package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AdminAccountRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.AdminAccountDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AdminAccountDO;
import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 平台账号仓储实现。
 *
 * <p>分页在本层内部执行 (Page 不外泄), 对领域层降级为 List。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class AdminAccountRepositoryImpl implements AdminAccountRepository {

    private final AdminAccountDAO adminAccountDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long adminAccountSave(AdminAccountReq req) {
        AdminAccountDO accountDO = TransferUtils.transfer(req, AdminAccountDO::new);
        adminAccountDAO.insertOrUpdate(accountDO);
        return accountDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminAccountDelete(List<Long> idList) {
        adminAccountDAO.deleteByIds(idList);
    }

    @Override
    public AdminAccountRes adminAccountVO(Long id) {
        return TransferUtils.transfer(adminAccountDAO.selectById(id), AdminAccountRes::new);
    }

    @Override
    public AdminAccountRes adminAccountByUsername(String username) {
        AdminAccountDO accountDO = adminAccountDAO.selectOne(new BaseLambdaQueryWrapper<AdminAccountDO>()
                .eq(AdminAccountDO::getUsername, username)
                .last("limit 1"));
        return TransferUtils.transfer(accountDO, AdminAccountRes::new);
    }

    @Override
    public List<AdminAccountRes> adminAccountList(AdminAccountQuery query) {
        Page<AdminAccountDO> page = adminAccountDAO.selectPage(RepositorySupport.page(query), adminAccountDAO.getLw(query));
        return TransferUtils.transfers(page.getRecords(), AdminAccountRes::new);
    }
}
