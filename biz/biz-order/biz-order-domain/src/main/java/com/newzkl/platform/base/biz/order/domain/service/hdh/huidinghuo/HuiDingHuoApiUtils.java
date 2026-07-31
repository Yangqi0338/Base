package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req.*;
import com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res.*;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.properties.PalletProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isDev;


/**
 * 会订货API工具类（优化版） 基于模板方法模式封装通用接口调用流程，消除重复代码
 */
@Slf4j
@Component
@DependsOn({"palletProperties"})
public class HuiDingHuoApiUtils implements InitializingBean {

    public static final String ORDER_CREATE_URL = "/order/create_order.do"; // 3.3 创建订单
    private static final String API_VERSION = "v1.0";
    private static final String CHARSET = StandardCharsets.UTF_8.name();
    // ====================== 接口路径常量 ======================
    private static final String GET_SIGN_URL = "/api/getSign";
    private static final String PRODUCT_GET_URL = "/item/get_items.do"; // 3.1 根据商品id获取商品信息
    private static final String PRODUCT_BATCH_GET_URL = "/item/get_item_list.do"; // 3.2 批量获取商品接口
    private static final String GET_CATEGORY_LIST_URL = "/cate/get_category_list.do"; // 3.8 类目接口
    private static final String GET_CHANNEL_TYPE_LIST_URL = "/channel/get_channel_type_list.do"; // 3.9 渠道接口
    private static final String GET_BRAND_LIST_URL = "/brand/get_brand_list.do"; // 3.10 品牌列表接口
    private static final String GET_BRANDS_URL = "/brand/get_brands.do"; // 3.11 根据品牌id获取品牌接口
    private static final String GET_EXPRESS_FEE_TEMPLATES_URL = "/express/get_express_fee_templates.do"; // 3.12 运费模板详情
    private static final String QUERY_TRACK_LIST_URL = "/track/query_track_list.do"; // 3.13 包裹轨迹查询
    private static final String AREA_ADDRESS_LIST_URL = "/address/area_address_list.do"; // 3.14 省份地区列表
    private static final String GET_EXPRESS_FEE_URL = "/express/get_express_fee.do"; // 3.15 计算运费
    private static final String GET_ORDER_DETAIL_URL = "/order/v1/get_order_detail.do"; // 3.16 订单详情
    private static final String GET_ACCOUNT_INFO_URL = "/account/get_account_info.do"; // 3.17 账户信息
    /**
     * 默认空校验器（无额外校验时使用）
     */
    private static final ExtraParamValidator<?> DEFAULT_VALIDATOR = req -> {
    };
    /**
     * 品牌列表参数校验器：校验分页参数
     */
    private static final ExtraParamValidator<HuiDingHuoGetBrandListReq> BRAND_LIST_VALIDATOR = req -> {
        if (req.getPage() == null || req.getPage() <= 0) {
            throw new PlatformException(BaseErrorCode.PARAM, "页码page必须大于0");
        }
        if (req.getLimit() == null || req.getLimit() <= 0) {
            throw new PlatformException(BaseErrorCode.PARAM, "每页数量limit必须大于0");
        }
    };
    /**
     * 品牌ID列表校验器：校验ID列表
     */
    private static final ExtraParamValidator<HuiDingHuoGetBrandsReq> BRAND_IDS_VALIDATOR = req -> {
        if (CollUtil.isEmpty(req.getIds())) {
            throw new PlatformException(BaseErrorCode.PARAM, "品牌ID列表ids不能为空");
        }
        if (req.getIds().size() > 300) {
            throw new PlatformException(BaseErrorCode.PARAM, "品牌ID列表ids最多不能超过300个");
        }
    };
    public static String PROD_APP_ID = "6e6eecf38f4f4fd5a18beb5f1269c450";
    public static String PROD_APP_SECRET = "196bd98376da487ca00cdc4acaa3a26b";
    public static String PROD_ITEM_CODE = "FYHJ13FGZP04";
    // ====================== 配置常量 ======================
    private static String APP_ID = "5406301d7cdb4ab1a30701d325b68eab";
    private static String APP_SECRET = "6105b6eff51f4fdeb07b7321a7345f10";
    private static String API_BASE_URL = "http://test2.open.apiunion.com";
    // 模式
    @Setter
    private static Boolean testMode = false;

