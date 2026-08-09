package com.newzkl.platform.base.biz.market.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.market.domain.adapt.api.PlatformCategoryInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code GoodsCategoryApiDefaultImpl} 兜底行为测试
 *
 * @author KC
 */
class GoodsCategoryApiDefaultImplTest {

    private final GoodsCategoryApiImpl goodsCategoryApi = new GoodsCategoryApiImpl();

    @Test
    @DisplayName("兜底 platformCategoryTree: 返回非 null 空集合")
    void platformCategoryTreeReturnsEmptyList() {
        List<PlatformCategoryInfo> list = goodsCategoryApi.platformCategoryTree(10L);

        assertThat(list).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("兜底 platformCategoryTree: rootId 为 null 也不抛异常")
    void platformCategoryTreeTolerateNullRootId() {
        assertThat(goodsCategoryApi.platformCategoryTree(null)).isNotNull().isEmpty();
    }
}
