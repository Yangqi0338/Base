package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.VirtualAssetsRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.VirtualAssetsDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.VirtualAssetsDO;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 虚拟资产仓储实现。
 *
 * <p>迁移自 new-scm {@code VirtualAssetsRepositoryImpl}。旧实现走手写 XML SQL + PageHelper,
 * 新实现改为 MyBatis-Plus 原生 {@code selectPage}, 因此无需 {@code VirtualAssetsDAO.xml}。
 * {@code Page} 不外泄, 对 domain 降级为 {@code List}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class VirtualAssetsRepositoryImpl extends RepositorySupport implements VirtualAssetsRepository {

    /**
     * 虚拟资产 DAO。
     */
    private final VirtualAssetsDAO virtualAssetsDAO;

    /**
     * 分页查询虚拟资产。
     *
     * @param query 查询条件
     * @return 虚拟资产列表, 永不为 null
     */
    @Override
    public List<VirtualAssetsRes> queryPage(VirtualAssetsQuery query) {
        Page<VirtualAssetsDO> page = virtualAssetsDAO.selectPage(RepositorySupport.page(query),
                virtualAssetsDAO.getLw(query));
        return TransferUtils.transfers(page.getRecords(), VirtualAssetsRes::new);
    }
}
