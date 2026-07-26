package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AroleRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.AroleDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AroleDO;
import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色仓储实现。
 *
 * <p>分页在本层内部执行 (Page 不外泄), 对领域层降级为 List。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class AroleRepositoryImpl implements AroleRepository {

    private final AroleDAO aroleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long aroleSave(AroleReq req) {
        AroleDO aroleDO = TransferUtils.transfer(req, AroleDO::new);
        aroleDAO.insertOrUpdate(aroleDO);
        return aroleDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aroleDelete(List<Long> idList) {
        aroleDAO.deleteByIds(idList);
    }

    @Override
    public AroleRes aroleVO(Long id) {
        return TransferUtils.transfer(aroleDAO.selectById(id), AroleRes::new);
    }

    @Override
    public List<AroleRes> aroleList(AroleQuery query) {
        Page<AroleDO> page = aroleDAO.selectPage(RepositorySupport.page(query), aroleDAO.getLw(query));
        return TransferUtils.transfers(page.getRecords(), AroleRes::new);
    }
}
