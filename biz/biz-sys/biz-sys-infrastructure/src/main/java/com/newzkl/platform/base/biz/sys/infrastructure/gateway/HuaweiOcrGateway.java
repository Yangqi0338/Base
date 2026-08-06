package com.newzkl.platform.base.biz.sys.infrastructure.gateway;

import cn.hutool.json.JSONUtil;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ocr.v1.OcrClient;
import com.huaweicloud.sdk.ocr.v1.model.BusinessLicenseRequestBody;
import com.huaweicloud.sdk.ocr.v1.model.IdCardRequestBody;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeBusinessLicenseRequest;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeBusinessLicenseResponse;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeIdCardRequest;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeIdCardResponse;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OcrApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 华为云 OCR 识别网关
 *
 * <p>迁移说明: 源 {@code CommonController#ocrIdentify} 把 SDK 调用写在 controller 内。
 * 本次按 Base 架构规约下沉至 {@code infrastructure/gateway}, controller 只经
 * {@code OcrApi} 出站端口调用。入参 (图片 URL 经 {@code IdCardRequestBody#withUrl})、
 * 调用方式 ({@code OcrClient#recognizeIdCard}) 与源逐字一致。</p>
 *
 * <p>行为对齐: 源在三类 SDK 异常分支仅打印堆栈后 {@code response} 保持 {@code null},
 * 最终 {@code ScmResult.success(null)}; 本实现改 {@code printStackTrace} 为日志,
 * 同样在失败时返回 {@code null}, 对外契约不变。</p>
 *
 * <p>出参: 源直返 SDK {@code RecognizeIdCardResponse}, 由 Jackson 序列化为
 * {@code {result:{...}}}。本实现经 {@code JSONUtil} 转为通用结构 (不把 SDK 类型
 * 漏进 domain 端口), 序列化后 JSON 形状与源一致。</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HuaweiOcrGateway implements OcrApi {

    private final OcrClient ocrClient;

    @Override
    public Object recognizeIdCard(String imageUrl) {
        RecognizeIdCardRequest request = new RecognizeIdCardRequest();
        IdCardRequestBody body = new IdCardRequestBody();
        body.withUrl(imageUrl);
        request.withBody(body);
        try {
            RecognizeIdCardResponse response = ocrClient.recognizeIdCard(request);
            return JSONUtil.parse(JSONUtil.toJsonStr(response));
        } catch (ConnectionException | RequestTimeoutException e) {
            log.error("[HuaweiOcrGateway] 身份证识别连接/超时失败, url={}", imageUrl, e);
        } catch (ServiceResponseException e) {
            log.error("[HuaweiOcrGateway] 身份证识别服务异常, url={}, httpStatus={}, errorCode={}, errorMsg={}",
                    imageUrl, e.getHttpStatusCode(), e.getErrorCode(), e.getErrorMsg(), e);
        }
        return null;
    }

    @Override
    public Object recognizeBusinessLicense(String imageUrl) {
        RecognizeBusinessLicenseRequest request = new RecognizeBusinessLicenseRequest();
        BusinessLicenseRequestBody body = new BusinessLicenseRequestBody();
        body.withUrl(imageUrl);
        request.withBody(body);
        try {
            RecognizeBusinessLicenseResponse response = ocrClient.recognizeBusinessLicense(request);
            return JSONUtil.parse(JSONUtil.toJsonStr(response));
        } catch (ConnectionException | RequestTimeoutException e) {
            log.error("[HuaweiOcrGateway] 营业执照识别连接/超时失败, url={}", imageUrl, e);
        } catch (ServiceResponseException e) {
            log.error("[HuaweiOcrGateway] 营业执照识别服务异常, url={}, httpStatus={}, errorCode={}, errorMsg={}",
                    imageUrl, e.getHttpStatusCode(), e.getErrorCode(), e.getErrorMsg(), e);
        }
        return null;
    }
}
