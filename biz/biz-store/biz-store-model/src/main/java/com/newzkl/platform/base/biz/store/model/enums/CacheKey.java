package com.newzkl.platform.base.biz.store.model.enums;

/**
 * 门店域缓存 key 常量
 *
 * @author KC
 */
public interface CacheKey {

    /**
     * 用户关联门店ID列表，参数: accountId
     */
    String STORE_ACCOUNT_RELATION = "store:account:relation:{}";
}
