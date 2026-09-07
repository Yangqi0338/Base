package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工修改入参
 *
 * <p>员工修改仅允许改基础信息与员工类型, 不含登录账号/密码/邀请码/层级/登录时间/状态/角色。
 * 除 {@code type} 外均为账号侧字段, 端内转 {@link AccountReq} 落 account 表,
 * identity 由 domain 固定 EMP</p>
 *
 * @author KC
 * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmpSaveCommand extends BaseReq {
    /**
     * 员工类型 (MANAGER 管理员 / SIMPLE 普通)
     *
     * @ext 落 emp 表, 唯一的员工自有列
     */
    private AccountEnum.EmpType type;
    /**
     * 昵称
     *
     * @ext 落 account 表, 非 emp 自有列
     */
    private String nickname;
    /**
     * 真实姓名
     *
     * @ext 落 account 表, 非 emp 自有列
     */
    private String realName;
    /**
     * 手机号
     *
     * @ext 落 account 表, 非 emp 自有列
     */
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 头像
     *
     * @ext 落 account 表, 非 emp 自有列
     */
    private String head;
    /**
     * 父id
     *
     * @ext 落 account 表, 非 emp 自有列
     */
    private Long pid;
}
