package com.newzkl.platform.base.biz.socialbang.model.im.req;

import lombok.Data;

/**
 * 敏感词新增/编辑请求
 *
 * <p>迁移自 {@code com.zkl.scm.im.base.tencent.req.SensitiveWordRequest}。
 * 源类带 {@code @Accessors(chain = true)}, 本仓 Req 规范为纯 {@code @Data}(禁 @Builder),
 * 链式 setter 去除, 不影响 JSON 契约。</p>
 *
 * @author KC
 */
@Data
public class SensitiveWordRequest {

    /**
     * 主键ID, 修改必传
     */
    private Long id;

    /**
     * 敏感词内容
     */
    private String sensitiveWord;

    /**
     * 敏感词来源, 默认 manual
     */
    private String sourceType = "manual";
}
