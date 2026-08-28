package com.newzkl.platform.base.biz.auth.action.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.properties.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

/**
 * Sa-Token 权限认证配置 (servlet)
 *
 * <p>SCM 为 servlet 应用。JWT simple 模式 {@link StpLogic} + 全局鉴权过滤器 {@link SaServletFilter}。
 * 过滤器职责: nacos 动态放行 (platform.secure.skipUrl, {@link AuthProperties} @RefreshScope) → 登录校验 →
 * client 校验; setBeforeAuth 处理 CORS 响应头与 OPTIONS 预检快速返回。原网关 reactor 写法 (SaReactorFilter)
 * 按 servlet 语义等价移植, WebFlux 那套收归未来 plugin-gateway</p>
 *
 * @author KC
 */
@Configuration
@Slf4j
public class SaTokenConfigure {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * JWT simple 模式 StpLogic
     *
     * @return StpLogic
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * Sa-Token 全局鉴权过滤器 (servlet)
     *
     * @return 全局过滤器
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 拦截全部 path
                .addInclude("/**")
                // 开放地址
                .addExclude("/favicon.ico")
                // 鉴权方法: 每次访问进入
                .setAuth(obj -> {
                    SaRequest request = SaHolder.getRequest();
                    String path = request.getRequestPath();
                    // 匹配跳过认证地址 (nacos 动态)
                    if (isSkipAuth(path)) {
                        log.warn("越过认证检查, path:" + path);
                    } else {
                        String token = request.getHeader(TokenConstants.AUTHENTICATION);
                        if (StrUtil.isEmpty(token)) {
                            throw new RuntimeException("未能读取到有效 token");
                        }
                        // 登录检查
                        StpUtil.checkLogin();
                    }
                })
                // 异常处理方法: 每次 setAuth 出现异常时进入
                .setError(e -> {
                    String message = e.getMessage();
                    if (StrUtil.isNotBlank(message)) {
                        if (message.contains("未能读取到有效 token")) {
                            return new SaResult(AccountErrorCode.NO_TOKEN.getCode(), AccountErrorCode.NO_TOKEN.getMessage(), null);
                        } else if (message.contains("token 无效")) {
                            log.warn(message);
                            return new SaResult(AccountErrorCode.EXPIRE.getCode(), AccountErrorCode.EXPIRE.getMessage(), null);
                        }
                    }
                    return new SaResult(AccountErrorCode.AUTH_ERROR.getCode(), message, null);
                })
                // 前置函数: 每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // 允许跨域访问
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setHeader("Access-Control-Max-Age", "3600");
                    // 预检请求立即返回
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> log.debug("OPTIONS 预检请求, 不做处理"))
                            .back();
                });
    }

    /**
     * 是否跳过鉴权
     *
     * @param path 请求路径
     * @return 命中放行集则 true
     */
    private boolean isSkipAuth(String path) {
        return AuthProperties.skipUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }
}
