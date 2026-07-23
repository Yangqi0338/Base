package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;

import java.util.List;

/**
 * app 版本仓储端口。
 *
 * @author fang
 */
public interface AppVersionRepository {

    /**
     * 查询 app 版本列表 (分页在实现内部执行)。
     *
     * @param query 查询条件
     * @return app 版本列表
     */
    List<AppVersionVO> queryAppVersionList(AppVersionQuery query);

    /**
     * 保存 app 版本信息。
     *
     * @param appVersion app 版本视图对象
     */
    void saveAppVersion(AppVersionVO appVersion);

    /**
     * 查询指定 app 最新版本。
     *
     * @param appName app 名称
     * @return app 版本视图对象
     */
    AppVersionVO queryAppNewVersion(String appName);
}
