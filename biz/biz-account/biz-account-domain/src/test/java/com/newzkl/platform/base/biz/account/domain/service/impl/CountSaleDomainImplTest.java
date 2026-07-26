package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CountSaleRepository;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.req.CountSaleReq;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link CountSaleDomainImpl} 行为测试。
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("销售统计领域服务")
class CountSaleDomainImplTest {

    @Mock
    private CountSaleRepository countSaleRepository;

    @InjectMocks
    private CountSaleDomainImpl countSaleDomain;

    @Test
    @DisplayName("新建: 领域层生成雪花主键后透传仓储")
    void saveShouldAssignSnowflakeId() {
        when(countSaleRepository.save(any())).thenReturn(1001L);
        CountSaleReq req = new CountSaleReq();
        req.setAccountId(66L);
        req.setRole("1001");
        req.setTotalOrderNumber(3);

        Long id = countSaleDomain.save(req);

        assertEquals(1001L, id);
        ArgumentCaptor<CountSaleVO> captor = ArgumentCaptor.forClass(CountSaleVO.class);
        verify(countSaleRepository).save(captor.capture());
        assertNotNull(captor.getValue().getId(), "主键应由领域层生成");
        assertEquals(66L, captor.getValue().getAccountId());
        assertEquals("1001", captor.getValue().getRole());
        assertEquals(3, captor.getValue().getTotalOrderNumber());
    }

    @Test
    @DisplayName("修改: 以入参 id 为更新目标")
    void editShouldUseGivenId() {
        when(countSaleRepository.edit(any())).thenReturn(1);
        CountSaleReq req = new CountSaleReq();
        req.setTotalOrderAmount(5000);

        int rows = countSaleDomain.edit(777L, req);

        assertEquals(1, rows);
        ArgumentCaptor<CountSaleVO> captor = ArgumentCaptor.forClass(CountSaleVO.class);
        verify(countSaleRepository).edit(captor.capture());
        assertEquals(777L, captor.getValue().getId());
        assertEquals(5000, captor.getValue().getTotalOrderAmount());
    }

    @Test
    @DisplayName("删除: 透传 ID 列表")
    void deleteShouldDelegate() {
        when(countSaleRepository.delete(List.of(1L, 2L))).thenReturn(2);

        assertEquals(2, countSaleDomain.delete(List.of(1L, 2L)));
    }

    @Test
    @DisplayName("详情: 记录不存在时返回 null")
    void detailShouldReturnNullWhenAbsent() {
        when(countSaleRepository.detail(9L)).thenReturn(null);

        assertNull(countSaleDomain.detail(9L));
    }

    @Test
    @DisplayName("按条件查询: 透传查询条件")
    void findByQueryShouldDelegate() {
        CountSaleQuery query = new CountSaleQuery();
        query.setAccountId(66L);
        query.setDate(LocalDateTime.of(2026, 7, 26, 0, 0));
        CountSaleVO vo = new CountSaleVO();
        vo.setId(5L);
        when(countSaleRepository.findByQuery(query)).thenReturn(vo);

        assertEquals(5L, countSaleDomain.findByQuery(query).getId());
    }

    @Test
    @DisplayName("分页: 透传查询条件并原样返回分页")
    void pageListShouldDelegate() {
        CountSaleQuery query = new CountSaleQuery();
        Page<CountSaleVO> page = new Page<>();
        page.setTotal(3);
        when(countSaleRepository.pageList(query)).thenReturn(page);

        assertEquals(3, countSaleDomain.pageList(query).getTotal());
    }
}
