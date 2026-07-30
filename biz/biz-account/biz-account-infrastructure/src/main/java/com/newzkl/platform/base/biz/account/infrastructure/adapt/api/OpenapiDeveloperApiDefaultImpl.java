package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.DeveloperInitReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.OpenapiDeveloperApi;
import org.springframework.stereotype.Component;

/**
 * {@code OpenapiDeveloperApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 开放平台(openapi)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class OpenapiDeveloperApiDefaultImpl implements OpenapiDeveloperApi {

    @Override
    public void initDeveloper(DeveloperInitReq req) {
        // TODO[cross-service]: 远程 openapi 初始化开发者应用, 默认空实现
    }
}
