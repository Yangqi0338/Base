package com.newzkl.platform.base.biz.finance.domain.hf;


import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.finance.model.pay.req.huifu.*;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeBaseRes;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.*;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.HuiFuBindCardReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.HuiFuEntUserOpenAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.HuiFuUserOpenAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.res.huifu.AccountBindSyncRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.huifu.OpenAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.huifu.TripartiteAccountBaseRes;
import com.newzkl.platform.base.biz.finance.model.support.TripartiteBaseRes;
import com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties;
import com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuNotifyEnum;
import com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import static com.newzkl.platform.base.biz.finance.domain.hf.AmountReq.*;
import static com.newzkl.platform.base.biz.finance.domain.hf.AmountRes.*;
import static com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties.publicKey;
import static com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties.sysId;

/**
 * 汇付工具类
 * 1、静态化 + 防腐层
 * 2、加签验签
 */
@Slf4j
@Component
public class HuiFuMethod {

    private static final String ALGORITHM = "RSA";
    private static final String SUB_ALGORITHM = "SHA256WithRSA";
    private static HuiFuApi api;

    /**
     * 汇付聚合正扫
     * 场景1-台牌码：用户通过扫描聚合静态二维码，输入订单金额完成支付操作
     * 场景2-公众号/小程序商城：用户通过公众号/小程序下单，输入密码完成支付操作。
     * 场景3-商户根据用户选择支付方式，调用该接口生成支付宝/银联/数字人民币，用户使用对应app下“扫一扫”完成支付操作。
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HuiFuPayRes pay(HuiFuPayReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));
        // 转化内部类
        PayReq req = new PayReq();
        req.setReq_seq_id(request.getTradeNo());
        req.setGoods_desc(request.getGoodsInfo());
        req.setTrade_type(EnumUtil.toString(request.getTradeType()));
        String amount = decorateAmount(request.getPayAmount());
        req.setTrans_amt(amount);
        req.setNotify_url(HuiFuProperties.getUrl(HuiFuNotifyEnum.pay));

        PayRes res = api.pay(req);
        // 转化业务类
        HuiFuPayRes huiFuPayRes = buildTradeRes(res, HuiFuPayRes.class);
        huiFuPayRes.setTradeType(request.getTradeType());
        huiFuPayRes.setQrCode(res.getQr_code());
        huiFuPayRes.setTradeNo(res.getTradeNo());
        huiFuPayRes.setTradeAmount(amountDecimal(amount));
        return huiFuPayRes;
    }

    /**
     * 主动查询支付单支付状态
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HuiFuPayStateRes payState(HuiFuPayStateReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));
        PayStateReq req = new PayStateReq();
        req.setOrg_hf_seq_id(request.getHuifuTradeNo());
        PayStateRes res = api.payState(req);
        // 转化业务类
        return buildRes(res, HuiFuPayStateRes.class);
    }

    /**
     * 汇付扫码交易原路退款
     * 交易发生之后一段时间内，由于用户或者商户的原因需要退款时，商户可以通过本接口将支付款退还给用户，退款成功资金将原路返回
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HuiFuRefundRes refund(HuiFuRefundReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));
        // 转化内部类
        RefundReq req = new RefundReq();
        req.setReq_seq_id(idStr(request.getSellAfterOrderNo()));
        req.setOrg_req_seq_id(request.getTradeNo());
        String reqDate = dateStr(request.getTradeDate());
        req.setOrg_req_date(reqDate);
        String amount = decorateAmount(request.getRefundAmount());
        req.setOrd_amt(amount);
        req.setNotify_url(HuiFuProperties.getUrl(HuiFuNotifyEnum.refund));

        RefundRes res = api.refund(req);
        // 转化业务类
        HuiFuRefundRes huiFuRefundRes = buildTradeRes(res, HuiFuRefundRes.class);
        huiFuRefundRes.setTradeDate(reqDate);
        huiFuRefundRes.setActualRefundAmount(amountDecimal(res.getActual_ref_amt()));
        huiFuRefundRes.setTradeAmount(amountDecimal(res.getOrd_amt()));
        huiFuRefundRes.setTradeFinishTime(res.getTrans_finish_time());

        return huiFuRefundRes;
    }

    /**
     *
     * 汇付取现
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HuiFuWithdrawRes withdraw(HuiFuWithdrawReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));

        // 转化内部类
        WithdrawReq req = new WithdrawReq();
        req.setInto_acct_date_type(HuiFuProperties.settleCycle);
        req.setToken_no(request.getTokenNo());
        req.setReq_seq_id(idStr(request.getWithdrawId()));
        String amount = decorateAmount(request.getWithdrawAmount());
        req.setCash_amt(amount);
        req.setNotify_url(HuiFuProperties.getUrl(HuiFuNotifyEnum.withdraw));

        WithdrawRes res = api.withdraw(req);
        // 转化业务类
        HuiFuWithdrawRes huiFuWithdrawRes = buildTradeRes(res, HuiFuWithdrawRes.class);
        huiFuWithdrawRes.setTradeAmount(amountDecimal(amount));
        return huiFuWithdrawRes;
    }

    /**
     * 汇付代发 (走余额支付, 根据设置的用户结算周期, 来自动结算钱)
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HuiFuRollOutRes rollOut(HuiFuRollOutReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));

        // 转化内部类
        RollOutReq req = new RollOutReq();
        req.setReq_seq_id(idStr(request.getRollOutId()));
        req.setOut_huifu_id(HuiFuProperties.sysId);
        req.setHuifu_id(request.getHuifuId());
        String amount = decorateAmount(request.getApplyAmount());
        req.setOrd_amt(amount);
        req.setNotify_url(HuiFuProperties.getUrl(HuiFuNotifyEnum.withdraw));

        List<RollOutReq.AcctInfo> acctInfos = new ArrayList<>();
        RollOutReq.AcctInfo acctInfo = new RollOutReq.AcctInfo();
        acctInfos.add(acctInfo);
        acctInfo.setHuifu_id(req.getHuifu_id());
        acctInfo.setDiv_amt(amount);
        RollOutReq.AcctSplitBunch acctSplitBunch = new RollOutReq.AcctSplitBunch(acctInfos);
        req.setAcct_split_bunch(acctSplitBunch);

        RollOutRes res = api.rollout(req);
        // 转化业务类
        HuiFuRollOutRes huiFuRollOutRes = buildTradeRes(res, HuiFuRollOutRes.class);
        huiFuRollOutRes.setTradeAmount(amountDecimal(res.getOrd_amt()));
        huiFuRollOutRes.setTradeFinishTime(res.getTrans_finish_time());

        return huiFuRollOutRes;
    }

    /**
     * 汇付企业开户
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static OpenAccountRes entOpenAccount(HuiFuEntUserOpenAccountReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));
        AccountReq.EntUserEnterReq req = buildEntEnterReq(request);
        AccountRes.OpenAccountRes res = api.entOpenAccount(req);

        OpenAccountRes result = buildAccountRes(res, OpenAccountRes.class);
        return result;
    }

    @NonNull
    private static AccountReq.EntUserEnterReq buildEntEnterReq(HuiFuEntUserOpenAccountReq request) {
        return buildReq(request, new AccountReq.EntUserEnterReq(), (v1, req) -> {
            req.setLicense_validity_type(v1.getLicenseValidType());
            req.setReg_prov_id(v1.getEntProvCode());
            req.setReg_area_id(v1.getEntAreaCode());
            req.setReg_district_id(v1.getEntRegionCode());
            req.setReg_detail(v1.getEntAddress());
            req.setLegal_cert_validity_type(v1.getLegalCertValidType());
        });

    }

    /* ----------------------------------------- 业务方法 ------------------------------------------ */

