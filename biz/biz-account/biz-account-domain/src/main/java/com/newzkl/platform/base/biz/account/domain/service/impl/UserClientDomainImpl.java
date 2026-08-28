package com.newzkl.platform.base.biz.account.domain.service.impl;

// TODO[infra-auth satoken]: import cn.dev33.satoken.stp.StpUtil; (登出属 auth 基础设施)

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberImportExcelVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.redis.model.req.VerificationCodeReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserClientDomainImpl implements UserClientDomain {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    private final AccountAssembler accountAssembler;

    private final com.newzkl.platform.base.biz.account.domain.service.AccountDomain accountDomain;


    @Override
    public Long memberSave(MemberReq memberCommand) {
        MemberVO item = TransferUtils.transfer(memberCommand, MemberVO::new);
//        item.init();
//        item.setPid(memberCommand.getInviteId());
        return memberRepository.memberSave(item);
    }

    @Override
    public int memberEdit(Long id, MemberReq memberCommand) {
        MemberVO item = TransferUtils.transfer(memberCommand, MemberVO::new, (c, v) -> {
            v.setId(id);
        });
        return memberRepository.memberEdit(item);
    }

    @Override
    public int memberDelete(List<Long> memberIdList) {
        return memberRepository.memberDelete(memberIdList);
    }

    @Override
    public void memberEdit(List<EditColumnVO> editColumnList, Long id) {
        memberRepository.memberEdit(editColumnList, id);
    }

    @Override
    public MemberVO member(Long memberId) {
        return memberRepository.member(memberId);
    }

    @Override
    public void cancelAccount(Long accountId, CancelMemberReq req) {
        log.info("注销账号， 入参: {}", req);
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setClient(AccountEnum.Client.USER);
        accountQuery.setId(accountId);
        AccountVO account = accountRepository.account(accountQuery);
        if (Objects.isNull(account)) {
            log.error("注销用户失败：用户不存在，accountId: {}", accountId);
            ThrowsException.exception(AccountErrorCode.PARAM_ERROR, "用户不存在");
        }

        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(account.getPhone());
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.IM_DEL);
        accountRepository.verificationCode(codeReq);

        account.setState(AccountEnum.State.DESTROY);
        account.setCancelTime(LocalDateTime.now());
        accountRepository.accountEdit(account, null);

        // TODO[infra-auth satoken]: StpUtil.logout();
        log.info("注销c端用户完成，accountId: {}", accountId);
    }


    @Override
    public void updateMemberInfo(Long accountId, UpdateMemberInfoCommand command) {
        log.info("开始更新用户信息，accountId: {}, 入参: {}", accountId, command);

        // 基础参数校验：无更新字段直接抛异常
        if (StrUtil.isAllBlank(command.getNickname(), command.getHeadImg(), command.getNewPhone(), command.getNewPassword(), command.getOldPassword())) {
            log.error("更新用户信息失败：无有效更新字段，accountId: {}", accountId);
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "请至少填写一项更新内容");
        }

        //  查询并校验用户信息
        AccountVO account = accountRepository.account(SecurityUtils.getClient(), accountId);
        if (Objects.isNull(account)) {
            log.error("更新用户信息失败：账号不存在，accountId: {}", accountId);
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "账号不存在");
        }
//        MemberVO member = memberRepository.validByAccountId(accountId);
//        if (Objects.isNull(member)) {
//            log.error("更新用户信息失败：用户不存在，accountId: {}", accountId);
//            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户不存在");
//        }

        // 验证码校验：传手机号则必须传验证码，且验证合法性
        if (StrUtil.isNotBlank(command.getPhone())) {
            if (StrUtil.isBlank(command.getCode())) {
                log.error("更新用户信息失败：传手机号但未传验证码，accountId: {}, phone: {}", accountId, command.getPhone());
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "验证码不能为空");
            }
            // 校验验证码有效性
            VerificationCodeReq codeReq = new VerificationCodeReq();
            codeReq.setPhone(command.getPhone());
            codeReq.setCode(command.getCode());
            codeReq.setType(SmsEnum.Type.UpdatePassword);
            accountRepository.verificationCode(codeReq);
            // 校验验证码手机号与绑定手机号一致
            if (!account.getPhone().equals(command.getPhone())) {
                log.error("更新用户信息失败：验证码手机号与绑定手机号不一致，accountId: {}, 绑定手机号: {}, 验证码手机号: {}",
                        accountId, account.getPhone(), command.getPhone());
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "验证码手机号和已绑定手机号不一致！");
            }
        }
        AccountEnum.Identity MEMBER_ROLE_CODE = AccountEnum.Identity.MEMBER;
        BCryptPasswordEncoder PWD_ENCODER = new BCryptPasswordEncoder();
        // 事务包裹核心更新逻辑
