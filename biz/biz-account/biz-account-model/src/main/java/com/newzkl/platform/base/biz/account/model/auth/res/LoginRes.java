package com.newzkl.platform.base.biz.account.model.auth.res;

import com.newzkl.platform.base.common.ddd.model.enums.account.AuthEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.res.LoginAccountRes;
import lombok.Data;

/**
 * 登录响应结果
 *
 * @author muc_fang
 * @date 2024/2/22 11:23
 */
@Data
public class LoginRes {
    /**
     * 账号信息
     */
    private LoginAccountRes accountVO;
    /**
     * Token
     */
    private String token;
    /**
     * 账号类型
     */
    private AccountEnum.SubUserType accountType;
    /**
     * 员工类型, 空 表示 非员工
     */
    private AuthEnum.EmpType empType;
    /**
     * 客户端
     */
    private CommonEnum.Client client;
    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;
    /**
     * 签名
     */
    private String userSign;
    /**
     * 腾讯IM sdkAppId
     */
    private Long sdkAppId;
    /**
     * 腾讯IM 用户标识
     */
    private String identifier;
}
