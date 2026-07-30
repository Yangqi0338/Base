package com.newzkl.platform.base.common.ddd.action.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.vo.RequestInfo;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

/**
 * 安全上下文过滤器
 *
 * <p>从请求头解析 token 并经 sa-token 取出各透传字段, 填充至 {@code SecurityContextHolder}
 * 供后续鉴权切面与业务读取; 请求结束时清理线程上下文。</p>
 *
 * <p>迁移说明: 由 {@code javax.servlet} 迁至 {@code jakarta.servlet} (Spring Boot 3);
 * 删除原调试用 {@code System.out.println}, 改用 SLF4J 日志。</p>
 *
 * @author fang
 */
@Slf4j
public class SecurityContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("SecurityContextFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = SecurityUtils.getHeader(servletRequest, "X-Real-IP");
        String role = SecurityUtils.getHeader(servletRequest, TokenConstants.ROLE);

        SecurityContextHolder.set(TokenConstants.REQUEST_INFO, new RequestInfo(DateUtil.toLocalDateTime(new Date()), ip));
        SecurityContextHolder.set(TokenConstants.ROLE, role);
        try {
            String token = getToken(servletRequest);
            try {
                if (StrUtil.isNotEmpty(token)) {
                    SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, String.valueOf(StpUtil.getExtra(token, TokenConstants.DETAILS_ACCOUNT_ID)));
                    SecurityContextHolder.set(TokenConstants.IM_USER_ACCOUNT, String.valueOf(StpUtil.getExtra(token, TokenConstants.IM_USER_ACCOUNT)));
                    SecurityContextHolder.set(TokenConstants.DETAILS_CLIENT, StpUtil.getExtra(token, TokenConstants.DETAILS_CLIENT));
                    SecurityContextHolder.set(TokenConstants.DETAILS_USERNAME, StpUtil.getExtra(token, TokenConstants.DETAILS_USERNAME));
                    SecurityContextHolder.set(TokenConstants.DETAILS_NICKNAME, StpUtil.getExtra(token, TokenConstants.DETAILS_NICKNAME));
                    SecurityContextHolder.set(TokenConstants.DETAILS_COMPANY_ROLE, StpUtil.getExtra(token, TokenConstants.DETAILS_COMPANY_ROLE));
                    SecurityContextHolder.set(TokenConstants.DETAILS_OPERATOR_ID, String.valueOf(StpUtil.getExtra(token, TokenConstants.DETAILS_OPERATOR_ID)));
                    SecurityContextHolder.set(TokenConstants.DETAILS_UP_ID, String.valueOf(StpUtil.getExtra(token, TokenConstants.DETAILS_UP_ID)));
                    SecurityContextHolder.set(TokenConstants.DETAILS_EMP_ID, String.valueOf(StpUtil.getExtra(token, TokenConstants.DETAILS_EMP_ID)));
                } else {
                    SecurityContextHolder.set(TokenConstants.DETAILS_CLIENT, SecurityUtils.getHeader(servletRequest, TokenConstants.DETAILS_CLIENT));
                }
            } catch (Exception e) {
                log.warn("子服务Token解析异常, token:{}", token);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SecurityContextHolder.remove();
        }
    }

    @Override
    public void destroy() {
        log.debug("SecurityContextFilter destroy");
    }

    /**
     * 从请求头获取 token, OPTIONS 预检请求直接放行
     *
     * @param servletRequest servlet 请求
     * @return 裁剪前缀后的 token, OPTIONS 请求返回 null
     */
    public static String getToken(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!"OPTIONS".equals(request.getMethod())) {
            String token = request.getHeader(TokenConstants.AUTHENTICATION);
            return replaceTokenPrefix(token);
        } else {
            return null;
        }
    }

    /**
     * 裁剪 token 前缀
     *
     * @param token 原始 token
     * @return 去除前缀后的 token
     */
    public static String replaceTokenPrefix(String token) {
        if (StrUtil.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }
}