//        transactionUtils.executeWithoutResult(() -> {
            boolean isMemberUpdated = false;
            boolean isAccountUpdated = false;

            if (StrUtil.isNotBlank(command.getNickname())) {
//                member.setNickname(command.getNickname());
                account.setNickname(command.getNickname());
                isMemberUpdated = true;
                isAccountUpdated = true;
            }
            if (StrUtil.isNotBlank(command.getHeadImg())) {
//                member.setHead(command.getHeadImg());
                account.setHead(command.getHeadImg());
                isMemberUpdated = true;
                isAccountUpdated = true;
            }
            if (StrUtil.isNotBlank(command.getNewPhone())) {
                account.setPhone(command.getNewPhone());
                isAccountUpdated = true;
            }

            if (StrUtil.isNotBlank(command.getNewPassword())) {

//                List<String> oldRoleIdList = findSameClientOldRoleId(account.getRoleIdList(), MEMBER_ROLE_CODE);

                //  首次设置密码（无旧密码）
                if (StrUtil.isBlank(command.getOldPassword())) {
                    String newPassword = account.getNewPassword(command.getNewPassword());
                    account.setPassword(newPassword);
                    isAccountUpdated = true;
                } else {
                    // 修改密码（有旧密码）
//                    RoleEnum.CompanyRole companyRole = findRole(account);
//                    if (Objects.isNull(companyRole)) {
//                        throw new PlatformException(AccountErrorCode.NO_EXIST);
//                    }
                    // 旧密码校验
                    boolean checkPassword = SecurityUtils.matchesPassword(command.getOldPassword(), account.getPassword());
                    if (!checkPassword) {
                        throw new PlatformException(AccountErrorCode.PASSWORD);
                    }
                    // 更新密码
                    String newPassword = account.getNewPassword(command.getNewPassword());
                    account.setPassword(newPassword);
                    isAccountUpdated = true;
                }
            }

            if (isMemberUpdated) {
//                memberRepository.memberEdit(member);
            }
            if (isAccountUpdated) {
                accountRepository.accountEdit(account, null);
            }
            log.info("用户信息更新事务执行成功，accountId: {}, member更新: {}, account更新: {}",
                    accountId, isMemberUpdated, isAccountUpdated);
        log.info("用户信息更新完成，accountId: {}", accountId);
    }

    @Override
    public IdentityRegisterRes adminCreateMember(AdminRegisterIdentityReq req) {
        AccountEnum.Identity identity = AccountEnum.Identity.MEMBER;

        // 落 account: 登录账号缺省用手机号, 拿回自增 id
        com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq rpcReq =
                new com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq();
        rpcReq.setIdentity(identity);
        rpcReq.setClient(identity.getClient());
        rpcReq.setUsername(StrUtil.blankToDefault(req.getUsername(), req.getPhone()));
        rpcReq.setPhone(req.getPhone());
        rpcReq.setNickname(req.getNickname());
        rpcReq.setHead(req.getHead());
        rpcReq.setPid(req.getPid());
        AccountVO account = accountDomain.register(rpcReq);

        // 角色初始化: 组装身份初始化参数并分发到 MEMBER 角色策略
        IdentityCustomSaveReq saveReq = new IdentityCustomSaveReq();
        saveReq.setId(account.getId());
        saveReq.setNickname(req.getNickname());
        saveReq.setHead(req.getHead());
        saveReq.setRoleIdList(req.getRoleIdList());
        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(identity)
                .customRegister(saveReq);

        // 注册失败则抛出异常
        if (registerRes.getErrorCode() != null) {
            throw new PlatformException(registerRes.getErrorCode());
        }

        registerRes.setId(account.getId());
        return registerRes;
    }

    @Override
    public EasyExcelErrorVO adminImportAccount(MultipartFile file) {
        Set<String> phoneSet = new HashSet<>();
        try (InputStream inputStream = file.getInputStream()) {
            return EasyExcelUtil.importBiz(inputStream, MemberImportExcelVO.class,
                    (data, rowNum) -> {
                        String phone = StrUtil.trimToNull(data.getPhone());
                        data.setPhone(phone);
                        if (!phoneSet.add(phone)) {
                            return String.format("第%s行：手机号【%s】重复，跳过导入", rowNum, phone);
                        }
                        return null;
                    },
                    list -> {
                        for (MemberImportExcelVO po : list) {
                            AdminRegisterIdentityReq req = new AdminRegisterIdentityReq();
                            req.setPhone(po.getPhone());
                            req.setNickname(StrUtil.blankToDefault(StrUtil.trim(po.getNickname()), po.getPhone()));
                            req.setState(AccountEnum.State.ENABLE);
                            this.adminCreateMember(req);
                        }
                    }, new EasyExcelUtil.ImportParam().setHeadRowNum(1));
        } catch (Exception e) {
            log.error("会员批量导入异常", e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recycleCanceledMember() {
        AccountQuery query = new AccountQuery();
        LocalDateTime expireBefore = LocalDateTime.now().minusDays(1);
        query.setState(AccountEnum.State.DESTROY);
        query.setCancelTimeBefore(expireBefore);
        List<AccountVO> accountList = accountRepository.accountList(query);
        int size = accountList.size();
        if (size == 0) {
            return 0;
        }
        // 物理删前按注销时保留的 identityList 逐身份清理: 删身份实体 + 解绑角色
        for (AccountVO account : accountList) {
            for (String code : StrUtil.split(account.getIdentityList(), ',', true, true)) {
                AbsIdentityPolicy policy = AbsIdentityPolicySupport.getPolicy(Long.valueOf(code));
                if (policy != null) {
                    policy.destroy(account, null);
                }
            }
        }
        List<Long> idList = accountList.stream().map(AccountVO::getId).toList();
        log.info("回收已注销用户账号{}", idList);
        accountRepository.accountDelete(idList);
        log.info("回收已注销用户账号完成，过期界限: {}, 删除行数: {}", expireBefore, size);
        return size;
    }
}
