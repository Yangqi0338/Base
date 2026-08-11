package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AppVersionRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.AppVersionDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AppVersionDO;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * app 版本仓储实现
 *
 * <p>⚠️ 2026-07-30 更正: 原注「分页在本层内部执行 (Page 不外泄), 对领域层降级为 List」
 * 与 {@code rules/Architecture.md} 语义迁移条「{@code PageInfo}→{@code IPage/Page} 直返」矛盾。
 * 降级成 List 的后果是 {@code total} 丢失 —— 本层已执行 {@code selectPage}, count 查询照样跑了,
 * 只是结果被扔掉, 前端分页器拿不到总数(platform-admin 读 {@code res.data.list} 得 undefined)。
 * 现按 Architecture.md 口径返 {@code Page}</p>
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AppVersionRepositoryImpl implements AppVersionRepository {

    private final AppVersionDAO appVersionDAO;

    @Override
    public Page<AppVersionVO> queryAppVersionList(AppVersionQuery query) {
        Page<AppVersionDO> page = appVersionDAO.selectPage(RepositorySupport.page(query), appVersionDAO.getLw(query));
        return TransferUtils.transferPage(page, AppVersionVO::new);
    }

    @Override
    public void saveAppVersion(AppVersionVO appVersion) {
        appVersionDAO.insertOrUpdate(TransferUtils.transfer(appVersion, AppVersionDO::new));
    }

    @Override
    public AppVersionVO queryAppNewVersion(String appName) {
        AppVersionQuery query = new AppVersionQuery();
        query.setAppName(appName);
        AppVersionDO appVersion = appVersionDAO.selectOne(appVersionDAO.getLw(query).last("limit 1"));
        return TransferUtils.transfer(appVersion, AppVersionVO::new);
    }
}
