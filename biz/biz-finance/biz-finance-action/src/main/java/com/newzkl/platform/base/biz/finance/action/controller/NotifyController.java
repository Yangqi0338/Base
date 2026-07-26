package com.newzkl.platform.base.biz.finance.action.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.purse.service.WithdrawService;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.ll.SignUtils;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.enums.CacheKey;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuAsyncRes;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuBindCardNotifyRes;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayNotifyRes;
import com.newzkl.platform.base.biz.finance.model.person.res.PersonPurseNotifyRes;
import com.newzkl.platform.base.biz.finance.model.person.res.TripartiteTxnNotifyRes;
import com.newzkl.platform.base.biz.finance.model.person.res.WithdrawNotifyRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountTripartitePurseQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 三方支付异步回调控制器 (连连 + 汇付)。
 *
 * <p>迁移自 new-scm {@code interfaces.pay.PayNotifyController}。</p>
 *
 * <p>迁移调整:</p>
 * <ul>
 *   <li>{@code javax.servlet.*} → {@code jakarta.servlet.*} (Spring Boot 3);</li>
 *   <li>连连验签走 {@link SignUtils#checkSign}, 汇付验签走 {@link HuiFuMethod#verify}
 *       (旧 {@code verify(String, HttpServletRequest)} 重载已在 domain 层裁掉,
 *       sign 提取属入口层职责, 由本控制器完成);</li>
 *   <li>{@code TRADE_*} / {@code Y P N} 裸串收敛为 {@link PurseEnum.TripartiteTxnStatus}
 *       与 {@link PurseEnum.TripartitePurchaseAuditStatus};</li>
 *   <li>{@code queryPageAccountTripartitePurse} 在 Base 直接返回 {@code List},
 *       旧代码的 {@code .getList()} 去掉;</li>
 *   <li>{@code AccountTripartitePurseVO.userStatus} 在 Base 已是枚举, 三方状态串经
 *       {@code getByValue} 转换后再赋值;</li>
 *   <li>旧 {@code consumeNotify} 里注释掉的连连分支未迁 (汇付已接管消费回调)。</li>
 * </ul>
 *
 * <p>回调接口不做鉴权 (三方直连), 安全性由验签保证。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    /**
     * 三方要求的成功应答体。
     */
    private static final String SUCCESS = "Success";

    /**
     * 连连转出到账成功状态值。
     */
    private static final Integer ROLL_OUT_SUCCESS = 1;

    /**
     * 连连转出到账失败状态值。
     */
    private static final Integer ROLL_OUT_FAIL = 2;

    /**
     * 汇付通知类型 - 审核消息。
     */
    private static final String NOTIFY_TYPE_AUDIT = "A";

    /**
     * 连连签名请求头名。
     */
    private static final String HEADER_SIGNATURE = "Signature-Data";

    private final TripartitePurseDomain tripartitePurse;

    private final CashPayService cashPayService;

    private final WithdrawService withdrawService;

    /**
     * 消费支付结果回调 (汇付)。
     *
     * @param request 回调请求
     * @return 成功返回 {@code Success}; 非成功状态返回 {@code null} 触发三方重试
     * @throws IOException 读取请求体失败
     */
    @PostMapping("/consumeNotify")
    public String consumeNotify(HttpServletRequest request) throws IOException {
        HuiFuAsyncRes huiFuRes = getHuiFuRes(request);
        HuiFuPayNotifyRes res = JSONUtil.toBean(huiFuRes.getData(), HuiFuPayNotifyRes.class);
        if (res != null && res.isSuccess()) {
            cashPayService.alterPayState(Long.valueOf(res.getReq_seq_id()), res.getHf_seq_id());
            return SUCCESS;
        }
        return null;
    }

    /**
     * 查询支付状态缓存。
     *
     * @param businessKey 业务键
     * @param key         业务单键
     * @return 支付状态, 无缓存返回 0
     */
    @GetMapping("/state")
    public ScmResult<Long> state(@RequestParam("businessKey") String businessKey,
                                 @RequestParam("key") String key) {
        Long state = RedisUtil.get(StrUtil.format(CacheKey.PAYMENT_STATE, businessKey, key));
        return ScmResult.success(Opt.ofNullable(state).orElse(0L));
    }

    /**
     * 个人 / 企业开户结果回调 (连连)。
     *
     * @param request 回调请求
     * @return 成功返回 {@code Success}; 验签失败返回 {@code null}
     * @throws IOException 读取请求体失败
     */
    @PostMapping("/personPurseNotify")
    public String personPurseNotify(HttpServletRequest request) throws IOException {
        String data = getNotifyDataAndCheck(request);
        if (data == null) {
            return null;
        }
        PersonPurseNotifyRes result = JSONUtil.toBean(data, PersonPurseNotifyRes.class);
        AccountTripartitePurseVO accountTripartite = new AccountTripartitePurseVO();
        accountTripartite.setAccountId(Long.valueOf(result.getUser_id()));
        accountTripartite.setOidUserNo(result.getOid_userno());
        accountTripartite.setUserStatus(PurseEnum.TripartitePurchaseStatus.getByValue(result.getUser_status()));
        accountTripartite.setRemark(result.getRemark());
        if (result.getAccountInfo() != null) {
            accountTripartite.setAccountLevel(result.getAccountInfo().getAccount_level());
        }
        tripartitePurse.alterAccountTripartitePurse(accountTripartite);
        return SUCCESS;
    }

    /**
     * 绑卡审核结果回调 (汇付)。
     *
     * @param request 回调请求
     * @return 成功返回 {@code Success}; 缺少 {@code data} 或验签失败返回 {@code null}
     */
    @PostMapping("/huifuBindCard")
    public String huifuBindCard(HttpServletRequest request) {
        String data = request.getParameter("data");
        if (data == null) {
            log.error("汇付绑卡回调缺少 data, resp_code={}, resp_desc={}",
                    request.getParameter("resp_code"), request.getParameter("resp_desc"));
            return null;
        }
        if (!getHuiFuNotifyDataAndCheck(data, request)) {
            log.error("汇付绑卡回调验签失败");
            return null;
        }
        HuiFuBindCardNotifyRes result = JSONUtil.toBean(data, HuiFuBindCardNotifyRes.class);
        // 仅处理审核类通知, 其余类型 (灵活用工 H / 电子账户 Z) 直接应答成功
        if (!NOTIFY_TYPE_AUDIT.equals(result.getNotify_type()) || result.getAudit_info() == null) {
            return SUCCESS;
        }
        applyAuditResult(result.getAudit_info());
        return SUCCESS;
    }

    /**
     * 平台转出到账结果回调 (连连)。
     *
     * @param request 回调请求
     * @return 已处理状态返回 {@code Success}; 处理中状态返回 {@code null}
     * @throws IOException 读取请求体失败
     */
    @PostMapping("/rollOutNotify")
    public String rollOutNotify(HttpServletRequest request) throws IOException {
        String data = getNotifyDataAndCheck(request);
        if (data == null) {
            return null;
        }
        TripartiteTxnNotifyRes result = JSONUtil.toBean(data, TripartiteTxnNotifyRes.class);
        PurseEnum.TripartiteTxnStatus status = PurseEnum.TripartiteTxnStatus.getByValue(result.getTxn_status());
        Long applyId = Long.valueOf(result.getOrderInfo().getTxn_seqno());
        if (PurseEnum.TripartiteTxnStatus.SUCCESS == status) {
            withdrawService.alterRollOutTripartiteState(applyId, ROLL_OUT_SUCCESS, result.getAccp_txno());
            return SUCCESS;
        }
        if (PurseEnum.TripartiteTxnStatus.CLOSE == status) {
            withdrawService.alterRollOutTripartiteState(applyId, ROLL_OUT_FAIL, result.getAccp_txno());
            return SUCCESS;
        }
        return null;
    }

    /**
     * 客户提现结果回调 (连连)。
     *
     * @param request 回调请求
     * @return 成功返回 {@code Success}; 验签失败返回 {@code null}
     * @throws IOException 读取请求体失败
     */
    @PostMapping("/withdrawOutNotify")
    public String withdrawOutNotify(HttpServletRequest request) throws IOException {
        String data = getNotifyDataAndCheck(request);
        if (data == null) {
            return null;
        }
        withdrawService.withdrawNotify(JSONUtil.toBean(data, WithdrawNotifyRes.class));
        return SUCCESS;
    }

    /**
     * 按汇付审核结果更新三方账户状态。
     *
     * @param auditInfo 审核信息
     */
    private void applyAuditResult(HuiFuBindCardNotifyRes.AuditInfo auditInfo) {
        String applyNo = auditInfo.getApply_no();
        AccountTripartitePurseQuery query = new AccountTripartitePurseQuery();
        query.setOidApplySeqNo(applyNo);
        query.resetQuerySingle();
        List<AccountTripartitePurseVO> dbList = tripartitePurse.queryPageAccountTripartitePurse(query);
        if (dbList == null || dbList.isEmpty()) {
            log.error("汇付绑卡回调未知的申请id[{}]", applyNo);
            throw new ScmException(BaseErrorCode.NODATA);
        }
        AccountTripartitePurseVO accountTripartite = dbList.get(0);

        PurseEnum.TripartitePurchaseAuditStatus auditStatus =
                PurseEnum.TripartitePurchaseAuditStatus.getByValue(auditInfo.getAudit_status());
        if (auditStatus == null) {
            log.error("汇付绑卡回调未知的审核状态[{}], 申请id[{}]", auditInfo.getAudit_status(), applyNo);
            throw new ScmException(BaseErrorCode.PARAM);
        }
        switch (auditStatus) {
            case HUI_FU_SUCCESS -> accountTripartite.setUserStatus(PurseEnum.TripartitePurchaseStatus.NORMAL);
            case HUI_FU_FAIL -> {
                accountTripartite.setUserStatus(PurseEnum.TripartitePurchaseStatus.BIND_FAIL);
                accountTripartite.setRemark(auditInfo.getAudit_desc());
            }
            // 审核中不改状态, 仅走一次更新保持与旧实现一致
            case HUI_FU_PROCESS -> log.info("汇付绑卡审核中, 申请id[{}]", applyNo);
            default -> throw new ScmException(BaseErrorCode.PARAM);
        }
        tripartitePurse.alterAccountTripartitePurse(accountTripartite);
    }

    /**
     * 读取连连回调报文并验签。
     *
     * @param request 回调请求
     * @return 验签通过返回原始报文; 否则返回 {@code null}
     * @throws IOException 读取请求体失败
     */
    private String getNotifyDataAndCheck(HttpServletRequest request) throws IOException {
        String signature = request.getHeader(HEADER_SIGNATURE);
        String data = readBody(request);
        log.info("接收连连异步通知, 签名值: {}", signature);
        if (SignUtils.checkSign(data, signature)) {
            return data;
        }
        log.error("连连异步通知验签失败");
        return null;
    }

    /**
     * 汇付回调验签。
     *
     * @param data    回调业务报文 ({@code resp_data})
     * @param request 回调请求 (取 {@code sign} 参数)
     * @return 验签通过返回 {@code true}
     */
    private boolean getHuiFuNotifyDataAndCheck(String data, HttpServletRequest request) {
        return HuiFuMethod.verify(data, request.getParameter("sign"));
    }

    /**
     * 解析汇付异步回调报文。
     *
     * <p>汇付以 {@code application/x-www-form-urlencoded} 推送, 但部分环境下 Spring 未完成
     * 表单解析, 故先手工解析请求体, 缺失字段再回退到 {@code request.getParameter}。</p>
     *
     * @param request 回调请求
     * @return 汇付异步响应
     * @throws IOException 读取请求体失败
     */
    private HuiFuAsyncRes getHuiFuRes(HttpServletRequest request) throws IOException {
        Map<String, String> params = parseAndDecodeFormData(readBody(request));
        HuiFuAsyncRes res = new HuiFuAsyncRes();
        res.setSign(paramOrFallback(params, request, "sign"));
        res.setData(paramOrFallback(params, request, "resp_data"));
        res.setResp_code(paramOrFallback(params, request, "resp_code"));
        res.setResp_desc(paramOrFallback(params, request, "resp_desc"));
        return res;
    }

    /**
     * 取表单解析结果, 空白时回退请求参数。
     *
     * @param params  手工解析出的表单参数
     * @param request 回调请求
     * @param name    参数名
     * @return 参数值
     */
    private String paramOrFallback(Map<String, String> params, HttpServletRequest request, String name) {
        return Opt.ofBlankAble(params.get(name)).orElseGet(() -> request.getParameter(name));
    }

    /**
     * 以 UTF-8 读尽请求体。
     *
     * @param request 请求
     * @return 请求体字符串
     * @throws IOException 读取失败
     */
    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    /**
     * 解析并解码 urlencoded 表单串。
     *
     * <p>迁移调整: 旧实现按 key/value 打 {@code log.error} 输出全部回调明细 (含签名),
     * 属日志噪音与敏感信息外泄, 迁移时删除。</p>
     *
     * @param formData 表单串
     * @return 解码后的键值对; 入参为空返回空 Map
     */
    static Map<String, String> parseAndDecodeFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isEmpty(formData)) {
            return params;
        }
        for (String pair : formData.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length != 2) {
                continue;
            }
            try {
                params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            } catch (IllegalArgumentException e) {
                // 解码失败时保留原始值, 与旧实现一致
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}
