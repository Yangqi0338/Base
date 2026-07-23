package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.SupplierApi;
import org.springframework.stereotype.Component;

/**
 * {@link SupplierApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 供应商域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class SupplierApiDefaultImpl implements SupplierApi {

    @Override
    public Integer limitAmount(Long accountId) {
        // TODO[cross-service]: 远程 user 供应商提现限额查询, 默认返回 0 (无限制)
        return 0;
    }
}
