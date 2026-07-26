package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AroleRepository;
import com.newzkl.platform.base.biz.sys.domain.service.AroleDomain;
import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 后台角色领域服务实现。
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class AroleDomainImpl implements AroleDomain {

    private final AroleRepository aroleRepository;

    @Override
    public Long aroleSave(AroleReq req) {
        return aroleRepository.aroleSave(req);
    }

    @Override
    public void aroleDelete(List<Long> idList) {
        aroleRepository.aroleDelete(idList);
    }

    @Override
    public AroleRes aroleVO(Long id) {
        return aroleRepository.aroleVO(id);
    }

    @Override
    public List<AroleRes> aroleList(AroleQuery query) {
        return aroleRepository.aroleList(query);
    }
}
