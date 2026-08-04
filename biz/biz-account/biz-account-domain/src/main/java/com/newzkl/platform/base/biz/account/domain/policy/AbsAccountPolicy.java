package com.newzkl.platform.base.biz.account.domain.policy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.model.support.VerificationCodeReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.RedisEnum;
import com.newzkl.platform.base.biz.account.model.enums.SmsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.AccountRes;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.tencent.ImCreateUserAccountObj;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
// TODO[cross-domain relation]: import relation.req.AccountLevelUpReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账号策略
 *
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
public abstract class AbsAccountPolicy extends AbsAccountPolicySupport {

    @Autowired
    protected AccountDomain accountDomain;
    @Autowired
    protected AccountAssembler accountAssembler;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected UserClientDomain userClientDomain;


    /**
     * 支持
     *
     * @return
     */
    public abstract CommonEnum.Client support();

    /**
     * 个人注册账号
     *
     * @param customSaveReq
     * @return
     */
    public abstract AccountRegisterRes customRegister(AccountCustomSaveReq customSaveReq);

    /**
     * 代理注册账号 (平台 or 其他账号)
     *
     * @param proxySaveReq 账号注册参数
     * @return
     */
    public abstract AccountRegisterRes proxyRegister(AccountProxySaveReq proxySaveReq);

    /**
     * 获取上级ID
     *
     * @param accountId
     * @return
     */
    public UpIdRes upId(Long accountId) {
        // 获取Redis 缓存
        String cacheKey = RedisEnum.Key.PID_LIST.getCode(accountId);
        String cacheRoleKey = RedisEnum.Key.PROLE_LIST.getCode(accountId);

        // 无缓存 请求数据库
        AccountVO account = accountDomain.account(null, accountId);

        return buildUpIdResByPidList(account.getPidList(), account.getPRoleList());
    }

    private UpIdRes buildUpIdResByPidList(String pidList, String pRoleList) {
        UpIdRes res = new UpIdRes();
        res.setUpId(StrUtil.split(pidList, ",").stream().filter(StrUtil::isNotBlank).map(NumberUtil::parseLong).collect(Collectors.toList()));
        res.setPRoleIdList(StrUtil.split(pRoleList, ";"));

        res.setOneId(CollUtil.getLast(res.getUpId()));

        // 获取分润用户第一级角色ID
        res.setDirectRoleId(UpIdRes.getLastEarningUserRoleId(res));
        return res;
    }

    /**
     * 角色申请审批通知
     *
     * @param auditEvent
     */
    public abstract void roleApplyAuditEvent(AuditEvent auditEvent);

    /**
     * 邀请别人成功
     *
     * @param account
     * @param inviteAccount
     */
    public abstract void inviteSuccess(AccountVO account, AccountRes inviteAccount, Object roleObj);

    /**
     * 建议验证码注册
     */
    protected AccountVO doRegisterAccount(String username, String code) {
        AccountSaveReq customSaveReq = new AccountSaveReq();
        customSaveReq.setUsername(username);
        customSaveReq.setCode(code);
        return doRegisterAccount(customSaveReq);
    }

    protected AccountVO doRegisterAccount(AccountSaveReq req) {
        if (StrUtil.isBlank(req.getUsername()) && StrUtil.isBlank(req.getPhone())) {
            throw new PlatformException(AccountErrorCode.REGISTER_CERTIFICATE_ERROR, "注册");
        }
        // 处理username和phone的数据同步关系
        String username = StrUtil.blankToDefault(req.getUsername(), req.getPhone());
        req.setUsername(username);

        if (PhoneUtil.isPhone(username) && StrUtil.isBlank(req.getPhone())) {
            req.setPhone(username);
        }

        AccountQuery query = new AccountQuery();
        query.setUsername(username);
        query.setClient(support());
        AccountVO targetAccount = accountRepository.account(query);

        // 移除掉用户侧注销且超过24的账号 (不在SQL做是因为效率问题)
        if (targetAccount != null && targetAccount.getState() == AccountEnum.State.DISABLE && targetAccount.getClient() == CommonEnum.Client.USER) {
            if (targetAccount.getCancelTime() == null || LocalDateTime.now().minusDays(1).isAfter(targetAccount.getCancelTime())) {
                targetAccount = null;
            }
        }

        // 如果有账号
        if (targetAccount != null) {
            if (targetAccount.getState() == AccountEnum.State.DISABLE) {
                // 停用状态自动启用
                log.info("{}对应的账号{}为停用状态，自动启用", username, targetAccount.getId());
                // login不做事务, 失败下次再登录即可
                targetAccount.setState(AccountEnum.State.ENABLE);
                accountRepository.accountEdit(targetAccount, null);
            } else if (targetAccount.getState() == AccountEnum.State.DESTROY) {
                // 禁用状态报错
                log.error("{}对应的账号{}已被平台禁用", username, targetAccount.getId());
                throw new PlatformException(AccountErrorCode.IN_BLOCKLIST, "注册");
            }
            targetAccount.setOld(true);
            return targetAccount;
        }

        // 短信验证
        if (StrUtil.isNotBlank(req.getCode())) {
            VerificationCodeReq codeReq = new VerificationCodeReq();
            codeReq.setPhone(req.getPhone());
            codeReq.setCode(req.getCode());
            codeReq.setType(SmsEnum.Type.Register);
            accountRepository.verificationCode(codeReq);
        } else if (StrUtil.isBlank(req.getPassword())) {
            throw new PlatformException(AccountErrorCode.CERTIFICATE_MISSING, "注册");
        }

        // 有邀请码但是没有邀请id，去数据库查
        String yqm = req.getYqm();
        if (StrUtil.isNotBlank(yqm) && req.getInviteId() == null) {
            AccountQuery yqmQuery = new AccountQuery();
            yqmQuery.setYqm(yqm);
            AccountVO account = accountRepository.account(yqmQuery);
            if (account == null) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            }
            log.info("邀请人信息：{}", JSONUtil.toJsonStr(account));
            req.setInviteId(account.getId());
            // 同客户端,
            if (req.getPid() == null && account.getClient() == support()) {
                req.setPid(account.getId());
            }
        }

        return accountDomain.customSave(req);
    }

    // TODO[cross-domain relation]: levelUp(AccountLevelUpReq, AccountRes) 依赖 relation.AccountLevelUpReq, 迁 biz-user 后恢复

    public abstract Class<?> getEntityClass();

    public void edit(Map<String, Object> columnMap, Long id) {

    }

    public void addColumn(List<EditColumnVO> columnList, Long id) {

    }

    public AccountVO detail(Long id) {
        return null;
    }

    public void remove(Long id) {

    }

    /**
     * 账号维度
     * 发送腾讯im用户创建消息
     */
    protected void sendTencentImMsg(AccountVO account) {
        ImCreateUserAccountObj imEntity = new ImCreateUserAccountObj().setUserAccount(account.getUserAccount())
                .setNickname(account.getNickname())
                .setHeadImg(account.getHead())
                .setPhone(account.getPhone());

        userClientDomain.sendTencentCreateUserMsg(imEntity);
    }
}