    /**
     * 用户汇付开户
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static OpenAccountRes userOpenAccount(HuiFuUserOpenAccountReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));

        AccountReq.UserEnterReq req = buildEnterReq(request);
        AccountRes.OpenAccountRes res = api.userOpenAccount(req);

        OpenAccountRes result = buildAccountRes(res, OpenAccountRes.class);
        return result;
    }

    private static AccountReq.UserEnterReq buildEnterReq(HuiFuUserOpenAccountReq request) {
        return buildReq(request, new AccountReq.UserEnterReq(), (v1, req) -> {
            req.setCert_validity_type(request.getCertValidType());
            req.setMobile_no(request.getMobile());
        });
    }

    /**
     * 用户汇付绑卡
     * @param request
     * @return
     * @throws Exception
     */
    public static AccountBindSyncRes userBindCard(HuiFuBindCardReq request) {
        log.info("请求原始数据:{}", JSONUtil.toJsonStr(request));

        AccountReq.EnterCardReq req = buildBindCardSyncReq(request);
        AccountRes.AccountBindSyncRes res = api.bindCard(req);

        AccountBindSyncRes result = buildAccountRes(res, AccountBindSyncRes.class);
        return result;
    }

    @NonNull
    private static AccountReq.EnterCardReq buildBindCardSyncReq(HuiFuBindCardReq request) {
        AccountReq.EnterCardReq req = new AccountReq.EnterCardReq();

        TransferUtils.transfer(request, () -> req,
                null, CopyOptions.create().setFieldNameEditor(StrUtil::toUnderlineCase));

        req.setUpper_huifu_id(sysId);
        req.setAsync_return_url(HuiFuProperties.getUrl(HuiFuNotifyEnum.bindCard));

        // 结算信息
        AccountReq.SettleConfig settleConfig = new AccountReq.SettleConfig();
        settleConfig.setSettle_cycle(HuiFuProperties.settleCycle);
        req.setSettle_config(settleConfig);

        // 卡信息
        AccountReq.CardInfo cardInfo = new AccountReq.CardInfo();
        cardInfo.setCard_type(request.getCardType());

        cardInfo.setCard_no(request.getCardNo());
        cardInfo.setCard_name(request.getCardName());
        cardInfo.setProv_id(request.getBankProvCode());
        cardInfo.setArea_id(request.getBankAreaCode());
        cardInfo.setBranch_code(request.getBranchCode());
        // 固定身份证类型
        cardInfo.setCert_type("00");
        cardInfo.setCert_no(request.getCertNo());
        cardInfo.setCert_validity_type(request.getCertValidType());
        cardInfo.setCert_begin_date(request.getCertBeginDate());
        cardInfo.setCert_end_date(request.getCertEndDate());
        req.setCard_info(cardInfo);

        return req;
    }

