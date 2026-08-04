package com.newzkl.platform.base.biz.sys.domain.adapt.api;

/**
 * OCR 识别出站端口
 *
 * <p>迁移说明: 源 {@code CommonController#ocrIdentify} 把华为云 OCR SDK 调用
 * (含 {@code OcrClient} 注入) 直接写在 controller 内。本次按 Base 架构规约下沉,
 * SDK 细节全落 {@code infrastructure/gateway}, controller 只经本端口调用,
 * 端口收发不出现任何 SDK 类型 (domain 不依赖 infra)。</p>
 *
 * @author KC
 */
public interface OcrApi {

    /**
     * 身份证识别
     *
     * @param imageUrl 身份证图片 URL
     * @return 三方识别结果 (已转为通用结构, 与源 {@code RecognizeIdCardResponse} 序列化后契约一致);
     *         识别失败返回 {@code null} (与源逐字一致: 源异常分支仅打印堆栈后返回 null)
     */
    Object recognizeIdCard(String imageUrl);
}