    /**
     * 通用接口调用模板，封装所有接口的固定流程
     *
     * @param request        业务请求对象
     * @param metadata       接口元数据（URL、请求/响应类型）
     * @param extraValidator 额外校验逻辑
     * @param <Req>          请求对象类型
     * @param <Res>          响应对象类型
     * @return 接口响应对象
     */
    @SuppressWarnings("unchecked")
    private static <Req extends HuiDingHuoBaseReq, Res extends HuiDingHuoBaseRes<?>> Res execute(Req request,
                                                                                                 ApiMetadata<Req, Res> metadata, ExtraParamValidator<Req> extraValidator) {
        // 1. 基础参数校验
        checkParam(request);

        // 2. 额外参数校验（如子列表校验）
        extraValidator.validate(request);

        try {
            // 测试环境但是生产模式
            if (isDev() && !testMode) {
                // 代理生产
                proxyProd();
                // 检查订单参数, 不允许下测试商品外的订单
                if (ORDER_CREATE_URL.equals(metadata.getUrl())) {
                    HuiDingHuoCreateOrderReq orderReq = (HuiDingHuoCreateOrderReq) request;
                    List<String> itemCodeList = orderReq.getSkuList().stream().map(HuiDingHuoCreateOrderReq.SkuItem::getItemCode).filter(Objects::nonNull).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(itemCodeList) && itemCodeList.stream().anyMatch(itemCode -> !itemCode.equals(PROD_ITEM_CODE))) {
                        throw new PlatformException(BaseErrorCode.EXECUTE, "测试环境生产代理模式不允许调用测试商品外的下单");
                    }
                }
            }

            // 3. 构建请求参数
            Map<String, Object> params = buildCommonParams();
            mergeBizParams(request, params);

            // 4. 生成JSON字符串
//			String jsonData = JSONObject.toJSONString(params, SerializerFeature.WriteNullStringAsEmpty,
//					SerializerFeature.DisableCircularReferenceDetect);
            String jsonData = JSON.toJSONString(params);

            // 5. 生成签名
            String sign = generateSign(jsonData);

            // 6. 构建请求头
            Map<String, Object> headers = buildHeaders(sign);

            // 7. 发送请求
            String fullUrl = API_BASE_URL + metadata.getUrl();
            log.info("调用会订货API[{}] - URL: {}, 入参JSON: {}, 签名: {}", metadata.getRequestType().getSimpleName(), fullUrl,
                    jsonData, sign);

//            String responseStr = HttpClientUtils.httpPostRequest(fullUrl, headers, jsonData);
//            log.info("调用会订货API[{}]返回参数: {}", metadata.getRequestType().getSimpleName(), responseStr);

            // 8. 解析响应
            Res response = JSONUtil.toBean(JSONUtil.toJsonStr(params), metadata.getResponseType());
            if (response == null) {
                throw new PlatformException(BaseErrorCode.EXECUTE, "API响应为空，接口：" + metadata.getUrl());
            }
            if (!"SUCCESS".equals(response.getCode()) || response.getSuccess() != 1) {
                throw new PlatformException(BaseErrorCode.EXECUTE, String.format("API调用失败[code=%s, msg=%s]，接口：%s",
                        response.getCode(), response.getMessage(), metadata.getUrl()));
            }
            return response;

        } catch (PlatformException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用会订货API[{}]处理异常", metadata.getUrl(), e);
            throw new PlatformException(BaseErrorCode.EXECUTE, "接口处理失败：" + e.getMessage());
        }
    }

    private static void proxyProd() {
        HuiDingHuoApiUtils.API_BASE_URL = "https://open.apiunion.com";
        HuiDingHuoApiUtils.APP_ID = PROD_APP_ID;
        HuiDingHuoApiUtils.APP_SECRET = PROD_APP_SECRET;
    }

    /**
     * 根据商品ID获取商品信息
     */
    public static HuiDingHuoProductDetailRes getProductById(HuiDingHuoProductGetReq request) {
        ApiMetadata<HuiDingHuoProductGetReq, HuiDingHuoProductDetailRes> metadata = new ApiMetadata<>(PRODUCT_GET_URL,
                HuiDingHuoProductGetReq.class, HuiDingHuoProductDetailRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 批量获取商品信息
     */
    public static HuiDingHuoProductBatchRes batchGetProducts(HuiDingHuoProductBatchGetReq request) {
        ApiMetadata<HuiDingHuoProductBatchGetReq, HuiDingHuoProductBatchRes> metadata = new ApiMetadata<>(
                PRODUCT_BATCH_GET_URL, HuiDingHuoProductBatchGetReq.class, HuiDingHuoProductBatchRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 创建订单
     */
    public static HuiDingHuoCreateOrderRes createOrder(HuiDingHuoCreateOrderReq request) {
        ApiMetadata<HuiDingHuoCreateOrderReq, HuiDingHuoCreateOrderRes> metadata = new ApiMetadata<>(ORDER_CREATE_URL,
                HuiDingHuoCreateOrderReq.class, HuiDingHuoCreateOrderRes.class);
        // 额外校验：校验skuList中的每个商品参数
        ExtraParamValidator<HuiDingHuoCreateOrderReq> validator = req -> req.getSkuList()
                .forEach(HuiDingHuoCreateOrderReq.SkuItem::isParamsValid);
        return execute(request, metadata, validator);
    }

    // ====================== 通用接口调用模板（核心） ======================

    /**
     * 获取类目列表
     */
    public static HuiDingHuoGetCategoryListRes getCategoryList(HuiDingHuoGetCategoryListReq request) {
        ApiMetadata<HuiDingHuoGetCategoryListReq, HuiDingHuoGetCategoryListRes> metadata = new ApiMetadata<>(
                GET_CATEGORY_LIST_URL, HuiDingHuoGetCategoryListReq.class, HuiDingHuoGetCategoryListRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 获取渠道类型列表
     */
    public static HuiDingHuoGetChannelTypeListRes getChannelTypeList(HuiDingHuoGetChannelTypeListReq request) {
        ApiMetadata<HuiDingHuoGetChannelTypeListReq, HuiDingHuoGetChannelTypeListRes> metadata = new ApiMetadata<>(
                GET_CHANNEL_TYPE_LIST_URL, HuiDingHuoGetChannelTypeListReq.class,
                HuiDingHuoGetChannelTypeListRes.class);
        return execute(request, metadata, emptyValidator());
    }

    // ====================== 业务接口调用方法（基于模板实现） ======================

    /**
     * 获取品牌列表（使用品牌列表参数校验器）
     */
    public static HuiDingHuoGetBrandListRes getBrandList(HuiDingHuoGetBrandListReq request) {
        ApiMetadata<HuiDingHuoGetBrandListReq, HuiDingHuoGetBrandListRes> metadata = new ApiMetadata<>(
                GET_BRAND_LIST_URL, HuiDingHuoGetBrandListReq.class, HuiDingHuoGetBrandListRes.class);
        return execute(request, metadata, BRAND_LIST_VALIDATOR);
    }

    /**
     * 根据品牌ID获取品牌信息（使用品牌ID列表校验器）
     */
    public static HuiDingHuoGetBrandsRes getBrands(HuiDingHuoGetBrandsReq request) {
        ApiMetadata<HuiDingHuoGetBrandsReq, HuiDingHuoGetBrandsRes> metadata = new ApiMetadata<>(GET_BRANDS_URL,
                HuiDingHuoGetBrandsReq.class, HuiDingHuoGetBrandsRes.class);
        return execute(request, metadata, BRAND_IDS_VALIDATOR);
    }

    /**
     * 获取运费模板详情
     */
    public static HuiDingHuoGetExpressFeeTemplatesRes getExpressFeeTemplates(
            HuiDingHuoGetExpressFeeTemplatesReq request) {
        ApiMetadata<HuiDingHuoGetExpressFeeTemplatesReq, HuiDingHuoGetExpressFeeTemplatesRes> metadata = new ApiMetadata<>(
                GET_EXPRESS_FEE_TEMPLATES_URL, HuiDingHuoGetExpressFeeTemplatesReq.class,
                HuiDingHuoGetExpressFeeTemplatesRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 查询包裹轨迹
     */
    public static HuiDingHuoQueryTrackListRes queryTrackList(HuiDingHuoQueryTrackListReq request) {
        ApiMetadata<HuiDingHuoQueryTrackListReq, HuiDingHuoQueryTrackListRes> metadata = new ApiMetadata<>(
                QUERY_TRACK_LIST_URL, HuiDingHuoQueryTrackListReq.class, HuiDingHuoQueryTrackListRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 获取省份地区列表
     */
    public static HuiDingHuoAreaAddressListRes getAreaAddressList(HuiDingHuoAreaAddressListReq request) {
        ApiMetadata<HuiDingHuoAreaAddressListReq, HuiDingHuoAreaAddressListRes> metadata = new ApiMetadata<>(
                AREA_ADDRESS_LIST_URL, HuiDingHuoAreaAddressListReq.class, HuiDingHuoAreaAddressListRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 计算运费
     */
    public static HuiDingHuoGetExpressFeeRes getExpressFee(HuiDingHuoGetExpressFeeReq request) {
        ApiMetadata<HuiDingHuoGetExpressFeeReq, HuiDingHuoGetExpressFeeRes> metadata = new ApiMetadata<>(
                GET_EXPRESS_FEE_URL, HuiDingHuoGetExpressFeeReq.class, HuiDingHuoGetExpressFeeRes.class);
        // 额外校验：校验skuList中的每个商品参数
        ExtraParamValidator<HuiDingHuoGetExpressFeeReq> validator = req -> req.getSkuList()
                .forEach(HuiDingHuoGetExpressFeeReq.SkuItem::isParamsValid);
        return execute(request, metadata, validator);
    }

    /**
     * 获取订单详情
     */
    public static HuiDingHuoGetOrderDetailRes getOrderDetail(HuiDingHuoGetOrderDetailReq request) {
        ApiMetadata<HuiDingHuoGetOrderDetailReq, HuiDingHuoGetOrderDetailRes> metadata = new ApiMetadata<>(
                GET_ORDER_DETAIL_URL, HuiDingHuoGetOrderDetailReq.class, HuiDingHuoGetOrderDetailRes.class);
        // 额外校验：orderNum和userOrderNum至少一项不为空
        ExtraParamValidator<HuiDingHuoGetOrderDetailReq> validator = HuiDingHuoGetOrderDetailReq::isOrderNumValid;
        return execute(request, metadata, validator);
    }

    /**
     * 获取账户信息
     */
    public static HuiDingHuoGetAccountInfoRes getAccountInfo(HuiDingHuoGetAccountInfoReq request) {
        ApiMetadata<HuiDingHuoGetAccountInfoReq, HuiDingHuoGetAccountInfoRes> metadata = new ApiMetadata<>(
                GET_ACCOUNT_INFO_URL, HuiDingHuoGetAccountInfoReq.class, HuiDingHuoGetAccountInfoRes.class);
        return execute(request, metadata, emptyValidator());
    }

    /**
     * 获取签名（内部使用）
     */
    private static HuiDingHuoSignRes getSign(HuiDingHuoGetSignReq request) {
        ApiMetadata<HuiDingHuoGetSignReq, HuiDingHuoSignRes> metadata = new ApiMetadata<>(GET_SIGN_URL,
                HuiDingHuoGetSignReq.class, HuiDingHuoSignRes.class);
        return execute(request, metadata, emptyValidator());
    }

    // 空参数校验器
    @SuppressWarnings("unused")
    private static <T> ExtraParamValidator<T> emptyValidator() {
        return req -> {
        };
    }

    /**
     * 生成签名 签名算法：MD5(CONCAT(json, secret))，大写处理
     */
    private static String generateSign(String jsonData) {
        if (StrUtil.isBlank(jsonData)) {
            throw new PlatformException(BaseErrorCode.PARAM, "生成签名失败：JSON参数不能为空");
        }

        try {
            String signStr = jsonData + APP_SECRET;
            String sign = DigestUtils.md5Hex(signStr.getBytes(CHARSET));
            log.info("签名原始串：{}，生成签名：{}", signStr, sign);
            return sign;
        } catch (Exception e) {
            log.error("生成签名失败", e);
            throw new PlatformException(BaseErrorCode.EXECUTE, "生成签名失败：" + e.getMessage());
        }
    }

    /**
     * 验证签名（基于JSON字符串） 签名逻辑：MD5(CONCAT(json字符串, secret)) 与传入的sign比对（均为大写）
     *
     * @param jsonData 原始JSON字符串（需与生成签名时的JSON一致）
     * @param sign     待验证的签名
     * @return 签名是否匹配
     */
    public static boolean verifySign(String jsonData, String sign) {
        // 校验入参合法性
        if (StrUtil.isBlank(jsonData)) {
            log.warn("验证签名失败：JSON字符串不能为空");
            return false;
        }
        if (StrUtil.isBlank(sign)) {
            log.warn("验证签名失败：待验证的签名不能为空");
            return false;
        }

        try {
//            JSONObject object = JSON.parseObject(jsonData);
//            object.put("appId", APP_ID);
            // 直接使用传入的JSON字符串生成签名（与生成逻辑一致）
            String generatedSign = generateSign(jsonData);
            // 比对签名（忽略大小写差异，实际场景中通常要求严格一致，这里按生成逻辑保持大写比对）
            boolean isMatch = sign.equals(generatedSign);
            if (!isMatch) {
                log.warn("签名不匹配：传入签名={}, 计算签名={}, 原始JSON={}", sign, generatedSign, jsonData);
            }
            return isMatch;
        } catch (Exception e) {
            log.error("验证签名过程异常", e);
            return false;
        }
    }

    /**
     * 构建公共请求参数
     */
    private static Map<String, Object> buildCommonParams() {
        Map<String, Object> commonParams = new HashMap<>(5);
        commonParams.put("appId", APP_ID);
//		commonParams.put("version", API_VERSION);
//		commonParams.put("timestamp", System.currentTimeMillis());
//		commonParams.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));
//		commonParams.put("charset", CHARSET);
        return commonParams;
    }

    /**
     * 构建请求头
     */
    private static Map<String, Object> buildHeaders(String sign) {
        Map<String, Object> headers = new HashMap<>(3);
        headers.put("SIGN", sign);
        headers.put("CONTENT-TYPE", "application/json");
        headers.put("ACCEPT-ENCODING", "gzip");
        return headers;
    }

    // ====================== 通用工具方法 ======================

    /**
     * 合并业务参数到通用参数
     */
    private static void mergeBizParams(HuiDingHuoBaseReq request, Map<String, Object> params) {
        //Map<String, Object> bizParams = BeanUtil.beanToMap(request, new HashMap<>(), false, true);
        ObjectMapper mapper = new ObjectMapper();
        // 配置不包含 null 值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String, Object> map = mapper.convertValue(request,
                new TypeReference<Map<String, Object>>() {
                });
        params.putAll(map);
    }

    /**
     * 基础参数校验
     */
    private static void checkParam(HuiDingHuoBaseReq request) {
        if (request == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "请求参数对象不能为空");
        }
    }

    // ====================== 初始化 ======================

    /**
     * Spring Bean 初始化时加载会订货渠道配置
     *
     * <p>迁移: 原 {@code @PostConstruct}(jakarta.annotation 不在 domain classpath) 改 InitializingBean
     */
    @Override
    public void afterPropertiesSet() {
        try {
            PalletProperties.Properties properties = PalletProperties.getChannelProperties(SpuEnum.SpuChannelSource.HDH.name().toLowerCase());
            HuiDingHuoApiUtils.API_BASE_URL = properties.getBaseUrl();
            HuiDingHuoApiUtils.APP_ID = properties.getPublicKey();
            HuiDingHuoApiUtils.APP_SECRET = properties.getPrivateKey();
        } catch (Exception e) {
        }

        log.info("会订货API工具类初始化完成，appId：{}", APP_ID);
    }

    /**
     * 额外参数校验器：函数式接口，处理接口特殊校验逻辑
     */
    @FunctionalInterface
    private interface ExtraParamValidator<Req> {
        void validate(Req request);
    }

    /**
     * 接口元数据：封装接口URL、请求类型、响应类型
     */
    @Data
    @AllArgsConstructor
    private static class ApiMetadata<Req, Res> {
        private String url;
        private Class<Req> requestType;
        private Class<Res> responseType;
    }
}