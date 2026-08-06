package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodPackageDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodPackageDO;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 商品套餐仓储实现单元测试 (DAO mock, 不连库)
 *
 * <p>回归 new-scm 坏桩: 旧 {@code GoodPackageRepositoryImpl.save()} 直接
 * {@code return null} 且 insert/update 被注释, {@code goodPackageMapper} 未注入。
 * 本测试锁死 save 必须真实落库并返回非 null 主键。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class GoodPackageRepositoryImplTest {

    @Mock
    private GoodPackageDAO goodPackageDAO;

    @InjectMocks
    private GoodPackageRepositoryImpl goodPackageRepository;

    private GoodPackageReq req() {
        GoodPackageReq req = new GoodPackageReq();
        req.setPackageId("PKG001");
        req.setPackageName("基础套餐");
        req.setGoodsNum(10L);
        req.setPackagePrice(Money.of(9900));
        req.setState(1);
        return req;
    }

    @Test
    void save_insertsWhenIdAbsentAndReturnsNonNullId() {
        doAnswer(invocation -> {
            GoodPackageDO arg = invocation.getArgument(0);
            arg.setId(31L);
            return 1;
        }).when(goodPackageDAO).insert(any(GoodPackageDO.class));

        Long id = goodPackageRepository.save(req());

        assertThat(id).isNotNull().isEqualTo(31L);
        verify(goodPackageDAO, never()).updateById(any(GoodPackageDO.class));
    }

    @Test
    void save_updatesWhenIdPresentAndReturnsNonNullId() {
        GoodPackageReq req = req();
        req.setId(31L);

        Long id = goodPackageRepository.save(req);

        assertThat(id).isNotNull().isEqualTo(31L);
        verify(goodPackageDAO).updateById(any(GoodPackageDO.class));
        verify(goodPackageDAO, never()).insert(any(GoodPackageDO.class));
    }

    @Test
    void save_transfersAllBusinessColumns() {
        doAnswer(invocation -> {
            GoodPackageDO arg = invocation.getArgument(0);
            arg.setId(31L);
            return 1;
        }).when(goodPackageDAO).insert(any(GoodPackageDO.class));

        goodPackageRepository.save(req());

        ArgumentCaptor<GoodPackageDO> captor = ArgumentCaptor.forClass(GoodPackageDO.class);
        verify(goodPackageDAO).insert(captor.capture());
        GoodPackageDO saved = captor.getValue();
        assertThat(saved.getPackageId()).isEqualTo("PKG001");
        assertThat(saved.getPackageName()).isEqualTo("基础套餐");
        assertThat(saved.getGoodsNum()).isEqualTo(10L);
        assertThat(saved.getPackagePrice()).isEqualTo(9900);
        assertThat(saved.getState()).isEqualTo(1);
    }

    @Test
    void existsByPackageId_trueWhenCountPositive() {
        when(goodPackageDAO.selectCount(any())).thenReturn(1L);

        assertThat(goodPackageRepository.existsByPackageId("PKG001")).isTrue();
    }

    @Test
    void existsByPackageId_falseWhenCountZero() {
        when(goodPackageDAO.selectCount(any())).thenReturn(0L);

        assertThat(goodPackageRepository.existsByPackageId("PKG001")).isFalse();
    }

    @Test
    void findByPackageId_mapsDoToVo() {
        GoodPackageDO packageDO = new GoodPackageDO();
        packageDO.setId(31L);
        packageDO.setPackageId("PKG001");
        packageDO.setPackageName("基础套餐");
        when(goodPackageDAO.selectOne(any())).thenReturn(packageDO);

        GoodPackageVO vo = goodPackageRepository.findByPackageId("PKG001");

        assertThat(vo).isNotNull();
        assertThat(vo.getPackageName()).isEqualTo("基础套餐");
    }

    @Test
    void findById_returnsNullWhenAbsent() {
        when(goodPackageDAO.selectById(404L)).thenReturn(null);

        assertThat(goodPackageRepository.findById(404L)).isNull();
    }

    @Test
    void updateState_writesStateById() {
        goodPackageRepository.updateState(31L, 0);

        ArgumentCaptor<GoodPackageDO> captor = ArgumentCaptor.forClass(GoodPackageDO.class);
        verify(goodPackageDAO).updateById(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(31L);
        assertThat(captor.getValue().getState()).isEqualTo(0);
    }
}
