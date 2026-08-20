package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.AccountDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.AccountDO;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.ChildStructureReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.redis.model.req.VerificationCodeReq;
import com.newzkl.platform.base.common.core.redis.utils.SmsMethod;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO[infra-sms gateway]: import ...support.SmsMethod; (SmsApi=Forest外部短信网关, 出域, 未迁)

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:44
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl extends RepositorySupport implements AccountRepository {

    private final AccountDAO accountDAO;

    @Override
    public Long findId(AccountQuery accountQuery) {
        return findOneField(accountDAO, accountDAO.getLw(accountQuery), AccountDO::getId);
    }

    @Override
    public List<Long> findIdList(AccountQuery accountQuery) {
        return listIds(accountDAO, accountDAO.getLw(accountQuery));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean destroy(AccountVO accountVO, AccountEnum.Identity identity) {
        // 移除角色
        String roleIdStr = accountVO.getIdentityList();
        List<String> roleIdList = StrUtil.split(roleIdStr, ",");
        CollUtil.removeAny(roleIdList, identity.getCodeStr());

        // 若新旧一样,说明没角色可移除
        String newRoleIdStr = CollUtil.join(roleIdList, ",");
        if (newRoleIdStr.equals(roleIdStr)) {
            throw new PlatformException(AccountErrorCode.NOT_OPEN_ROLE);
        }
        // 若没有角色了,直接注销
        if (StrUtil.isBlank(newRoleIdStr)) {
            accountVO.setUsername(accountVO.getId().toString());
            accountVO.setState(AccountEnum.State.DESTROY);
        } else {
            accountVO.setIdentityList(newRoleIdStr);
        }
        return accountEdit(accountVO, null);
    }

    @Override
    public List<AccountStructureVO> findScopeSubAccountStructure(Long accountId) {
        ChildStructureReq req = new ChildStructureReq();
        req.setPid(accountId);
        return accountDAO.findScopeSubAccountStructure(accountId);
    }

    @Override
    public Long selectCount(AccountQuery query) {
        query.addCountField();
        BizCountMap countMap = accountDAO.countByCondition(query, accountDAO.getLw(query));
        return countMap.getCount(0);
    }

    @Override
    public AccountVO selectByUserAccount(String userAccount) {
        AccountDO accountDO = accountDAO.selectByUserAccount(userAccount);
        return TransferUtils.transfer(accountDO, AccountVO::new);
    }


    @Override
    public Long accountSave(AccountVO account) {
        if (account.getPid() == null || account.getPid() == 0) {
            // 根据不为空的pid构建关联的pidList和pRoleList
            if (StrUtil.isBlank(account.getPidList()) || StrUtil.isBlank(account.getPRoleList())) {
                account.setPidList(BizUtil.getPidList(account.getPidList(), account.getId()));
                account.setPRoleList(BizUtil.getPIdentityList(account.getPRoleList(), account.getIdentityList()));
            }
        }
        AccountDO entity = TransferUtils.transfer(account, AccountDO::new);
        accountDAO.insert(entity);
        return entity.getId();
    }

    @Override
    public void accountDelete(List<Long> idList) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setIdList(idList);
        accountDAO.deleteByIds(idList);
    }

    @Override
    public boolean accountEdit(AccountVO account, AccountQuery query) {
        if (account == null) return false;
        AccountDO accountDO = TransferUtils.transfer(account, AccountDO::new);
        // 无查询条件直接使用id更新
        if (query == null) {
            return accountDAO.updateById(accountDO) > 0;
        } else {
            return accountDAO.update(accountDO, accountDAO.getLw(query)) > 0;
        }
    }

    @Override
    public AccountVO account(AccountEnum.Client client, Long id) {
        AccountQuery query = new AccountQuery();
        query.setClient(client);
        query.setId(id);
        return account(query);
    }

    @Override
    public AccountVO account(AccountQuery query) {
        AccountDO accountDO = accountDAO.selectOne(accountDAO.getLw(query));
        return TransferUtils.transfer(accountDO, AccountVO::new);
    }

    @Override
    public List<AccountVO> accountList(AccountQuery query) {
        List<AccountDO> accountDO = accountDAO.selectList(accountDAO.getLw(query));
        return TransferUtils.transfers(accountDO, AccountVO.class);
    }

    @Override
    public Page<AccountVO> accountPage(AccountQuery query) {
        return TransferUtils.transferPage(accountDAO.selectPage(RepositorySupport.page(query), accountDAO.getLw(query)), AccountVO::new);
    }

    @Override
    public <T> List<T> listObj(AccountQuery query, Class<T> clazz) {
        return list(accountDAO, accountDAO.getLw(query), clazz);
    }

    @Override
    public <T> Page<T> pageObj(AccountQuery query, Class<T> clazz) {
        return page(accountDAO, query, accountDAO.getLw(query), clazz);
    }

    @Override
    public <T> T getOne(AccountQuery query, Class<T> clazz) {
        return getOne(accountDAO, accountDAO.getLw(query), clazz);
    }

    @Override
    public int accountCountByQuery(AccountQuery accountQuery) {
//        return accountDAO.countByQuery(accountQuery);
        return 0;
    }

    @Override
    public void verificationCode(VerificationCodeReq verificationCodeReq) {
        boolean isRight = SmsMethod.verificationCode(verificationCodeReq);
        if (!isRight) {
            throw new PlatformException(AccountErrorCode.CODE_ERROR);
        }
    }

    @Override
    public List<AccountVO> listAccountByIds(List<Long> accountIdList) {
        return accountDAO.listAccountByIds(accountIdList);
    }

}
