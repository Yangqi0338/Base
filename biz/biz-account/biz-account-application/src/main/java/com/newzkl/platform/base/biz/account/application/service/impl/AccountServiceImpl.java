package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PermissionApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDomain accountDomain;
    private final AccountRepository accountRepository;
    private final AccountAssembler accountAssembler;
    private final PermissionApi permissionApi;


    @Override
    public Page<?> pageAccount(AccountQuery query) {
        AccountEnum.Client client = query.getClient();
        Page<AccountVO> accountPage = accountRepository.accountPage(query);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
            return new Page<>();
        }
        List<AccountVO> accountList = accountPage.getRecords();

        List<Long> pidList = accountList.stream()
                .map(AccountVO::getPid)
                .filter(pid -> ObjectUtils.isNotEmpty(pid) && pid != 0)
                .toList();

        Map<Long, AccountVO> parentAccountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(pidList)) {
            AccountQuery parentQuery = new AccountQuery();
            parentQuery.setIdList(pidList);
            List<AccountVO> parentAccountList = accountRepository.accountList(parentQuery);
            parentAccountList.forEach(parentVO -> parentAccountMap.put(parentVO.getId(), parentVO));
        }

        // 角色查询
        Map<Long, List<Long>> roleMap = permissionApi.findRoleByAccountIdList(client, CollUtil.map(accountList, AccountVO::getId, true));

        return TransferUtils.transferPage(accountPage, (accountVO)-> {
            accountVO.setRoleIdList(roleMap.getOrDefault(accountVO.getId(), new ArrayList<>()));
            switch (client) {
                case ADMIN -> {
                    return TransferUtils.transfer(accountVO, EmpAccountVO.class);
                }
                case USER -> {
                    MemberAccountVO memberVO = TransferUtils.transfer(accountVO, MemberAccountVO.class);
                    Long pid = accountVO.getPid();
                    if (ObjectUtils.isNotEmpty(pid) && pid != 0) {
                        AccountVO parentAccount = parentAccountMap.get(pid);
                        if (ObjectUtils.isNotEmpty(parentAccount)) {
                            memberVO.setPid(parentAccount.getId());
                            memberVO.setPUsername(parentAccount.getUsername());
                            memberVO.setPNickname(parentAccount.getNickname());
                        }
                    }
                    return memberVO;
                }
                case PARTNER -> {
                    return null;
                }
                case CHANNEL -> {
                    return null;
                }
                case SUPPLIER -> {
                    return null;
                }
                default -> {
                    return null;
                }
            }
        });
    }

    @Override
    public void disableAccount(AdminDisableAccountReq req) {
        AccountVO account = accountDomain.account(req.getClient(), req.getId());

        if (account.getState() == AccountEnum.State.DESTROY) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户已注销，不可操作");
        }
        if (req.getState() != AccountEnum.State.DISABLE && req.getState() != AccountEnum.State.ENABLE) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "只能进行启用或者禁用");
        }

        // 仅改 account 状态: 只带 id/client/state 三列落库, 关联身份实体状态不变
        AccountVO update = new AccountVO();
        update.setId(account.getId());
        update.setClient(account.getClient());
        update.setState(req.getState());
        accountRepository.accountEdit(update, null);
    }

    @Override
    public Long identityCreate(AdminRegisterIdentityReq req) {
        AccountEnum.Identity identity = req.getIdentity();

        // 账号占用校验: 按手机号+端查在库账号
        // 注销只改状态并回收 username(改写为 id), phone 仍在, 物理删除由定时任务回收
        // 故此处按 phone 命中即视为占用, 待注销行被清理后方可复用
        AccountQuery existQuery = new AccountQuery();
        existQuery.setPhone(req.getPhone());
        existQuery.setClient(identity.getClient());
        if (accountRepository.selectCount(existQuery) > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 落 account: 登录账号缺省用手机号, 拿回自增 id
        IdentityRegisterRpcReq rpcReq = TransferUtils.transfer(req,IdentityRegisterRpcReq.class);
        rpcReq.setIdentity(identity);
        rpcReq.setClient(identity.getClient());
        rpcReq.setUsername(StrUtil.blankToDefault(req.getUsername(), req.getPhone()));
        AccountVO account = accountDomain.register(rpcReq);

        // 角色初始化: 组装身份初始化参数并分发到对应角色策略(含账号-角色绑定)
        // req 无 id, 需回填注册拿到的 account id 供策略消费
        IdentityCustomSaveReq saveReq = TransferUtils.transfer(req, IdentityCustomSaveReq.class);
        saveReq.setId(account.getId());
        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(identity)
                .customRegister(saveReq);

        // 注册失败则抛出异常
        if (registerRes.getErrorCode() != null) {
            throw new PlatformException(registerRes.getErrorCode());
        }

        return account.getId();
    }

    @Override
    public void bindRoles(Long accountId, Collection<Long> roleIds) {
        AccountEnum.Client client = SecurityUtils.getClient();
        AccountVO account = accountDomain.account(client, accountId);
        if (account == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "账号");
        }
        permissionApi.bindRoles(client, accountId, roleIds == null ? null : new ArrayList<>(roleIds));
    }

}
