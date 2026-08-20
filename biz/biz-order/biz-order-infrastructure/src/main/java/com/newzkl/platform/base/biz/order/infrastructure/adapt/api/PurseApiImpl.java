package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.HuiFuPurseInfo;
import com.newzkl.platform.base.biz.order.domain.adapt.api.PurseApi;
import org.springframework.stereotype.Service;

@Service("orderPurseApi")
public class PurseApiImpl implements PurseApi {

    @Override
    public HuiFuPurseInfo queryHuiFuPurse(Long accountId) {
        // FIXME
        return null;
    }
}
