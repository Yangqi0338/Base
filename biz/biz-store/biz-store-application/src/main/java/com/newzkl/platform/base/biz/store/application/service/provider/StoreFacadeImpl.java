package com.newzkl.platform.base.biz.store.application.service.provider;

import cn.hutool.core.lang.Opt;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.facade.StoreFacade;
import com.newzkl.platform.base.biz.store.facade.model.StoreRegisterReq;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.req.StoreSaveReq;

import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 运营商
 * @date 2023/12/1213:47
 */
@Component
@Setter(onMethod_ = @Autowired)
@Slf4j
public class StoreFacadeImpl implements StoreFacade {

    @Autowired
    private StoreDomain storeDomain;

    @Override
    public boolean openStore(StoreRegisterReq req) {
        Long accountId = req.getChannelId();
        Store store = storeDomain.store(accountId);

        StoreSaveReq storeSaveReq = new StoreSaveReq();
        storeSaveReq.setId(accountId);
        storeSaveReq.setName(Opt.ofNullable(req.getStoreName()).orElse("主营门店"));
        storeSaveReq.setAddress(req.getAddress());
        storeSaveReq.setType(req.getStoreType());

        if (store == null) {
            SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, accountId + "");
            return storeDomain.storeSave(storeSaveReq) > 0;
        } else {
            return storeDomain.storeEdit(accountId, storeSaveReq) > 0;
        }
    }
}
