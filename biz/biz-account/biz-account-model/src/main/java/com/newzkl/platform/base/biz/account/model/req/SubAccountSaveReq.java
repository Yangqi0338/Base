package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 主账号新增/编辑子账号请求
 *
 * <p>id 为空走新增(此时 password 必填, 服务层手动校验), id 非空走编辑(改昵称/手机号/角色,
 * password 非空则一并重置)。等同 adopt-chicken {@code EmpController} 的员工创建编辑</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubAccountSaveReq extends BaseReq {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    @NotBlank
    @Pattern(regexp = PatternUtil.MOBILE, message = "手机号格式错误")
    private String phone;
    /**
     * 分配的角色 id 集合
     */
    private List<Long> roleIds;
}
