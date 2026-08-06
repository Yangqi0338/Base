package com.newzkl.platform.base.biz.market.domain.category.service.impl;

import com.newzkl.platform.base.biz.market.domain.adapt.api.GoodsCategoryApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.PlatformCategoryInfo;
import com.newzkl.platform.base.biz.market.domain.category.repository.MerchantCategoryRepository;
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

    private MerchantCategoryRepository merchantCategoryRepository;

    private GoodsCategoryApi goodsCategoryApi;

    private CategoryDomainImpl categoryDomain;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        merchantCategoryRepository = Mockito.mock(MerchantCategoryRepository.class);
        goodsCategoryApi = Mockito.mock(GoodsCategoryApi.class);
        categoryDomain = new CategoryDomainImpl(merchantCategoryRepository, goodsCategoryApi);
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
        when(merchantCategoryRepository.existsBySource(11L, ACCOUNT_ID)).thenReturn(false);
        when(goodsCategoryApi.platformCategoryTree(11L))
                .thenReturn(Arrays.asList(platform(11L, 0L, "根"), platform(1101L, 11L, "子一"), platform(1102L, 11L, "子二")));
        CategorySyncReq req = new CategorySyncReq();
        req.setId(1101L);

        categoryDomain.syncCategory(req);

        ArgumentCaptor<List<CategoryReq>> captor = ArgumentCaptor.forClass(List.class);
        verify(merchantCategoryRepository).batchSave(captor.capture());
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
        when(merchantCategoryRepository.existsBySource(11L, ACCOUNT_ID)).thenReturn(true);
        CategorySyncReq req = new CategorySyncReq();
        req.setId(110203L);

        categoryDomain.syncCategory(req);

        verify(merchantCategoryRepository).existsBySource(11L, ACCOUNT_ID);
        Mockito.verifyNoInteractions(goodsCategoryApi);
    }

    @Test
    @DisplayName("syncCategory: 已同步过则幂等返回")
    void syncCategorySkipsWhenAlreadySynced() {
        when(merchantCategoryRepository.existsBySource(11L, ACCOUNT_ID)).thenReturn(true);
        CategorySyncReq req = new CategorySyncReq();
        req.setId(11L);

        categoryDomain.syncCategory(req);

        verify(merchantCategoryRepository, never()).batchSave(anyList());
    }

    @Test
    @DisplayName("syncCategory: 平台返回空则不落库")
    void syncCategorySkipsWhenPlatformEmpty() {
        when(merchantCategoryRepository.existsBySource(11L, ACCOUNT_ID)).thenReturn(false);
        when(goodsCategoryApi.platformCategoryTree(11L)).thenReturn(List.of());
        CategorySyncReq req = new CategorySyncReq();
        req.setId(11L);

        categoryDomain.syncCategory(req);

        verify(merchantCategoryRepository, never()).batchSave(anyList());
    }

    @Test
    @DisplayName("syncCategory: id 为 null 或不足两位抛参数异常")
    void syncCategoryThrowsOnInvalidId() {
        assertThatThrownBy(() -> categoryDomain.syncCategory(new CategorySyncReq()))
                .isInstanceOf(PlatformException.class);
        CategorySyncReq shortReq = new CategorySyncReq();
        shortReq.setId(5L);
        assertThatThrownBy(() -> categoryDomain.syncCategory(shortReq))
                .isInstanceOf(PlatformException.class);
    }

    @Test
    @DisplayName("categorySave: 分配雪花 id, 默认顶层 + 自营类型")
    void categorySaveAssignsIdAndDefaults() {
        CategoryReq req = new CategoryReq();
        req.setName("自营分类");

        Long id = categoryDomain.categorySave(req);

        ArgumentCaptor<CategoryReq> captor = ArgumentCaptor.forClass(CategoryReq.class);
        verify(merchantCategoryRepository).categorySave(captor.capture());
        assertThat(id).isNotNull().isEqualTo(captor.getValue().getId());
        assertThat(captor.getValue().getPid()).isZero();
        assertThat(captor.getValue().getType()).isEqualTo(1);
        assertThat(captor.getValue().getAccountId()).isEqualTo(ACCOUNT_ID);
    }

    @Test
    @DisplayName("categoryEdit: id 回填到分类内容后交由仓储更新")
    void categoryEditFillsId() {
        CategoryEditReq req = new CategoryEditReq();
        req.setId(88L);
        CategoryReq category = new CategoryReq();
        category.setName("改名");
        req.setCategory(category);

        categoryDomain.categoryEdit(req);

        ArgumentCaptor<CategoryReq> captor = ArgumentCaptor.forClass(CategoryReq.class);
        verify(merchantCategoryRepository).categoryEdit(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(88L);
        assertThat(captor.getValue().getName()).isEqualTo("改名");
    }

    @Test
    @DisplayName("categoryEdit: 缺 id 或缺内容抛参数异常")
    void categoryEditThrowsOnInvalidReq() {
        assertThatThrownBy(() -> categoryDomain.categoryEdit(new CategoryEditReq()))
                .isInstanceOf(PlatformException.class);
    }

    @Test
    @DisplayName("categoryDelete: 空列表不落库")
    void categoryDeleteSkipsEmpty() {
        categoryDomain.categoryDelete(null);
        categoryDomain.categoryDelete(List.of());

        verify(merchantCategoryRepository, never()).categoryDelete(anyList());
    }

    @Test
    @DisplayName("categoryTree: 扁平列表按 id/pid 组装为树")
    void categoryTreeBuildsHierarchy() {
        when(merchantCategoryRepository.categoryList(any()))
                .thenReturn(Arrays.asList(vo(1L, 0L, "根"), vo(2L, 1L, "子一"), vo(3L, 1L, "子二"), vo(4L, 2L, "孙")));

        List<CategoryVO> tree = categoryDomain.categoryTree(new CategoryQuery());

        assertThat(tree).hasSize(1);
        CategoryVO root = tree.get(0);
        assertThat(root.getId()).isEqualTo(1L);
        assertThat(root.getChildren()).hasSize(2);
        assertThat(root.getChildren().get(0).getChildren()).hasSize(1);
        assertThat(root.getChildren().get(0).getChildren().get(0).getId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("categoryTree: 空结果返回空集合而非 null")
    void categoryTreeReturnsEmptyList() {
        when(merchantCategoryRepository.categoryList(any())).thenReturn(null);

        assertThat(categoryDomain.categoryTree(new CategoryQuery())).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("categoryTree: 父节点缺失的孤儿节点提升为顶层")
    void categoryTreeLiftsOrphanToRoot() {
        when(merchantCategoryRepository.categoryList(any()))
                .thenReturn(Arrays.asList(vo(1L, 0L, "根"), vo(9L, 999L, "孤儿")));

        List<CategoryVO> tree = categoryDomain.categoryTree(new CategoryQuery());

        assertThat(tree).hasSize(2);
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
