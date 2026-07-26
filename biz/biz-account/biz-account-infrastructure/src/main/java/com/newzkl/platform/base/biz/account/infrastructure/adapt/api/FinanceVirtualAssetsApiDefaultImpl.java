package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceVirtualAssetsApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.VirtualAssetsAlterReq;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link FinanceVirtualAssetsApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 资金域(finance)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class FinanceVirtualAssetsApiDefaultImpl implements FinanceVirtualAssetsApi {

    @Override
    public void alterVirtualAssets(List<VirtualAssetsAlterReq> reqList) {
        // TODO[cross-service]: 远程 finance 期权变更, 默认空实现
    }
}
