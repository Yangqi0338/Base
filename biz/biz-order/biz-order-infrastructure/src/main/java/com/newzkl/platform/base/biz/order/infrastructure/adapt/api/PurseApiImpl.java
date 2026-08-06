package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.ddd.facade.HuiFuPurseInfo;
import com.newzkl.platform.base.biz.order.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PurseApiImpl implements PurseApi {

    @Override
    public HuiFuPurseInfo queryHuiFuPurse(Long accountId) {
        // FIXME
        return null;
    }
}
