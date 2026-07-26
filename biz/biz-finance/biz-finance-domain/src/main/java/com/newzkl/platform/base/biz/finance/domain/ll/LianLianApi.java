package com.newzkl.platform.base.biz.finance.domain.ll;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.BindingVar;
import com.dtflys.forest.annotation.ForestClient;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.LogEnabled;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Retry;
import com.dtflys.forest.reflection.ForestMethod;
import com.newzkl.platform.base.biz.finance.model.person.req.ApplyPasswordElementParams;
import com.newzkl.platform.base.biz.finance.model.person.req.BindMobileApplyReq;
import com.newzkl.platform.base.biz.finance.model.person.req.BindMobileCheckReq;
import com.newzkl.platform.base.biz.finance.model.person.req.CnapsQueryParams;
import com.newzkl.platform.base.biz.finance.model.person.req.EntOpenacctApplyParams;
import com.newzkl.platform.base.biz.finance.model.person.req.OpenacctApplyParams;
import com.newzkl.platform.base.biz.finance.model.person.req.PersonCheckReq;
import com.newzkl.platform.base.biz.finance.model.person.req.RandomPasswordReq;
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
import org.springframework.stereotype.Component;

/**
 * 连连 (LianLian) 纯净 API 接口 (Forest)。
 *
 * <p>迁移自 new-scm {@code TripartitePayMethod} 的手写 {@code HttpClientUtils} 调用,
 * 改为与已迁 {@code HuiFuApi} 同构的 Forest 声明式客户端: 加签、公共参数补齐、
 * 返回码校验统一由 {@link LianLianInterceptor} 承担。</p>
 *
 * <p>连连有三个网关前缀 (主接口 / 网关 / 文件), 故按端点分别用绝对 URL 覆盖 baseURL。</p>
 *
 * @author KC
 */
@ForestClient
@Component
@BaseRequest(baseURL = "${lianLianApiUrl}", interceptor = LianLianInterceptor.class)
@Retry(maxRetryCount = "0", maxRetryInterval = "10")
@LogEnabled(false)
public interface LianLianApi {

    /**
     * 绑定 ACCP 主接口前缀变量。
     *
     * @param method Forest 方法上下文
     * @return 主接口前缀
     */
    @BindingVar("lianLianApiUrl")
    default String getApiUrl(ForestMethod<?> method) {
        return LianLianProperties.apiUrl;
    }

    /**
     * 绑定 ACCP 网关前缀变量。
     *
     * @param method Forest 方法上下文
     * @return 网关前缀
     */
    @BindingVar("lianLianGwUrl")
    default String getGwUrl(ForestMethod<?> method) {
        return LianLianProperties.gwUrl;
    }

    /**
     * 绑定 ACCP 文件服务前缀变量。
     *
     * @param method Forest 方法上下文
     * @return 文件服务前缀
     */
    @BindingVar("lianLianFileUrl")
    default String getFileUrl(ForestMethod<?> method) {
        return LianLianProperties.fileUrl;
    }

    /**
     * 绑定手机号申请 (下发验证码)。
     *
     * @param req 绑定手机号申请请求
     * @return 验证码申请结果
     */
    @Post(url = "/acctmgr/regphone-verifycode-apply")
    VerifyCodeResult bindMobileApply(@JSONBody BindMobileApplyReq req);

    /**
     * 绑定手机号验证。
     *
     * @param req 绑定手机号验证请求
     * @return 验证结果
     */
    @Post(url = "/acctmgr/regphone-verifycode-verify")
    VerifyCodeResult bindMobileCheck(@JSONBody BindMobileCheckReq req);

    /**
     * 个人用户开户申请。
     *
     * @param req 个人开户申请请求
     * @return 开户申请结果
     */
    @Post(url = "/acctmgr/openacct-apply-individual")
    OpenacctApplyResult personApply(@JSONBody OpenacctApplyParams req);

    /**
     * 企业用户开户申请。
     *
     * @param req 企业开户申请请求
     * @return 开户申请结果
     */
    @Post(url = "/acctmgr/openacct-apply-enterprise")
    EntOpenacctApplyResult entPersonApply(@JSONBody EntOpenacctApplyParams req);

    /**
     * 个人用户开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    @Post(url = "/acctmgr/openacct-verify-individual")
    OpenacctVerifyResult personCheck(@JSONBody PersonCheckReq req);

    /**
     * 企业用户开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    @Post(url = "/acctmgr/openacct-verify-enterprise")
    OpenacctVerifyResult entCheck(@JSONBody PersonCheckReq req);

    /**
     * 随机密码因子获取。
     *
     * @param req 随机密码因子请求
     * @return 随机因子结果
     */
    @Post(url = "/acctmgr/get-random")
    GetRandomResult randomPassword(@JSONBody RandomPasswordReq req);

    /**
     * 大额行号查询。
     *
     * @param req 大额行号查询请求
     * @return 支行列表
     */
    @Post(url = "/acctmgr/query-cnapscode")
    CnapsQueryResult codeQuery(@JSONBody CnapsQueryParams req);

    /**
     * 申请密码控件 Token (走 ACCP 网关域名)。
     *
     * @param req 密码控件申请请求
     * @return 密码控件 Token 结果
     */
    @Post(url = "${lianLianGwUrl}/acctmgr/apply-password-element")
    ApplyPasswordElementResult applyPasswordElement(@JSONBody ApplyPasswordElementParams req);

    /**
     * 文件上传 (走 ACCP 文件服务域名)。
     *
     * @param req 文件上传请求
     * @return 文件上传结果
     */
    @Post(url = "${lianLianFileUrl}/documents/upload")
    UploadResult uploadFile(@JSONBody UploadParams req);
}
