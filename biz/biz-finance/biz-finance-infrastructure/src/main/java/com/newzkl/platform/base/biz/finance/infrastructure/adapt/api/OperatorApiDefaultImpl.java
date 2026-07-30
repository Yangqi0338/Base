package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.OperatorApi;
import org.springframework.stereotype.Component;

/**
 * {@code OperatorApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 运营商域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class OperatorApiDefaultImpl implements OperatorApi {

    @Override
    public void leverSave(Long accountId, Integer radio) {
        // TODO[cross-service]: 远程 user 运营商杠杆保存, 默认空操作
    }

    @Override
    public Integer getLever(Long operatorId) {
        // TODO[cross-service]: 远程 user 运营商杠杆查询, 默认返回 0
        return 0;
    }
}
