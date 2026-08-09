package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.DictApi;
import org.springframework.stereotype.Component;

/**
 * {@code DictApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 字典能力尚未在中台落地, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("accountDictApi")
public class DictApiImpl implements DictApi {

    @Override
    public String get(Long code) {
        // TODO[cross-service]: 远程字典查询, 默认 null
        return null;
    }
}
