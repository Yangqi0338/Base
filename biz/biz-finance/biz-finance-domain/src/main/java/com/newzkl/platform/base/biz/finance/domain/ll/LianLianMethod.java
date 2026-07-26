package com.newzkl.platform.base.biz.finance.domain.ll;

import com.newzkl.platform.base.biz.finance.model.person.req.ApplyPasswordElementParams;
import com.newzkl.platform.base.biz.finance.model.person.req.BindMobileApplyReq;
import com.newzkl.platform.base.biz.finance.model.person.req.BindMobileCheckReq;
import com.newzkl.platform.base.biz.finance.model.person.req.CnapsQueryParams;
import com.newzkl.platform.base.biz.finance.model.person.req.EntOpenacctApplyParams;
import com.newzkl.platform.base.biz.finance.model.person.req.OpenacctApplyParams;
import com.newzkl.platform.base.biz.finance.model.person.req.PersonCheckReq;
import com.newzkl.platform.base.biz.finance.model.person.req.RandomPasswordReq;
import com.newzkl.platform.base.biz.finance.model.person.req.TripartiteBaseParam;
import com.newzkl.platform.base.biz.finance.model.person.req.UploadParams;
import com.newzkl.platform.base.biz.finance.model.person.res.ApplyPasswordElementResult;
import com.newzkl.platform.base.biz.finance.model.person.res.CnapsQueryResult;
import com.newzkl.platform.base.biz.finance.model.person.res.EntOpenacctApplyResult;
import com.newzkl.platform.base.biz.finance.model.person.res.GetRandomResult;
import com.newzkl.platform.base.biz.finance.model.person.res.OpenacctApplyResult;
import com.newzkl.platform.base.biz.finance.model.person.res.OpenacctVerifyResult;
import com.newzkl.platform.base.biz.finance.model.person.res.UploadResult;
import com.newzkl.platform.base.biz.finance.model.person.res.VerifyCodeResult;
import com.newzkl.platform.base.biz.finance.model.support.FinanceProperties.LianLianProperties;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 连连 (LianLian) 静态防腐层。
 *
 * <p>迁移自 new-scm {@code TripartitePayMethod} 的连连相关方法。旧类混合了连连与汇付两侧调用,
 * 汇付部分已由 {@link com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod} 覆盖, 此处不重复迁移。</p>
 *
 * <p>职责: 补齐公共参数 (timestamp / oid_partner / user_id) 后转调 {@link LianLianApi};
 * 加签与请求头由 {@link LianLianInterceptor} 承担。</p>
 *
 * <p>返回码不在此处校验: 旧 {@code PersonPayController} 依赖读取 {@code ret_code} 做业务分支,
 * 保持由调用方判定 (见 {@code LianLianBaseRes#isSuccess()})。</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class LianLianMethod {

    /**
     * 连连时间戳格式。
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Forest 客户端 (由 Spring 注入到静态字段, 供静态方法调用)。
     */
    private static LianLianApi api;

    /**
     * 注入 Forest 客户端。
     *
     * @param lianLianApi 连连 Forest 客户端
     */
    @Autowired
    public void setLianLianApi(LianLianApi lianLianApi) {
        LianLianMethod.api = lianLianApi;
    }

    /**
     * 绑定手机号申请 (下发验证码)。
     *
     * @param req 绑定手机号申请请求
     * @return 验证码申请结果
     */
    public static VerifyCodeResult bindMobileApply(BindMobileApplyReq req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.bindMobileApply(req);
    }

    /**
     * 绑定手机号验证。
     *
     * @param req 绑定手机号验证请求
     * @return 验证结果
     */
    public static VerifyCodeResult bindMobileCheck(BindMobileCheckReq req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.bindMobileCheck(req);
    }

    /**
     * 个人开户申请。
     *
     * @param req 个人开户申请请求
     * @return 开户申请结果
     */
    public static OpenacctApplyResult personApply(OpenacctApplyParams req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.personApply(req);
    }

    /**
     * 企业开户申请。
     *
     * @param req 企业开户申请请求
     * @return 开户申请结果
     */
    public static EntOpenacctApplyResult entPersonApply(EntOpenacctApplyParams req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.entPersonApply(req);
    }

    /**
     * 个人开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    public static OpenacctVerifyResult personCheck(PersonCheckReq req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.personCheck(req);
    }

    /**
     * 企业开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    public static OpenacctVerifyResult entCheck(PersonCheckReq req) {
        paddingBaseParam(req);
        req.setUser_id(currentUserId());
        return api.entCheck(req);
    }

    /**
     * 随机密码因子获取。
     *
     * @param req 随机密码因子请求
     * @return 随机因子结果
     */
    public static GetRandomResult randomPassword(RandomPasswordReq req) {
        paddingBaseParam(req);
        return api.randomPassword(req);
    }

    /**
     * 大额行号查询。
     *
     * @param req 大额行号查询请求
     * @return 支行列表
     */
    public static CnapsQueryResult codeQuery(CnapsQueryParams req) {
        paddingBaseParam(req);
        return api.codeQuery(req);
    }

    /**
     * 申请密码控件 Token。
     *
     * @param req    密码控件申请请求
     * @param userId 指定用户号; 为空时取当前登录账号 (平台打款场景需回填收款方 ID)
     * @return 密码控件 Token 结果
     */
    public static ApplyPasswordElementResult applyPasswordElement(ApplyPasswordElementParams req, String userId) {
        paddingBaseParam(req);
        req.setUser_id(userId == null ? currentUserId() : userId);
        return api.applyPasswordElement(req);
    }

    /**
     * 文件上传。
     *
     * @param req 文件上传请求
     * @return 文件上传结果
     */
    public static UploadResult uploadFile(UploadParams req) {
        paddingBaseParam(req);
        return api.uploadFile(req);
    }

    /**
     * 补齐连连公共请求参数。
     *
     * @param param 连连请求基类
     */
    private static void paddingBaseParam(TripartiteBaseParam param) {
        param.setTimestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        param.setOid_partner(LianLianProperties.oidPartner);
    }

    /**
     * 当前登录账号 ID 字符串形式 (连连 user_id)。
     *
     * @return 账号 ID 字符串
     */
    private static String currentUserId() {
        return String.valueOf(SecurityUtils.getAccountId());
    }
}
