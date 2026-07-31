package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.goods.facade.ISpuFacade;
import com.newzkl.platform.base.biz.goods.facade.model.SkuQuery;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsSpuApi;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSkuVO;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code OperatorApi} 的跨域实现
 *
 * <p>经 {@code OperatorFacade} 调 biz-account 的运营商能力。对等旧
 * {@code @DubboReference IOperatorFacade}: Base 当前为单体, facade 实现
 * ({@code OperatorFacadeProvider}) 与本类同上下文, 直接按接口注入即可;
 * 将来拆服务时改为远程 consumer, 本类与领域层零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpuApiImpl implements GoodsSpuApi {

    @Autowired
    private ISpuFacade spuFacade;

    @Override
    public List<ApiSkuVO> querySkuIdListByOutId(List<String> skuIdList) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setIdList(skuIdList.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList()));
        return TransferUtils.transfers(spuFacade.skuVOList(skuQuery), ApiSkuVO::new);
    }

}
