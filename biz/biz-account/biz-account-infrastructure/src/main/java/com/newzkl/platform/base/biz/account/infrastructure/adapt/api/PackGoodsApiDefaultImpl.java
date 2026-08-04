package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsInfo;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsSaveReq;
import org.springframework.stereotype.Component;

/**
 * {@code PackGoodsApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 入会礼包归 biz-user(PackGoodsFacade), 端口待接线。
 * 未接线前写入返回 null、查询返回 null, 保证账户域可独立编排与测试;
 * 入口 starter 侧应以远程 consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class PackGoodsApiDefaultImpl implements PackGoodsApi {

    @Override
    public Long save(PackGoodsSaveReq req) {
        // TODO[cross-service]: 远程保存礼包商品, 默认 null
        return null;
    }

    @Override
    public PackGoodsInfo findByQuery(PackGoodsQuery query) {
        // TODO[cross-service]: 远程查询礼包商品, 默认 null
        return null;
    }
}