    /* ----------------------------------------- 功能方法 ------------------------------------------ */

    public static String sign(String data) {
        if (StrUtil.isBlank(HuiFuProperties.privateKey)) {
            throw new PlatformException(BaseErrorCode.EXECUTE, "汇付商户私钥未配置, 无法加签");
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(HuiFuProperties.privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SUB_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            log.error("汇付加签失败", e);
            throw new PlatformException(BaseErrorCode.EXECUTE, "汇付加签失败");
        }
    }

    public static boolean verify(String data, String sign) {
        if (StrUtil.isBlank(publicKey)) {
            log.error("汇付平台公钥未配置, 验签直接失败");
            return false;
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SUB_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("汇付验签失败", e);
            return false;
        }
    }

    // 迁移: 移除 verify(String, HttpServletRequest) 重载 (servlet 依赖属入口层, 控制器已延迟);
    // 回调验签由入口 starter 提取 sign 后调用 verify(String data, String sign)。

    /**
     * 装饰金额
     */
    public static String decorateAmount(Integer cents) {
        BigDecimal amount = BizUtil.percent(cents, 2, RoundingMode.FLOOR);
        // 非生产走逻辑
        if (SecurityContextHolder.isDev()) {
            // 阶梯下降,直到小于1元 (判断价格是否正确)
            if (HuiFuProperties.devDecorateMode == 1) {
                Double decimal = stepDownAmount(amount.doubleValue());
                return String.format("%.2f", decimal);
            } else if (HuiFuProperties.devDecorateMode == 0) {
                return "0.01";
            }
        }
        return amount.toString();
    }

    private static Double stepDownAmount(Double amount) {
        if (amount >= 100) {
            return stepDownAmount(amount / 10);
        }
        return amount;
    }

    /**
     * 构建基础交易类
     */
    private static <T extends TradeBaseRes> T buildTradeRes(TradeRes res, Class<T> clazz) {
        T result = buildRes(res, clazz);
        result.setTripartiteNo(res.getThirdTradeNo());
        return result;
    }

    /**
     * 构建基础账号类
     */
    private static <T extends TripartiteAccountBaseRes> T buildAccountRes(AccountRes.UserRes res, Class<T> clazz) {
        return buildRes(res, clazz);
    }

    /**
     * 构建基础交易类
     */
    private static <T extends TripartiteBaseRes> T buildRes(Base.Res res, Class<T> clazz) {
        T result = TransferUtils.transfer(res, () -> ReflectUtil.newInstance(clazz), CopyOptions.create().setFieldNameEditor(StrUtil::toCamelCase));
        return result.build();
    }

    /**
     * 构建基础交易类
     */
    private static <A, T extends Base.Req> T buildReq(A request, T req, BiConsumer<A, T> biConsumer) {
        return TransferUtils.transfer(request, () -> req, biConsumer, CopyOptions.create().setFieldNameEditor(StrUtil::toUnderlineCase));
    }


    /**
     * 将时间转化为汇付时间
     */
    private static String dateStr(LocalDateTime date) {
        return DateUtil.date(date).toString("yyyyMMdd");
    }

    /**
     * 将id转化为汇付字符串
     */
    private static String idStr(Long id) {
        return StrUtil.toStringOrEmpty(id);
    }

    /**
     * 字符串转double
     */
    private static Double amountDecimal(String amount) {
        return NumberUtil.toBigDecimal(amount).doubleValue();
    }

    @Autowired
    public void setHuiFuApi(HuiFuApi huiFuApi) {
        HuiFuMethod.api = huiFuApi;
    }

    /**
     * 递归字典序排序 JSON 字符串
     *
     * <p>汇付加签要求参数按 key 升序排列后再签名, 此方法基于 TreeMap 对 JSON 做深度排序,
     * 保证嵌套对象与数组内的对象同样有序。</p>
     *
     * @param sourceJson 原始 JSON 字符串
     * @param maxLayer   数组最大嵌套层级, 超出抛参数异常; 传 0 不限制
     * @param needLoop   是否对嵌套对象递归排序
     * @return 排序后的 JSON 字符串, 入参为空时返回空串
     */
    public static String loopSort4JsonString(String sourceJson, int maxLayer, boolean needLoop) {
        if (StrUtil.isBlank(sourceJson)) {
            return "";
        }

        TreeMap<String, Object> m = JSONObject.parseObject(sourceJson, TreeMap.class);
        if (maxLayer > 0) {
            for (Map.Entry<String, Object> entry : m.entrySet()) {
                int layer = 0;
                if (entry.getValue() instanceof JSONArray array) {
                    ++layer;
                    sortJsonArray(array, layer, maxLayer);
                }
            }
        }
        if (needLoop && MapUtil.isNotEmpty(m)) {
            List<Map.Entry<String, Object>> handleMapList = m.entrySet().stream()
                    .filter(it -> it.getValue() instanceof JSONObject).toList();

            if (CollUtil.isNotEmpty(handleMapList)) {
                for (Map.Entry<String, Object> map : handleMapList) {
                    String sortJson = loopSort4JsonString(JSON.toJSONString(map.getValue()), maxLayer + 1, needLoop);
                    map.setValue(sortJson);
                }
            }
        }

        return JSON.toJSONString(m);
    }

    private static void sortJsonArray(JSONArray array, int layer, int maxLayer) {
        if (layer >= maxLayer) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        for (int i = 0; i < array.size(); ++i) {
            JSONArray nested;
            if (array.get(i) instanceof JSONArray) {
                nested = (JSONArray) array.get(i);
                ++layer;
                sortJsonArray(nested, layer, maxLayer);
            } else if (!(array.get(i) instanceof Comparable)) {
                Map map = JSON.parseObject(array.get(i).toString(), TreeMap.class);
                array.set(i, map);
                for (Object o : map.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    if (entry.getValue() instanceof JSONArray) {
                        nested = (JSONArray) entry.getValue();
                        ++layer;
                        sortJsonArray(nested, layer, maxLayer);
                    }
                }
            }
        }
    }

}
