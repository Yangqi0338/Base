package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code MerchantCategoryRepositoryImpl} 单元测试 (DAO 全 mock, 不连库)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class MerchantCategoryRepositoryImplTest {

    @Mock
    private MerchantCategoryDAO merchantCategoryDAO;

    @InjectMocks
    private MerchantCategoryRepositoryImpl merchantCategoryRepository;

    @Test
    @DisplayName("batchSave: 逐条转 DO 落库, id/pid/sourceId 原样保留")
    void batchSaveKeepsIdAndPidAndSourceId() {
        CategoryReq root = new CategoryReq();
        root.setId(1000L);
        root.setPid(0L);
        root.setSourceId(11L);
        root.setAccountId(7L);
        root.setName("根");
        CategoryReq child = new CategoryReq();
        child.setId(1001L);
        child.setPid(1000L);
        child.setSourceId(1101L);
        child.setAccountId(7L);
        child.setName("子");

        merchantCategoryRepository.batchSave(Arrays.asList(root, child));

        ArgumentCaptor<MerchantCategoryDO> captor = ArgumentCaptor.forClass(MerchantCategoryDO.class);
        verify(merchantCategoryDAO, times(2)).insert(captor.capture());
        List<MerchantCategoryDO> saved = captor.getAllValues();
        assertThat(saved.get(0).getId()).isEqualTo(1000L);
        assertThat(saved.get(0).getPid()).isZero();
        assertThat(saved.get(0).getSourceId()).isEqualTo(11L);
        assertThat(saved.get(1).getPid()).isEqualTo(1000L);
        assertThat(saved.get(1).getSourceId()).isEqualTo(1101L);
        assertThat(saved.get(1).getAccountId()).isEqualTo(7L);
    }

    @Test
    @DisplayName("batchSave: 空入参不落库")
    void batchSaveSkipsEmptyList() {
        merchantCategoryRepository.batchSave(null);
        merchantCategoryRepository.batchSave(List.of());

        Mockito.verify(merchantCategoryDAO, Mockito.never()).insert(any(MerchantCategoryDO.class));
    }

    @Test
    @DisplayName("existsBySource: count>0 判定已同步")
    void existsBySourceTrueWhenCountPositive() {
        when(merchantCategoryDAO.selectCount(any())).thenReturn(1L);

        assertThat(merchantCategoryRepository.existsBySource(11L, 7L)).isTrue();
        verify(merchantCategoryDAO).getLwBySource(11L, 7L);
    }

    @Test
    @DisplayName("existsBySource: count=0 判定未同步")
    void existsBySourceFalseWhenCountZero() {
        when(merchantCategoryDAO.selectCount(any())).thenReturn(0L);

        assertThat(merchantCategoryRepository.existsBySource(11L, 7L)).isFalse();
    }

    @Test
    @DisplayName("categoryList: 复用抽象基类扁平查询, DO 转 VO")
    void categoryListTransfersDoToVo() {
        MerchantCategoryDO categoryDO = new MerchantCategoryDO();
        categoryDO.setId(1000L);
        categoryDO.setPid(0L);
        categoryDO.setName("根");
        when(merchantCategoryDAO.selectList(any())).thenReturn(List.of(categoryDO));

        List<CategoryVO> list = merchantCategoryRepository.categoryList(new CategoryQuery());

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1000L);
        assertThat(list.get(0).getPid()).isZero();
        assertThat(list.get(0).getName()).isEqualTo("根");
    }
}
