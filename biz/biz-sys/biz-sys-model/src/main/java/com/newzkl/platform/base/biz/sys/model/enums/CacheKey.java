package com.newzkl.platform.base.biz.sys.model.enums;

/**
 * sys 域缓存键
 *
 * <p>占位符为 hutool {@code StrUtil.format} 风格的 <code>{}</code>。</p>
 *
 * @author KC
 */
public interface CacheKey {

    /**
     * 账号已表示意向的项目 id 集合, 参数: accountId
     */
    String INTEREST_PROJECT = "project:interest:{}";

    /**
     * 项目意向人数计数, 参数: 项目 id
     */
    String PROJECT_INTEREST_NUM = "project:interestNum:{}";
}
