package com.newzkl.platform.base.biz.market.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.market.domain.adapt.api.GoodsCategoryApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.PlatformCategoryInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * {@link GoodsCategoryApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 商品域(goods)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class GoodsCategoryApiDefaultImpl implements GoodsCategoryApi {

    @Override
    public List<PlatformCategoryInfo> platformCategoryTree(Long rootId) {
        // TODO[cross-service]: 远程 goods 平台分类查询, 默认返回空集合
        return Collections.emptyList();
    }
}
