package com.newzkl.platform.base.biz.auth.model.oauth.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注册请求
 *
 * <p>register 端口专用扁平入参, 仅承载注册流程实际消费的字段, 不继承账号/身份注册基类,
 * 避免带入 companyInfo/license/storeName 等经 RPC 跳转即被丢弃的冗余字段。角色专属初始化
 * 数据由 account 侧身份初始化入参承接, 与此入参解耦</p>
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class RegisterReq {

    /**
     * 注册身份, 为空时按当前端默认身份
     */
    private AccountEnum.Identity identity;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信验证码
     */
    @NotBlank
    private String code;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String head;

    /**
     * 邀请码
     */
    private String yqm;

    /**
     * 邀请人 ID
     */
    private Long inviteId;

    /**
     * 是否同步注册 member 账号 (仅渠道商注册时生效)
     */
    private CommonEnum.YesOrNo createMember;

    /**
     * 注册后是否自动登录
     *
     * <p>默认 true: 注册成功后回登录态返回 token; false 仅注册, token 为空</p>
     */
    private Boolean login = Boolean.TRUE;
}
