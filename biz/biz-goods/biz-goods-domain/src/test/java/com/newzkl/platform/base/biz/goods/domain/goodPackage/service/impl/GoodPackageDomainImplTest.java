package com.newzkl.platform.base.biz.goods.domain.goodPackage.service.impl;

import com.newzkl.platform.base.biz.goods.domain.goodPackage.adapt.repository.GoodPackageRepository;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 商品套餐领域服务单元测试 (仓储端口 mock, 不连库)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class GoodPackageDomainImplTest {

    @Mock
    private GoodPackageRepository goodPackageRepository;

    @InjectMocks
    private GoodPackageDomainImpl goodPackageDomain;

    private GoodPackageReq req(String packageId) {
        GoodPackageReq req = new GoodPackageReq();
        req.setPackageId(packageId);
        req.setPackageName("基础套餐");
        req.setGoodsNum(10L);
        req.setPackagePrice(9900);
        return req;
    }

    @Test
    void create_returnsIdAndDefaultsStateEnabled() {
        when(goodPackageRepository.existsByPackageId("PKG001")).thenReturn(false);
        when(goodPackageRepository.save(any(GoodPackageReq.class))).thenReturn(5L);

        Long id = goodPackageDomain.create(req("PKG001"));

        assertThat(id).isEqualTo(5L);
        ArgumentCaptor<GoodPackageReq> captor = ArgumentCaptor.forClass(GoodPackageReq.class);
        verify(goodPackageRepository).save(captor.capture());
        assertThat(captor.getValue().getState()).isEqualTo(1);
    }

    @Test
    void create_throwsWhenPackageIdDuplicated() {
        when(goodPackageRepository.existsByPackageId("PKG001")).thenReturn(true);

        assertThatThrownBy(() -> goodPackageDomain.create(req("PKG001")))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("套餐ID已存在");
        verify(goodPackageRepository, never()).save(any(GoodPackageReq.class));
    }

    @Test
    void update_savesWhenExists() {
        GoodPackageReq req = req("PKG001");
        req.setId(5L);
        GoodPackageVO existing = new GoodPackageVO();
        existing.setId(5L);
        when(goodPackageRepository.findById(5L)).thenReturn(existing);

        goodPackageDomain.update(req);

        verify(goodPackageRepository).save(req);
    }

    @Test
    void update_throwsWhenNotExists() {
        GoodPackageReq req = req("PKG001");
        req.setId(404L);
        when(goodPackageRepository.findById(404L)).thenReturn(null);

        assertThatThrownBy(() -> goodPackageDomain.update(req))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("套餐不存在");
    }

    @Test
    void enable_updatesStateToOne() {
        GoodPackageVO existing = new GoodPackageVO();
        existing.setId(5L);
        when(goodPackageRepository.findById(5L)).thenReturn(existing);

        goodPackageDomain.enable(5L);

        verify(goodPackageRepository).updateState(5L, 1);
    }

    @Test
    void disable_updatesStateToZero() {
        GoodPackageVO existing = new GoodPackageVO();
        existing.setId(5L);
        when(goodPackageRepository.findById(5L)).thenReturn(existing);

        goodPackageDomain.disable(5L);

        verify(goodPackageRepository).updateState(5L, 0);
    }

    @Test
    void enable_throwsWhenNotExists() {
        when(goodPackageRepository.findById(404L)).thenReturn(null);

        assertThatThrownBy(() -> goodPackageDomain.enable(404L))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("套餐不存在");
    }

    @Test
    void detailByPackageId_throwsWhenNotExists() {
        when(goodPackageRepository.findByPackageId("NOPE")).thenReturn(null);

        assertThatThrownBy(() -> goodPackageDomain.detailByPackageId("NOPE"))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("套餐不存在");
    }

    @Test
    void detailByPackageId_returnsRepositoryResult() {
        GoodPackageVO vo = new GoodPackageVO();
        vo.setPackageId("PKG001");
        when(goodPackageRepository.findByPackageId("PKG001")).thenReturn(vo);

        assertThat(goodPackageDomain.detailByPackageId("PKG001").getPackageId()).isEqualTo("PKG001");
    }

    @Test
    void page_delegatesToRepository() {
        GoodPackageQuery query = new GoodPackageQuery();
        goodPackageDomain.page(query);
        verify(goodPackageRepository).page(query);
    }
}
