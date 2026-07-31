package com.newzkl.platform.base.biz.goods.domain.freight.service.impl;

import com.newzkl.platform.base.biz.goods.domain.freight.repository.FreightTemplateRepository;
import com.newzkl.platform.base.biz.goods.model.goods.req.freight.FreightTemplateReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

/**
 * 运费模板领域服务单元测试 (仓储端口 mock, 不连库)
 *
 * <p>重点验证: DB 写与 Redis 缓存清由领域层按序编排</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class FreightDomainImplTest {

    @Mock
    private FreightTemplateRepository freightTemplateRepository;

    @InjectMocks
    private FreightDomainImpl freightDomain;

    @Test
    void freightTemplateEdit_updatesThenEvictsCache() {
        FreightTemplateReq req = new FreightTemplateReq();
        req.setId(3L);

        freightDomain.freightTemplateEdit(req);

        var order = inOrder(freightTemplateRepository);
        order.verify(freightTemplateRepository).freightTemplateEdit(any());
        order.verify(freightTemplateRepository).evictFreightTemplateCache(List.of(3L));
    }

    @Test
    void freightTemplateDelete_deletesThenEvictsCache() {
        List<Long> idList = List.of(1L, 2L);

        freightDomain.freightTemplateDelete(idList);

        var order = inOrder(freightTemplateRepository);
        order.verify(freightTemplateRepository).freightTemplateDelete(idList);
        order.verify(freightTemplateRepository).evictFreightTemplateCache(idList);
    }
}
