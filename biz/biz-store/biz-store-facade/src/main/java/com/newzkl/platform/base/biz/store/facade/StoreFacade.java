package com.newzkl.platform.base.biz.store.facade;


import com.newzkl.platform.base.biz.store.facade.model.StoreRegisterReq;

/**
 * @author muc_fang
 * @Description: 微信公众号
 * @date 2024/4/810:40
 */
public interface StoreFacade {

    boolean openStore(StoreRegisterReq req);

}
