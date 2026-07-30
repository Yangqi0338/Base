package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.DictApi;
import org.springframework.stereotype.Component;

/**
 * {@code DictApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 字典域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("financeDictApiDefaultImpl")
public class DictApiDefaultImpl implements DictApi {

    @Override
    public String get(Long code) {
        // TODO[cross-service]: 远程 user 字典取值, 默认返回 null
        return null;
    }

    @Override
    public void set(Long code, String value) {
        // TODO[cross-service]: 远程 user 字典写值, 默认空操作
    }
}
