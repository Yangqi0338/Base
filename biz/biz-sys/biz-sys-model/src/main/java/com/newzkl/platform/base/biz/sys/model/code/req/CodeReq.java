package com.newzkl.platform.base.biz.sys.model.code.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 短信验证码入参
 *
 * <p>迁移自 new-scm {@code message.application.model.req.CodeReq}。
 * {@code type} 保持字符串编码 (线上前端按 "2004"/"2005" 之类裸码提交), 不改为枚举。</p>
 *
 * @author KC
 */
@Data
public class CodeReq {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 短信类型编码
     *
     * @ext 候选值: 验证码登录 2003; 手机号换绑 2004; 修改密码 2005; 渠道商注册 2006
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 模板参数
     */
    private List<String> params;

    /**
     * 三方短信模板号
     */
    private String templateId;
}
