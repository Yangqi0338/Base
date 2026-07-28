package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.category.service.CategoryDomain;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link CategoryController} 单元测试 (领域服务 mock, 不起 Spring 容器)。
 *
 * @author KC
 */
class CategoryControllerTest {

    private static final Long ACCOUNT_ID = 7L;

    private CategoryDomain categoryDomain;

    private CategoryController categoryController;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        categoryDomain = Mockito.mock(CategoryDomain.class);
        categoryController = new CategoryController(categoryDomain);
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getAccountId).thenReturn(ACCOUNT_ID);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    @Test
    @DisplayName("categoryList: 回填当前账号并返回树形结果")
    void categoryListFillsAccountIdAndReturnsTree() {
        CategoryVO root = new CategoryVO();
        root.setId(1L);
        CategoryVO child = new CategoryVO();
        child.setId(2L);
        child.setPid(1L);
        List<CategoryVO> children = new ArrayList<>();
        children.add(child);
        root.setChildren(children);
        when(categoryDomain.categoryTree(any(CategoryQuery.class))).thenReturn(List.of(root));

        List<CategoryVO> data = categoryController.categoryList(new CategoryQuery()).getData();

        ArgumentCaptor<CategoryQuery> captor = ArgumentCaptor.forClass(CategoryQuery.class);
        verify(categoryDomain).categoryTree(captor.capture());
        assertThat(captor.getValue().getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getChildren()).hasSize(1);
        assertThat(data.get(0).getChildren().get(0).getId()).isEqualTo(2L);
    }
}
