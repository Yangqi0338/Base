package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;

import java.util.List;

/**
 * app 版本仓储端口
 *
 * @author fang
 */
public interface AppVersionRepository {

    /**
     * 分页查询 app 版本
     *
     * <p>返回分页对象而非裸 {@code List}: 实现内已执行 {@code selectPage} 拿到 {@code total},
     * 丢壳等于 count 查询白跑, 前端分页器无总数可用。
     * 见 {@code rules/Architecture.md}「{@code PageInfo}→{@code IPage/Page} 直返」</p>
     *
     * @param query 查询条件
     * @return app 版本分页
     */
    Page<AppVersionVO> queryAppVersionList(AppVersionQuery query);

    /**
     * 保存 app 版本信息
     *
     * @param appVersion app 版本视图对象
     */
    void saveAppVersion(AppVersionVO appVersion);

    /**
     * 查询指定 app 最新版本
     *
     * @param appName app 名称
     * @return app 版本视图对象
     */
    AppVersionVO queryAppNewVersion(String appName);
}
