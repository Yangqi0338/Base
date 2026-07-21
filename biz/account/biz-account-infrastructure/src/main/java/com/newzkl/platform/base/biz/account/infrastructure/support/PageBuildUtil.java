package com.newzkl.platform.base.biz.account.infrastructure.support;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;

/**
 * 分页构建工具 (account infra).
 * <p>新 common {@link PageQuery} 不再耦合 mybatis-plus {@code Page},
 * 原 {@code PageQuery.getPage()} 迁移至基础设施层此工具。</p>
 *
 * @author KC
 */
public final class PageBuildUtil {

    private PageBuildUtil() {
    }

    /**
     * 由分页查询构建 mybatis-plus 分页对象.
     *
     * @param query 分页查询
     * @param <T>   记录类型
     * @return 分页对象
     */
    public static <T> Page<T> of(PageQuery query) {
        Integer pageSize = query.getPageSize();
        return new Page<>(query.getPageNo(), pageSize, pageSize != null && pageSize != Integer.MAX_VALUE);
    }
}
