package com.newzkl.platform.base.biz.account.domain.service.impl;

// TODO[infra-auth satoken]: import cn.dev33.satoken.stp.StpUtil; (登出属 auth 基础设施)

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.identity.MemberAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.dto.MemberDTO;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.MemberRes;
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
    private final MemberAssembler memberAssembler;

    private final AccountDomain accountDomain;

    @Override
    public Long memberSave(MemberReq memberCommand) {
        MemberVO item = TransferUtils.transfer(memberCommand, MemberVO::new,
                (c, v) -> v.setResidence(joinResidence(c)));
//        item.init();
//        item.setPid(memberCommand.getInviteId());
        return memberRepository.memberSave(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int memberEdit(Long id, MemberReq memberCommand) {
        // nickname / head 已随建模收敛到 account 表(member 表无该两列), 命中时先落账号侧
        if (StrUtil.isNotBlank(memberCommand.getNickname()) || StrUtil.isNotBlank(memberCommand.getHead())) {
            AccountReq accountReq = TransferUtils.transfer(memberCommand, AccountReq::new,
                    (source, target) -> {
                        target.setId(id);
                        target.setIdentity(AccountEnum.Identity.MEMBER);
                    });
            accountDomain.accountEdit(accountReq);
        }
        MemberVO item = TransferUtils.transfer(memberCommand, MemberVO::new, (c, v) -> {
            v.setId(id);
            v.setResidence(joinResidence(c));
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
    @Transactional(rollbackFor = Exception.class)
    public boolean bindMemberWx(Long accountId, String openId, String unionId) {
        if (accountId == null) {
            return false;
        }
        MemberVO item = new MemberVO();
        item.setId(accountId);
        item.setOpenId(openId);
        item.setUnionId(unionId);
        return memberRepository.memberEdit(item) > 0;
    }

    @Override
    public MemberVO member(Long memberId) {
        return memberRepository.member(memberId);
    }

    @Override
    public MemberDTO memberBase(Long memberId) {
        return memberAssembler.vo2DTO(this.loadMember(memberId));
    }

    @Override
    public MemberRes memberDetail(Long memberId) {
        MemberRes res = memberAssembler.vo2Res(this.loadMember(memberId));
        // 副数据: 账号侧展示字段。memberId 同时是账号ID, 账号缺失时 accountDomain 内部抛 NO_EXIST
        AccountVO account = accountDomain.account(AccountEnum.Client.USER, memberId);
        res.setUsername(account.getUsername());
        res.setRealName(account.getRealName());
        res.setNickname(account.getNickname());
        res.setHead(account.getHead());
        res.setPhone(account.getPhone());
        res.setYqm(account.getYqm());
        res.setAccountState(account.getState());
        res.setLastLoginTime(account.getLastLoginTime());
        res.setInviteId(account.getInviteAccountId());
        return res;
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

    /**
     * 按ID取会员主数据, 不存在直接抛
     *
     * @param memberId 会员账号ID
     * @return 会员视图
     */
    private MemberVO loadMember(Long memberId) {
        MemberVO member = memberRepository.member(memberId);
        if (member == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "会员");
        }
        return member;
    }

    /**
     * 三拆分常住地拼成单列
     *
     * <p>{@code member} 表只有 {@code residence} 一列, 入参仍保留省/市/区三字段以免前端改动。
     * 三者全空时返回 null, 交由 MyBatis-Plus 跳过该列, 保持部分更新语义</p>
     *
     * @param req 会员入参
     * @return 逗号分隔的常住地, 三者全空则 null
     */
    private String joinResidence(MemberReq req) {
        if (StrUtil.isAllBlank(req.getResidenceProvince(), req.getResidenceCity(), req.getResidenceDistrict())) {
            return null;
        }
        return StrUtil.join(",", StrUtil.nullToEmpty(req.getResidenceProvince()),
                StrUtil.nullToEmpty(req.getResidenceCity()),
                StrUtil.nullToEmpty(req.getResidenceDistrict()));
    }
}
