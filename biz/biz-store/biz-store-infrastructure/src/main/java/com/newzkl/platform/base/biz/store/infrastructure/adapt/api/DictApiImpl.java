package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.DictApi;
import org.springframework.stereotype.Component;

/**
 * {@code DictApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 字典域(admin/user)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component("storeDictApi")
public class DictApiImpl implements DictApi {

    @Override
    public String get(Long code) {
        // TODO[cross-service]: 远程字典查询, 默认返回 null
        return null;
    }
}
