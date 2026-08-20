package com.newzkl.platform.base.biz.auth.model.oauth.req;


import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 身份自行注册请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class IdentityCustomSaveReq extends IdentitySaveReq {

    /**
     * 上级账号(用户账号定位上级, 取其邀请码)
     */
    private String superiorAccount;

    /**
     * 邀请码
     */
    private String yqm;

    /**
     * 注册后是否自动登录
     *
     * <p>默认 true: 注册成功后回登录态返回 token; false 仅注册, token 为空</p>
     */
    private Boolean login = Boolean.TRUE;

    /**
     * 登录类型
     */
    @NotNull(message = "登录类型不能为空")
    private AuthEnum.Type type;

    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;

    /**
     * 头像
     */
    private String head;
}
