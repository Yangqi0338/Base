package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.AppVersionDomain;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app 版本控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/common")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionDomain appVersionDomain;

    /**
     * 查询 app 版本列表。
     *
     * @param query 查询条件
     * @return app 版本列表
     */
    @PostMapping("/queryAppVersionList")
    public PlatformResult<List<AppVersionVO>> queryAppVersionList(@RequestBody AppVersionQuery query) {
        return PlatformResult.success(appVersionDomain.queryAppVersionList(query));
    }

    /**
     * 保存 app 版本信息。
     *
     * @param appVersion app 版本视图对象
     * @return 成功结果
     */
    @PostMapping("/saveAppVersion")
    public PlatformResult<Void> saveAppVersion(@RequestBody AppVersionVO appVersion) {
        appVersionDomain.saveAppVersion(appVersion);
        return PlatformResult.success();
    }

    /**
     * 查询指定 app 最新版本。
     *
     * @param appName app 名称
     * @return app 版本视图对象
     */
    @PostMapping("/queryAppNewVersion/{appName}")
    public PlatformResult<AppVersionVO> queryAppNewVersion(@PathVariable String appName) {
        return PlatformResult.success(appVersionDomain.queryAppNewVersion(appName));
    }
}
