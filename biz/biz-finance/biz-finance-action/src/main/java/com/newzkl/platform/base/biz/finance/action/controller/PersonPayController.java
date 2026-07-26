package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.ll.LianLianMethod;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PayeeInfoDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;
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
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.biz.finance.model.support.FinanceProperties.LianLianProperties;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连连 (LianLian) 前端交互控制器。
 *
 * <p>迁移自 new-scm {@code interfaces.person.PersonPayController}。</p>
 *
 * <p>迁移调整:</p>
 * <ul>
 *   <li>三方调用由手写 {@code HttpClientUtils} 改为 {@link LianLianMethod} + Forest 声明式客户端;</li>
 *   <li>回调地址、商户号由 {@link LianLianProperties} (Nacos) 注入, 不再硬编码;</li>
 *   <li>{@code sun.misc.BASE64Encoder} 替换为 {@link Base64} (见 {@link #getImagBase64(MultipartFile)});</li>
 *   <li>HTTP 代理配置 ({@code HttpProxyProperties}) 未随之迁入 Base, 图片直连读取。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonPayController {

    /**
     * 三方账户领域服务。
     */
    private final TripartitePurseDomain purse;

    /**
     * 收款方配置领域服务。
     */
    private final PayeeInfoDomain payeeInfoDomain;

    /**
     * 连连交易时间格式。
     */
    private static final DateTimeFormatter TXN_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 平台打款收款方配置的消费类型 (旧代码写死 3 = 甄选师转出出账)。
     */
    private static final Integer PAYEE_CONSUME_TYPE_ROLL_OUT = 3;

    /**
     * 读取图片流的缓冲区大小。
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * 图片链接读取超时 (毫秒)。
     */
    private static final int LINK_READ_TIMEOUT = 3000;

    /**
     * 绑定手机号申请 (下发验证码)。
     *
     * <p>TODO[auth-defer]: 旧代码有 {@code @Limit(code = FuncCons.finance, level = set)} 权限拦截,
     * 对应切面尚未迁入 Base, 暂以注释记录, 待权限模块补齐后恢复。</p>
     *
     * @param req 绑定手机号申请请求
     * @return 验证码申请结果
     */
    @PostMapping("/bindMobileApply")
    public ScmResult<VerifyCodeResult> bindMobileApply(@RequestBody BindMobileApplyReq req) {
        AccountTripartitePurseVO tripartitePurse = purse.queryAccountTripartitePurse(SecurityUtils.getAccountId());
        if (tripartitePurse != null && PurseEnum.TripartitePurchaseStatus.NORMAL == tripartitePurse.getUserStatus()) {
            return ScmResult.fail(BaseErrorCode.EXIST_DATA.getMessage());
        }
        return ScmResult.success(LianLianMethod.bindMobileApply(req));
    }

    /**
     * 绑定手机号验证。
     *
     * @param req 绑定手机号验证请求
     * @return 验证结果
     */
    @PostMapping("/bindMobileCheck")
    public ScmResult<VerifyCodeResult> bindMobileCheck(@RequestBody BindMobileCheckReq req) {
        return ScmResult.success(LianLianMethod.bindMobileCheck(req));
    }

    /**
     * 个人开户申请。
     *
     * @param req 个人开户申请请求
     * @return 开户申请结果
     */
    @PostMapping("/personApply")
    public ScmResult<OpenacctApplyResult> personApply(@RequestBody OpenacctApplyParams req) {
        req.setTxn_seqno(String.valueOf(SnowflakeIdAble.getSnowflakeId()));
        req.setTxn_time(LocalDateTime.now().format(TXN_TIME_FORMATTER));
        req.setNotify_url(LianLianProperties.notifyUrl);
        OpenacctApplyResult result = LianLianMethod.personApply(req);
        if (!result.isSuccess()) {
            return ScmResult.success(result);
        }
        AccountTripartitePurseVO tripartitePurse = new AccountTripartitePurseVO();
        tripartitePurse.setAccountId(SecurityUtils.getAccountId());
        tripartitePurse.setAccountName(req.getBasicInfo().getUser_name());
        tripartitePurse.setBankNo(req.getLinkedAcctInfo().getLinked_acctno());
        tripartitePurse.setCommitInfo(JSONUtil.toJsonStr(req));
        purse.addAccountTripartitePurse(tripartitePurse);
        return ScmResult.success(result);
    }

    /**
     * 企业开户申请。
     *
     * @param req 企业开户申请请求
     * @return 开户申请结果
     */
    @PostMapping("/entPersonApply")
    public ScmResult<EntOpenacctApplyResult> entPersonApply(@RequestBody EntOpenacctApplyParams req) {
        req.setTxn_seqno(String.valueOf(SnowflakeIdAble.getSnowflakeId()));
        req.setTxn_time(LocalDateTime.now().format(TXN_TIME_FORMATTER));
        req.setNotify_url(LianLianProperties.notifyUrl);
        EntOpenacctApplyResult result = LianLianMethod.entPersonApply(req);
        AccountTripartitePurseVO tripartitePurse = new AccountTripartitePurseVO();
        tripartitePurse.setAccountId(SecurityUtils.getAccountId());
        tripartitePurse.setAccountName(req.getBasicInfo().getUser_name());
        tripartitePurse.setBankNo(req.getLinkedAcctInfo().getLinked_acctno());
        tripartitePurse.setUserStatus(parseUserStatus(result.getUser_status()));
        tripartitePurse.setCommitInfo(JSONUtil.toJsonStr(req));
        purse.addAccountTripartitePurse(tripartitePurse);
        if (!result.isSuccess()) {
            log.error("企业开户申请失败, result: {}, req: {}", JSONUtil.toJsonStr(result), JSONUtil.toJsonStr(req));
            return ScmResult.fail(result.getRet_msg());
        }
        return ScmResult.success(result);
    }

    /**
     * 大额行号查询。
     *
     * @param req 大额行号查询请求
     * @return 支行列表
     */
    @PostMapping("/codeQuery")
    public ScmResult<CnapsQueryResult> codeQuery(@RequestBody CnapsQueryParams req) {
        return ScmResult.success(LianLianMethod.codeQuery(req));
    }

    /**
     * 个人开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    @PostMapping("/personCheck")
    public ScmResult<OpenacctVerifyResult> personCheck(@RequestBody PersonCheckReq req) {
        return ScmResult.success(LianLianMethod.personCheck(req));
    }

    /**
     * 企业开户验证。
     *
     * @param req 开户验证请求
     * @return 开户验证结果
     */
    @PostMapping("/entCheck")
    public ScmResult<OpenacctVerifyResult> entCheck(@RequestBody PersonCheckReq req) {
        return ScmResult.success(LianLianMethod.entCheck(req));
    }

    /**
     * 随机密码因子获取。
     *
     * @param req    随机密码因子请求
     * @param client 客户端: 1 = APP 客户; 其他 = 平台打款
     * @return 随机因子结果
     */
    @PostMapping("/randomPassword/{client}")
    public ScmResult<GetRandomResult> randomPassword(@RequestBody RandomPasswordReq req, @PathVariable Integer client) {
        if (Integer.valueOf(1).equals(client)) {
            req.setUser_id(String.valueOf(SecurityUtils.getAccountId()));
        } else {
            req.setEncrypt_algorithm("SM2");
            req.setUser_id(platformPayeeId());
        }
        return ScmResult.success(LianLianMethod.randomPassword(req));
    }

    /**
     * 申请密码控件 Token。
     *
     * @param req 密码控件申请请求
     * @return 密码控件 Token 结果
     */
    @PostMapping("/applyPasswordElement")
    public ScmResult<ApplyPasswordElementResult> applyPasswordElement(@RequestBody ApplyPasswordElementParams req) {
        return ScmResult.success(LianLianMethod.applyPasswordElement(req, null));
    }

    /**
     * 平台打款场景申请密码控件 Token (用户号取平台收款方配置)。
     *
     * @param req 密码控件申请请求
     * @return 密码控件 Token 结果
     */
    @PostMapping("/applyPasswordElementByRollOut")
    public ScmResult<ApplyPasswordElementResult> applyPasswordElementByRollOut(
            @RequestBody ApplyPasswordElementParams req) {
        String payeeId = platformPayeeId();
        ApplyPasswordElementResult result = LianLianMethod.applyPasswordElement(req, payeeId);
        result.setUserId(payeeId);
        return ScmResult.success(result);
    }

    /**
     * 文件上传 (表单文件)。
     *
     * @param file 上传文件
     * @return 文件上传结果
     * @throws IOException 读取文件流失败
     */
    @PostMapping("/uploadFile")
    public ScmResult<UploadResult> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileType = originalFilename == null ? null
                : originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        UploadParams uploadParams = buildUploadParams(fileType, getImagBase64(file));
        return ScmResult.success(LianLianMethod.uploadFile(uploadParams));
    }

    /**
     * 文件上传 (图片外链)。
     *
     * @param link 图片地址
     * @return 文件上传结果
     * @throws IOException 读取图片流失败
     */
    @GetMapping("/uploadFileByLink")
    public ScmResult<UploadResult> uploadFileByLink(@RequestParam("link") String link) throws IOException {
        Map<String, String> image = getImagBase64(link);
        UploadParams uploadParams = buildUploadParams(image.get("type"), image.get("code"));
        return ScmResult.success(LianLianMethod.uploadFile(uploadParams));
    }

    /**
     * 查询当前客户的连连账户。
     *
     * @return 三方账户
     */
    @PostMapping("/queryAccountTripartitePurse")
    public ScmResult<AccountTripartitePurseVO> queryAccountTripartitePurse() {
        return ScmResult.success(purse.queryAccountTripartitePurse(SecurityUtils.getAccountId()));
    }

    /**
     * 查询当前客户提交的企业资料信息。
     *
     * @return 企业开户申请资料
     */
    @PostMapping("/queryCommitInfo")
    public ScmResult<EntOpenacctApplyParams> queryCommitInfo() {
        return ScmResult.success(parseCommitInfo(purse.queryCommitInfo(SecurityUtils.getAccountId())));
    }

    /**
     * 平台查询指定客户提交的企业资料信息。
     *
     * @param accountId 客户ID
     * @return 企业开户申请资料
     */
    @PostMapping("/platformQueryCommitInfo/{accountId}")
    public ScmResult<EntOpenacctApplyParams> platformQueryCommitInfo(@PathVariable Long accountId) {
        return ScmResult.success(parseCommitInfo(purse.queryCommitInfo(accountId)));
    }

    /**
     * 构建连连文件上传请求。
     *
     * @param fileType    文件类型
     * @param fileContext 文件 Base64 内容
     * @return 上传请求
     */
    private UploadParams buildUploadParams(String fileType, String fileContext) {
        UploadParams uploadParams = new UploadParams();
        uploadParams.setTxn_seqno(String.valueOf(SnowflakeIdAble.getSnowflakeId()));
        uploadParams.setTxn_time(LocalDateTime.now().format(TXN_TIME_FORMATTER));
        uploadParams.setFile_type(fileType);
        uploadParams.setContext_type("UBO_IMAGE");
        uploadParams.setFile_context(fileContext);
        return uploadParams;
    }

    /**
     * 解析已提交的企业资料 JSON。
     *
     * @param commitInfo 资料 JSON, 可为空
     * @return 企业开户申请资料, 无资料时返回 null
     */
    private EntOpenacctApplyParams parseCommitInfo(String commitInfo) {
        if (commitInfo == null) {
            return null;
        }
        return JSONUtil.toBean(commitInfo, EntOpenacctApplyParams.class);
    }

    /**
     * 取平台打款收款方 ID。
     *
     * @return 收款方 ID
     */
    private String platformPayeeId() {
        List<PayeeInfoVO> payeeInfos = payeeInfoDomain.queryPayeeInfo(PAYEE_CONSUME_TYPE_ROLL_OUT);
        if (payeeInfos == null || payeeInfos.isEmpty()) {
            throw new IllegalStateException("平台打款收款方配置缺失, consumeType=" + PAYEE_CONSUME_TYPE_ROLL_OUT);
        }
        return payeeInfos.get(0).getPayeeId();
    }

    /**
     * 连连账户状态字符串转枚举。
     *
     * @param userStatus 连连返回的账户状态
     * @return 账户状态枚举, 无法识别时返回 null
     */
    private PurseEnum.TripartitePurchaseStatus parseUserStatus(String userStatus) {
        if (userStatus == null) {
            return null;
        }
        for (PurseEnum.TripartitePurchaseStatus status : PurseEnum.TripartitePurchaseStatus.values()) {
            if (status.getValue().equals(userStatus)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 将上传文件转为 Base64 字符串 (不带 {@code data:image/png;base64} 前缀)。
     *
     * <p><b>Base64 决策</b>: 旧实现用 {@code sun.misc.BASE64Encoder.encode()}, 其每 76 字符插入换行,
     * 语义等价 {@code Base64.getMimeEncoder()}。此处<b>刻意改用 {@link Base64#getEncoder()} (无换行)</b>:
     * 该结果作为 JSON 字符串值 {@code file_context} 传输, 换行会被 JSON 转义成 {@code \r\n} 占位并参与
     * RSA 加签, 属旧代码遗留缺陷; 连连按标准 Base64 解码, 无换行是正确形态。</p>
     *
     * @param file 上传文件
     * @return Base64 字符串
     * @throws IOException 读取文件流失败
     */
    public static String getImagBase64(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return Base64.getEncoder().encodeToString(readAll(inputStream));
        }
    }

    /**
     * 读取图片外链并转为 Base64 字符串。
     *
     * <p><b>Base64 决策</b>: 同 {@link #getImagBase64(MultipartFile)}, 使用无换行的
     * {@link Base64#getEncoder()} 而非 MIME 分块编码。</p>
     *
     * <p>旧实现依赖 {@code HttpProxyProperties} 走可选 HTTP 代理; 该配置类未随迁移进入 Base,
     * 此处直连读取。若后续需要代理, 应在通用层补 {@code HttpUtil} 代理支持而非在控制器内实现。</p>
     *
     * @param imgLink 图片地址
     * @return 含 {@code type} (内容类型去掉 image/ 前缀) 与 {@code code} (Base64 内容) 的映射
     * @throws IOException 读取图片流失败
     */
    public static Map<String, String> getImagBase64(String imgLink) throws IOException {
        Map<String, String> map = new HashMap<>(4);
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URI(imgLink).toURL().openConnection();
        } catch (URISyntaxException e) {
            throw new IOException("图片地址非法: " + imgLink, e);
        }
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(LINK_READ_TIMEOUT);
        conn.setReadTimeout(LINK_READ_TIMEOUT);
        String contentType = conn.getContentType();
        // contentType 形如 image/png, 截取 image/ 之后的子类型
        map.put("type", contentType == null ? null : contentType.substring("image/".length()));
        try (InputStream inputStream = conn.getInputStream()) {
            map.put("code", Base64.getEncoder().encodeToString(readAll(inputStream)));
        } finally {
            conn.disconnect();
        }
        return map;
    }

    /**
     * 读尽输入流。
     *
     * @param inputStream 输入流
     * @return 字节内容
     * @throws IOException 读取失败
     */
    private static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(temp)) != -1) {
            outputStream.write(temp, 0, len);
        }
        return outputStream.toByteArray();
    }
}
