package com.newzkl.platform.base.common.core.utils.biz;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.vo.RequestInfo;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * 权限获取工具类
 *
 * <p>迁移说明: 原 {@code getRole()} 依赖业务枚举 {@code RoleEnum.CompanyRole}，
 * 已随业务枚举下沉到业务层，通用层仅保留 {@link SecurityUtils#getRoleId}。</p>
 *
 * @author ruoyi
 */
public class SecurityUtils {
    public static String getHeader(ServletRequest servletRequest, String name) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!"OPTIONS".equals(request.getMethod())) {
            return request.getHeader(name);
        } else {
            return null;
        }
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static Long getAccountId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_ACCOUNT_ID, String.class);
        return NumberUtil.parseLong(s, null);
    }

    /**
     * 获取当前员工ID
     *
     * <p>主账号无员工ID, 返回 {@code null}; 员工账号返回其员工ID。</p>
     *
     * @return 员工ID, 主账号或未设置时返回 null
     */
    public static Long getEmpId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_EMP_ID, String.class);
        if (StrUtil.isEmpty(s) || "null".equals(s)) {
            return null;
        }
        return Long.parseLong(s);
    }

    /**
     * 获取当前上级运营商ID
     *
     * @return 运营商ID, 未设置时返回 null
     */
    public static Long getOperatorId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_OPERATOR_ID, String.class);
        if (StrUtil.isEmpty(s) || "null".equals(s)) {
            return null;
        }
        return Long.parseLong(s);
    }

    /**
     * 获取当前 IM 用户账号
     *
     * @return IM 用户账号
     */
    public static String getImUserAccount() {
        return SecurityContextHolder.get(TokenConstants.IM_USER_ACCOUNT, String.class);
    }

    public static Long getUpId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_UP_ID, String.class);
        return Long.parseLong(s);
    }

    public static CommonEnum.Client getClient() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_CLIENT, String.class);
        return CommonEnum.Client.getByCode(s);
    }

    public static String getUsername() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_USERNAME, String.class);
        return s;
    }

    public static String getNickName() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_NICKNAME, String.class);
        return s;
    }

    public static Long getRoleId() {
        String role = SecurityContextHolder.get(TokenConstants.ROLE, String.class);
        if (StrUtil.isBlank(role)) {
            return null;
        }
        return Long.parseLong(role);
    }

    public static RoleEnum.CompanyRole getRole() {
        return RoleEnum.CompanyRole.getByCode(getRoleId());
    }

    public static RequestInfo getRequestInfo() {
        return SecurityContextHolder.get(TokenConstants.REQUEST_INFO, RequestInfo.class);
    }

    public static String getSkipMode() {
        String skipMode = SecurityContextHolder.get(TokenConstants.SKIP_MODE, String.class);
        if (StrUtil.isEmpty(skipMode)) {
            return "none";
        }
        return skipMode;
    }
}
