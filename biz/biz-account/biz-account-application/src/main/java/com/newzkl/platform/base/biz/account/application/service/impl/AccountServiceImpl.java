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
import com.newzkl.platform.base.biz.account.model.res.SubAccountDetailRes;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long subSave(SubAccountSaveReq req) {
        AccountEnum.Client client = SecurityUtils.getClient();
        Long mainId = SecurityUtils.getAccountId();
        // id 非空走编辑, 否则新增
        if (req.getId() != null) {
            return subEdit(client, req);
        }
        return subCreate(client, mainId, req);
    }

    /**
     * 新增子账号: 继承主账号身份建号并绑角色
     *
     * @param client 当前登录端
     * @param mainId 当前登录主账号id
     * @param req    子账号请求(id 空)
     * @return 子账号id
     */
    private Long subCreate(AccountEnum.Client client, Long mainId, SubAccountSaveReq req) {
        // 新增 password 必填(编辑非必填, 故未走 @NotBlank, 此处手动校验)
        if (StrUtil.isBlank(req.getPassword())) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "密码不能为空");
        }
        // 主账号存在校验(不存在抛)
        AccountVO main = accountDomain.account(client, mainId);

        // username 空则用手机号(同 emp)
        String username = StrUtil.blankToDefault(req.getUsername(), req.getPhone());
        // username 主账号内唯一: pid=mainId + username 命中即占用
        AccountQuery uq = new AccountQuery();
        uq.setClient(client);
        uq.setPid(mainId);
        uq.setUsername(username);
        if (accountRepository.selectCount(uq) > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }
        // phone 必填, 校验端内占用
        AccountQuery pq = new AccountQuery();
        pq.setClient(client);
        pq.setPhone(req.getPhone());
        if (accountRepository.selectCount(pq) > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 组 RPC: identity 继承主账号(主账号 identityList 首个身份)
        AccountEnum.Identity identity = resolveMainIdentity(main);
        IdentityRegisterRpcReq rpc = TransferUtils.transfer(req, IdentityRegisterRpcReq.class);
        rpc.setClient(client);
        rpc.setIdentity(identity);
        rpc.setUsername(username);
        rpc.setPid(mainId);
        // 两级: pidList = 主账号id + ','(末尾分隔符, 对齐 id_list 列约定)
        rpc.setPidList(mainId + ",");
        rpc.setMainAccountId(mainId);
        rpc.setOrigin(AccountEnum.Origin.MAIN_CREATE);
        AccountVO sub = accountDomain.register(rpc);

        // 绑角色 + recalc 派生
        if (CollUtil.isNotEmpty(req.getRoleIds())) {
            permissionApi.bindRoles(client, sub.getId(), new ArrayList<>(req.getRoleIds()));
        }
        return sub.getId();
    }

    /**
     * 编辑子账号: 归属校验后改昵称/手机号, password 非空则重置。角色不动(绑角色走独立 /bindRoles)
     *
     * @param client 当前登录端
     * @param req    子账号请求(id 非空)
     * @return 子账号id
     */
    private Long subEdit(AccountEnum.Client client, SubAccountSaveReq req) {
        AccountVO sub = assertSubOwnership(req.getId());
        AccountVO update = new AccountVO();
        update.setId(sub.getId());
        update.setClient(sub.getClient());
        update.setNickname(req.getNickname());
        update.setPhone(req.getPhone());
        if (StrUtil.isNotBlank(req.getPassword())) {
            update.setPassword(sub.getNewPassword(req.getPassword()));
        }
        accountRepository.accountEdit(update, null);
        return sub.getId();
    }

    @Override
    public Page<SubAccountDetailRes> subPage(SubAccountQuery query) {
        AccountEnum.Client client = SecurityUtils.getClient();
        Long mainId = SecurityUtils.getAccountId();

        AccountQuery q = TransferUtils.transfer(query, AccountQuery.class);
        q.setClient(client);
        q.setPid(mainId);

        Page<AccountVO> page = accountRepository.accountPage(q);
        Map<Long, List<Long>> roleMap = permissionApi.findRoleByAccountIdList(
                client, CollUtil.map(page.getRecords(), AccountVO::getId, true));
        return TransferUtils.transferPage(page, vo -> {
            SubAccountDetailRes r = TransferUtils.transfer(vo, SubAccountDetailRes::new);
            r.setRoleIds(roleMap.getOrDefault(vo.getId(), new ArrayList<>()));
            return r;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subDelete(Long id) {
        AccountVO sub = assertSubOwnership(id);
        // 清空该账号 ACCOUNT_ROLE 绑定(空集=清空)并 recalc 派生
        permissionApi.bindRoles(SecurityUtils.getClient(), sub.getId(), new ArrayList<>());
        // 逻辑删账号(del_flag=1, @TableLogic)
        accountRepository.accountDelete(List.of(sub.getId()));
    }

    /**
     * 解析主账号身份(identityList csv 首个身份)
     *
     * @param main 主账号
     * @return 主账号身份
     */
    private AccountEnum.Identity resolveMainIdentity(AccountVO main) {
        String csv = main.getIdentityList();
        if (StrUtil.isBlank(csv)) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        Long code = Long.valueOf(StrUtil.split(csv, ",").get(0));
        AccountEnum.Identity identity = AccountEnum.Identity.getByCode(code);
        if (identity == null) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        return identity;
    }

    /**
     * 子账号归属校验: 必属当前登录主账号且同端
     *
     * @param subId 子账号id
     * @return 校验通过的子账号
     */
    private AccountVO assertSubOwnership(Long subId) {
        AccountEnum.Client client = SecurityUtils.getClient();
        Long mainId = SecurityUtils.getAccountId();
        // client 隔离 + 存在校验
        AccountVO sub = accountDomain.account(client, subId);
        if (sub.getMainAccountId() == null || !sub.getMainAccountId().equals(mainId)) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "非本主账号的子账号, 禁止操作");
        }
        return sub;
    }

}
