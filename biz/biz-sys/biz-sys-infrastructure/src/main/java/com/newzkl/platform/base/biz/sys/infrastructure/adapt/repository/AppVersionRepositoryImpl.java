package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AppVersionRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.AppVersionDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AppVersionDO;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * app 版本仓储实现。
 *
 * <p>分页在本层内部执行 (Page 不外泄), 对领域层降级为 List。</p>
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AppVersionRepositoryImpl implements AppVersionRepository {

    private final AppVersionDAO appVersionDAO;

    @Override
    public List<AppVersionVO> queryAppVersionList(AppVersionQuery query) {
        Page<AppVersionDO> page = appVersionDAO.selectPage(RepositorySupport.page(query), appVersionDAO.getLw(query));
        return TransferUtils.transfers(page.getRecords(), AppVersionVO::new);
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
