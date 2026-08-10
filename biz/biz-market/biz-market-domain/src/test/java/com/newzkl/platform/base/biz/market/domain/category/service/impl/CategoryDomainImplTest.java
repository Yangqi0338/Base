package com.newzkl.platform.base.biz.market.domain.category.service.impl;

import com.newzkl.platform.base.biz.market.domain.adapt.api.GoodsCategoryApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.PlatformCategoryInfo;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryEditReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategorySyncReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code CategoryDomainImpl} 单元测试 (仓储端口 / 出站端口全 mock, 不连库)
 *
 * <p>重点验证 {@code code/pcode -> pid} 两趟 remap: 子节点 pid 必须指向根节点的
 * 新雪花 id, 而非平台 pid。</p>
 *
 * @author KC
 */
class CategoryDomainImplTest {

    private static final Long ACCOUNT_ID = 7L;


    private GoodsCategoryApi goodsCategoryApi;


    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        goodsCategoryApi = Mockito.mock(GoodsCategoryApi.class);
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getAccountId).thenReturn(ACCOUNT_ID);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    @Test
    @DisplayName("syncCategory: 两趟 remap 后子节点 pid 指向根的新 id, 平台 id 落 sourceId")
    void syncCategoryRemapsPlatformIdToNewPid() {
        when(goodsCategoryApi.platformCategoryTree(11L))
                .thenReturn(Arrays.asList(platform(11L, 0L, "根"), platform(1101L, 11L, "子一"), platform(1102L, 11L, "子二")));
        CategorySyncReq req = new CategorySyncReq();
        req.setId(1101L);


        ArgumentCaptor<List<CategoryReq>> captor = ArgumentCaptor.forClass(List.class);
        List<CategoryReq> saved = captor.getValue();
        assertThat(saved).hasSize(3);
        CategoryReq root = saved.get(0);
        CategoryReq childOne = saved.get(1);
        CategoryReq childTwo = saved.get(2);
        // 根节点 pid 为 0
        assertThat(root.getPid()).isZero();
        // 子节点 pid 指向根的新 id, 而非平台 pid 11
        assertThat(childOne.getPid()).isEqualTo(root.getId());
        assertThat(childTwo.getPid()).isEqualTo(root.getId());
        assertThat(childOne.getPid()).isNotEqualTo(11L);
        // 三条新 id 互不相同, 且都不等于平台 id
        assertThat(Arrays.asList(root.getId(), childOne.getId(), childTwo.getId()))
                .doesNotHaveDuplicates()
                .doesNotContain(11L, 1101L, 1102L);
        // 平台源 id 落 sourceId
        assertThat(root.getSourceId()).isEqualTo(11L);
        assertThat(childOne.getSourceId()).isEqualTo(1101L);
        assertThat(childTwo.getSourceId()).isEqualTo(1102L);
        // 账号与来源类型
        assertThat(root.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(root.getType()).isZero();
    }

    @Test
    @DisplayName("syncCategory: 入参 id 取前两位作为平台根分类ID (保留旧行为)")
    void syncCategoryTruncatesRootIdToTwoDigits() {
        Mockito.verifyNoInteractions(goodsCategoryApi);
    }

    private PlatformCategoryInfo platform(Long id, Long pid, String name) {
        PlatformCategoryInfo info = new PlatformCategoryInfo();
        info.setId(id);
        info.setPid(pid);
        info.setName(name);
        return info;
    }

    private CategoryVO vo(Long id, Long pid, String name) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(id);
        categoryVO.setPid(pid);
        categoryVO.setName(name);
        return categoryVO;
    }
}
