package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
// TODO[infra-sms gateway]: import ...support.SmsMethod; (SmsApi=Forest外部短信网关, 出域, 未迁)
import com.newzkl.platform.base.biz.account.model.support.VerificationCodeReq;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.model.support.UserProperties;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.AccountDAO;
import com.newzkl.platform.base.biz.account.infrastructure.dao.MemberDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.AccountDO;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubStructureReq;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccount;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.res.UserCountRes;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.identity.MemberAssembler;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final AccountAssembler accountAssembler;

    private final MemberDAO memberDAO;
    private final MemberAssembler memberAssembler;

    @Override
    public Long findId(AccountQuery accountQuery) {
        return getId(accountDAO, accountDAO.getLw(accountQuery));
    }

    @Override
    public List<Long> findIdList(AccountQuery accountQuery) {
        return listIds(accountDAO, accountDAO.getLw(accountQuery));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean destroy(AccountVO accountVO, RoleEnum.CompanyRole role) {
        // 移除角色
        String roleIdStr = accountVO.getRoleIdList();
        List<String> roleIdList = StrUtil.split(roleIdStr, ",");
        CollUtil.removeAny(roleIdList, role.getCodeStr());

        // 若新旧一样,说明没角色可移除
        String newRoleIdStr = CollUtil.join(roleIdList, ",");
        if (newRoleIdStr.equals(roleIdStr)) {
            throw new ScmException(AccountErrorCode.NOT_OPEN_ROLE);
        }
        // 若没有角色了,直接注销
        if (StrUtil.isBlank(newRoleIdStr)) {
            accountVO.setUsername(accountVO.getId().toString());
            accountVO.setState(AccountEnum.State.DESTROY);
        } else {
            accountVO.setRoleIdList(newRoleIdStr);
        }
        return accountEdit(accountVO, null);
    }

    @Override
    public UpIdRes channelOperatorId(Long accountId) {
//        UpIdRes upIdRes = channelDAO.channelUpId(accountId);
        return null;
    }

    @Override
    public List<AccountStructureVO> findScopeSubAccountStructure(Long accountId) {
        SubStructureReq req = new SubStructureReq();
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
    public OperatorListQueryVO operatorAccountListQuery() {
        AccountQuery query = new AccountQuery();
        query.setState(AccountEnum.State.ENABLE);
        query.setClient(CommonEnum.Client.OPERATOR);

        // 根据条件查询 id 和 角色
        Map<Long, String> accountIdRoleMap =
                mapOneField(accountDAO, accountDAO.getLw(query), AccountDO::getId, AccountDO::getRoleIdList);

        OperatorListQueryVO listQueryVO = new OperatorListQueryVO();
        if (MapUtil.isEmpty(accountIdRoleMap)) {
            return listQueryVO;
        }
        List<Long> guestList = new ArrayList<>();
        listQueryVO.setGuestList(guestList);
        List<Long> selectorList = new ArrayList<>();
        listQueryVO.setSelectorList(selectorList);
        List<Long> dealerList = new ArrayList<>();
        listQueryVO.setDealerList(dealerList);
        List<Long> operatorList = new ArrayList<>();
        listQueryVO.setOperatorList(operatorList);
        accountIdRoleMap.forEach((key, value) -> {
            List<RoleEnum.CompanyRole> roleList = RoleEnumUtil.getOperatorLevelUpEnumList(value);
            if (roleList.contains(RoleEnum.CompanyRole.OPERATOR_GUEST)) {
                guestList.add(key);
            }
            if (roleList.contains(RoleEnum.CompanyRole.SELECTOR)) {
                selectorList.add(key);
            }
            if (roleList.contains(RoleEnum.CompanyRole.DEALER)) {
                dealerList.add(key);
            }
            if (roleList.contains(RoleEnum.CompanyRole.OPERATOR)) {
                operatorList.add(key);
            }
        });
        return listQueryVO;
    }

    @Override
    public List<AccountRPCResVO> accountAndSonAccountRpcList(List<Long> list) {

        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 通过id查询账号信息
        QueryWrapper<AccountOutRes> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        List<AccountRPCResVO> voList = accountDAO.selectAwardListByIdList(queryWrapper);
        for (AccountRPCResVO entry : voList) {
            RoleEnum.CompanyRole roleId = Arrays.stream(entry.getRoleIdList().split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .map(Long::parseLong)
                    .filter(it -> RoleEnum.CompanyRole.OPERATOR_GUEST.equals(it) ||
                            RoleEnumUtil.findClientRoleIdList(CommonEnum.Client.OPERATOR).contains(it))
                    .findFirst()
                    .map(RoleEnum.CompanyRole::getByCode)
                    .orElse(RoleEnum.CompanyRole.OPERATOR_GUEST);
            entry.setRole(roleId);
            entry.setRoleName(Opt.ofNullable(roleId).map(RoleEnum.CompanyRole::getValue).orElse("游客"));
            entry.setSonAccounts(accountDAO.selectSonIdList(entry.getId()));
        }
        return voList;
    }

    @Override
    public List<AccountBillUserRpcVO> accountBillUserRpcList() {
        QueryWrapper<AccountBillUserRpcVO> accountWrapper = new QueryWrapper<>();

        accountWrapper.and(wrapper -> wrapper
                        .like("role_id_list", RoleEnum.CompanyRole.SELECTOR.getCode())
                        .or()
                        .like("role_id_list", RoleEnum.CompanyRole.DEALER.getCode())
                        .or()
                        .like("role_id_list", RoleEnum.CompanyRole.OPERATOR.getCode()))
                .eq("state", 1);

        List<AccountBillUserRpcVO> list = accountDAO.selectBillUserList(accountWrapper);

        for (AccountBillUserRpcVO entry : list) {
            // 有个数组，如果发现匹配任何一个1004、1005、1006任何一个返回
            RoleEnum.CompanyRole role = Arrays.stream(entry.getRoleIdList().split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .map(Long::parseLong)
                    .filter(RoleEnumUtil.findClientRoleIdList(CommonEnum.Client.OPERATOR)::contains)
                    .findFirst()
                    .map(RoleEnum.CompanyRole::getByCode)
                    .orElse(RoleEnum.CompanyRole.SELECTOR);
            entry.setRole(role);
        }
        return list;
    }

    @Override
    public AccountVO selectByUserAccount(String userAccount) {
        AccountDO accountDO = accountDAO.selectByUserAccount(userAccount);
        return TransferUtils.transfer(accountDO, AccountVO::new);
    }


    @Override
    public boolean accountSave(AccountVO account) {
        if (account.getPid() == null || account.getPid() == 0) {
            // 根据不为空的pid构建关联的pidList和pRoleList
            if (StrUtil.isBlank(account.getPidList()) || StrUtil.isBlank(account.getPRoleList())) {
                account.setPidList(ScmUtil.getPidList(account.getPidList(), account.getId()));
                account.setPRoleList(ScmUtil.getPRoleList(account.getPRoleList(), account.getRoleIdList()));
            }
        }

        return accountDAO.insert(TransferUtils.transfer(account, AccountDO::new)) > 0;
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
    public AccountVO account(CommonEnum.Client client, Long id) {
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
    public void subAccountSave(SubAccount subAccount) {
//        accountDAO.insert(accountAssembler.subAccountToDO(subAccount));
    }

    @Override
    public void subAccountEdit(SubAccount subAccount) {
//        accountDAO.updateByPrimaryKeySelective(accountAssembler.subAccountToDO(subAccount));
    }

    @Override
    public SubAccount subAccount(Long id) {
        AccountDO accountDO = accountDAO.selectById(id);
//        return accountAssembler.doToSubAccount(accountDO);
        return null;
    }

    @Override
    public void verificationCode(VerificationCodeReq verificationCodeReq) {
        // 若是测试且为通行验证码就直接放行
        if (SecurityContextHolder.isDev() && UserProperties.isPassSmsCode(verificationCodeReq.getCode())) {
            return;
        }
        // TODO[infra-sms gateway]: boolean isRight = SmsMethod.verificationCode(verificationCodeReq);
        boolean isRight = true;
        if (!isRight) {
            throw new ScmException(AccountErrorCode.CODE_ERROR);
        }
    }

    @Override
    public UserCountRes userCount() {
        // FIXME 统计方式有问题, 待修改
        return accountDAO.userCount();
    }

    @Override
    public List<GroupCountRes> groupCount(TimeQuery timeQuery) {
        // FIXME 统计方式有问题, 待修改
        return accountDAO.groupCount(timeQuery);
    }

    @Override
    public List<AccountVO> listAccountByIds(List<Long> accountIdList) {
        return accountDAO.listAccountByIds(accountIdList);
    }

}
