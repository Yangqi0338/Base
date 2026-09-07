package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PermissionApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountAggRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountDetailRes;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
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
    private final MemberRepository memberRepository;
    private final EmpRepository empRepository;
    private final SupplierRepository supplierRepository;
    private final ChannelRepository channelRepository;


    @Override
    public Page<AccountAggRes> aggPage(AccountQuery query) {
        List<AccountEnum.Identity> identityList = query.getIdentityList();
        if (CollUtil.isEmpty(identityList)) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "身份不能为空");
        }
        // 跨端聚合: 不按 client 过滤 account, 只按 identityList 过滤
        query.setClient(null);

        Page<AccountVO> accountPage = accountRepository.accountPage(query);
        if (accountPage == null || CollectionUtils.isEmpty(accountPage.getRecords())) {
            return new Page<>();
        }
        List<Long> idList = CollUtil.map(accountPage.getRecords(), AccountVO::getId, true);

        Map<AccountEnum.Identity, Map<Long, Object>> identityMaps = loadIdentityMaps(identityList, idList);
        Map<Long, List<String>> roleMap = loadRoleCodes(accountPage.getRecords());

        return TransferUtils.transferPage(accountPage, accountVO -> assembleAgg(identityList, accountVO,
                identityMaps, roleMap.getOrDefault(accountVO.getId(), new ArrayList<>())));
    }

    @Override
    public AccountAggRes aggDetail(List<AccountEnum.Identity> identityList, Long accountId) {
        if (CollUtil.isEmpty(identityList) || accountId == null) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "身份与账号 ID 不能为空");
        }
        AccountVO accountVO = accountRepository.account(null, accountId);
        if (accountVO == null) {
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "账号不存在");
        }
        List<Long> idList = List.of(accountId);
        Map<AccountEnum.Identity, Map<Long, Object>> identityMaps = loadIdentityMaps(identityList, idList);
        Map<Long, List<String>> roleMap = loadRoleCodes(List.of(accountVO));
        return assembleAgg(identityList, accountVO, identityMaps,
                roleMap.getOrDefault(accountId, new ArrayList<>()));
    }

    /**
     * 按传入身份批量加载各身份表
     * <p>
     * 每个 identity 返回一张表, 内层 map 的 key 是 account id。身份表与 account 共用主键,
     * 故 map 的 key 就是 account id。PLATFORM / PARTNER / MMT_CHANNEL 无身份表, 对应内层 map 为空。
     * value 的运行时类型由 identity 唯一决定, 这条不变量由本方法保证, assembleAgg 据此强转。
     *
     * @param identityList 身份列表, 决定查哪几张身份表
     * @param idList       账号 ID 列表, 调用方保证非空
     * @return identity 到 (account id → 身份视图) 的映射
     */
    private Map<AccountEnum.Identity, Map<Long, Object>> loadIdentityMaps(
            List<AccountEnum.Identity> identityList, List<Long> idList) {
        Map<AccountEnum.Identity, Map<Long, Object>> maps = new HashMap<>();
        for (AccountEnum.Identity identity : identityList) {
            Map<Long, Object> m = new HashMap<>();
            switch (identity) {
                case MEMBER -> memberRepository.selectMemberByAccountIdList(idList)
                        .forEach(vo -> m.put(vo.getId(), vo));
                case EMP -> empRepository.listByIdList(idList)
                        .forEach(vo -> m.put(vo.getId(), vo));
                case SUPPLIER -> supplierRepository.listByIdList(idList)
                        .forEach(vo -> m.put(vo.getId(), vo));
                case CHANNEL -> channelRepository.listByIdList(idList)
                        .forEach(vo -> m.put(vo.getId(), vo));
                default -> {
                    // PLATFORM / PARTNER / MMT_CHANNEL 无身份表, 内层 map 保持空
                }
            }
            maps.put(identity, m);
        }
        return maps;
    }

    /**
     * 按账号各自 client 分组查角色编码列表
     *
     * <p>跨端分页下本页账号可能分属多个端, 角色关系 (ACCOUNT_ROLE) 按端隔离,
     * 故按 account.client 分组后逐端批量查, 再合并为 account id → 角色编码列表。</p>
     *
     * @param accountList 本页账号视图列表
     * @return account id 到角色编码列表的映射
     */
    private Map<Long, List<String>> loadRoleCodes(List<AccountVO> accountList) {
        Map<AccountEnum.Client, List<Long>> clientGroups = new HashMap<>();
        for (AccountVO vo : accountList) {
            if (vo.getClient() == null) {
                continue;
            }
            clientGroups.computeIfAbsent(vo.getClient(), k -> new ArrayList<>()).add(vo.getId());
        }
        Map<Long, List<String>> roleMap = new HashMap<>();
        clientGroups.forEach((client, ids) ->
                roleMap.putAll(permissionApi.findRoleCodeByAccountIdList(client, ids)));
        return roleMap;
    }

    /**
     * 解析账号身份 csv 为身份枚举列表
     *
     * @param csv 账号身份 csv (如 "1000,1001")
     * @return 身份枚举列表, 空串或无法识别时返回空列表
     */
    private List<AccountEnum.Identity> parseIdentityList(String csv) {
        if (StrUtil.isBlank(csv)) {
            return new ArrayList<>();
        }
        return StrUtil.split(csv, ",").stream()
                .map(code -> AccountEnum.Identity.getByCode(Long.valueOf(code)))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 装配单行账号聚合根
     * <p>
     * 账号主体字段由 {@code TransferUtils} 直接平铺映射 (AccountAggRes 继承 AccountRes), 仅 identityList
     * 需从 account.identityList csv 显式解析。identityVO 的运行时类型由 identity 唯一决定, 故槽内强转安全。
     *
     * @param identityList 本次查询传入的身份列表, 决定回填哪些槽
     * @param accountVO    账号视图, 非 null
     * @param identityMaps identity 到 (account id → 身份视图) 的映射
     * @param roleCodes    角色编码列表, 无角色传空列表
     * @return 聚合根, 永不为 null
     */
    private AccountAggRes assembleAgg(List<AccountEnum.Identity> identityList, AccountVO accountVO,
                                      Map<AccountEnum.Identity, Map<Long, Object>> identityMaps,
                                      List<String> roleCodes) {
        AccountAggRes agg = TransferUtils.transfer(accountVO, AccountAggRes.class, null,
                CopyOptions.create().setIgnoreProperties("identityList"));
        agg.setIdentityList(parseIdentityList(accountVO.getIdentityList()));
        agg.setRoleList(roleCodes);

        for (AccountEnum.Identity identity : identityList) {
            Object vo = identityMaps.getOrDefault(identity, Collections.emptyMap()).get(accountVO.getId());
            switch (identity) {
                case MEMBER -> agg.setMember((MemberVO) vo);
                case EMP -> agg.setEmp((EmpVO) vo);
                case SUPPLIER -> agg.setSupplier((SupplierVO) vo);
                case CHANNEL -> agg.setChannel((ChannelVO) vo);
                default -> {
                    // PLATFORM / PARTNER / MMT_CHANNEL 无身份表, 无槽可填
                }
            }
        }
        return agg;
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
