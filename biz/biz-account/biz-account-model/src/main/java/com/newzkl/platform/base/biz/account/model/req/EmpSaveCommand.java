package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工修改入参
 *
 * <p>员工修改仅允许改基础信息, 不含登录账号/密码/邀请码/层级/登录时间/状态/角色。
 * 端内转 {@link AccountReq} 落库, identity 由 controller 固定 EMP</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmpSaveCommand extends BaseReq {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 手机号
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 头像
     */
    private String head;
    /**
     * 父id
     */
    private Long pid;
}
