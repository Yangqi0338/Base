package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.VirtualAssetsRecordRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.VirtualAssetsRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.VirtualAssetsRecordDO;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 虚拟资产变动记录仓储实现。
 *
 * <p>迁移自 new-scm {@code VirtualAssetsRecordRepositoryImpl}。旧实现走手写 XML SQL + PageHelper,
 * 新实现改为 MyBatis-Plus 原生 {@code selectPage}, 因此无需 {@code VirtualAssetsRecordDAO.xml}。
 * 记录按创建时间倒序, 与旧 XML 的 {@code order by create_time desc} 对齐。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class VirtualAssetsRecordRepositoryImpl extends RepositorySupport implements VirtualAssetsRecordRepository {

    /**
     * 虚拟资产变动记录 DAO。
     */
    private final VirtualAssetsRecordDAO virtualAssetsRecordDAO;

    /**
     * 分页查询虚拟资产变动记录。
     *
     * @param query 查询条件
     * @return 变动记录列表, 永不为 null
     */
    @Override
    public List<VirtualAssetsRecordRes> queryPage(VirtualAssetsRecordQuery query) {
        Page<VirtualAssetsRecordDO> page = virtualAssetsRecordDAO.selectPage(RepositorySupport.page(query),
                virtualAssetsRecordDAO.getLw(query).orderByDesc(VirtualAssetsRecordDO::getCreateTime));
        return TransferUtils.transfers(page.getRecords(), VirtualAssetsRecordRes::new);
    }
}
