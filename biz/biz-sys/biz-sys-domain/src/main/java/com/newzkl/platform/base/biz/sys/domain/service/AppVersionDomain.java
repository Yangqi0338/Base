package com.newzkl.platform.base.biz.sys.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;

import java.util.List;

/**
 * app 版本领域服务
 *
 * @author fang
 */
public interface AppVersionDomain {

    /**
     * 分页查询 app 版本
     *
     * <p>出参分页对象, 前端读 {@code records}/{@code total}/{@code current}/{@code size};
     * 只要数据行的内部调用方自行 {@code getRecords()}</p>
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
