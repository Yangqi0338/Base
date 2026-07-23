package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AppVersionRepository;
import com.newzkl.platform.base.biz.sys.domain.service.AppVersionDomain;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * app 版本领域服务实现。
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class AppVersionDomainImpl implements AppVersionDomain {

    private final AppVersionRepository appVersionRepository;

    @Override
    public List<AppVersionVO> queryAppVersionList(AppVersionQuery query) {
        return appVersionRepository.queryAppVersionList(query);
    }

    @Override
    public void saveAppVersion(AppVersionVO appVersion) {
        appVersionRepository.saveAppVersion(appVersion);
    }

    @Override
    public AppVersionVO queryAppNewVersion(String appName) {
        return appVersionRepository.queryAppNewVersion(appName);
    }
}
