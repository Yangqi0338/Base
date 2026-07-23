package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.AssertTrue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
@EqualsAndHashCode
public class OpenSubAccountExcelData {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 公司角色
     */
    private String companyRole;
    /**
     * 员工角色
     */
    private String empRole;

    @AssertTrue(message = "用户名|昵称|角色不能为同时为空")
    public boolean certificateCheck() {
        return StrUtil.hasBlank(nickname, username, companyRole);
    }
}