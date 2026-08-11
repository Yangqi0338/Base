package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.LevelRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.AccountLevelDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.LevelDO;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.vo.LevelVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 等级仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.LevelRepositoryImpl}。
 * 旧实现手工判空后走 insert / updateByPrimaryKeySelective, 本仓统一用
 * MyBatis-Plus {@code insertOrUpdate} 表达同一语义。</p>
 *
 * @author KC
 */
@Repository("accountLevelRepositoryImpl")
@RequiredArgsConstructor
public class LevelRepositoryImpl implements LevelRepository {

    private final AccountLevelDAO levelDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(LevelVO level) {
        LevelDO levelDO = TransferUtils.transfer(level, LevelDO::new);
        levelDAO.insertOrUpdate(levelDO);
        return levelDO.getId();
    }

    @Override
    public LevelVO detail(Long id) {
        return TransferUtils.transfer(levelDAO.selectById(id), LevelVO::new);
    }

    @Override
    public List<LevelVO> list(LevelQuery query) {
        List<LevelDO> levelList = levelDAO.selectList(levelDAO.getLw(query));
        List<LevelVO> result = TransferUtils.transfers(levelList, LevelVO::new);
        return result == null ? new ArrayList<>() : result;
    }

    @Override
    public Page<LevelVO> pageList(LevelQuery query) {
        Page<LevelDO> pageList = levelDAO.selectPage(RepositorySupport.page(query), levelDAO.getLw(query));
        return TransferUtils.transferPage(pageList, LevelVO::new);
    }
}
