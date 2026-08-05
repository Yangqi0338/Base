package com.newzkl.platform.base.biz.course.domain.adapt.api;

/**
 * 用户账号出站端口
 *
 * <p>替代旧实现中 controller/service 直连 {@code SecurityUtils.getAccountId()} 与
 * user 域的用户校验。观看记录与关注讲师两个切片依赖登录用户身份, 经本端口取值,
 * 不直连 biz-user/biz-account 的 domain。</p>
 *
 * <p>默认实现 {@code UserAccountApiDefaultImpl} 为兜底(取不到当前用户返回 null,
 * 校验恒放行), 真实实现由入口 starter 以 Dubbo consumer 覆写。</p>
 *
 * @author KC
 */
public interface UserAccountApi {

    /**
     * 校验用户是否存在
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsUser(Long userId);
}
