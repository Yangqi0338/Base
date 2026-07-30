package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.DictDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.DictDO;
import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典仓储实现
 *
 * <p>分页在本层内部执行 (Page 不外泄), 对领域层降级为 List。</p>
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class DictRepositoryImpl implements DictRepository {

    private final DictDAO dictDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long dictSave(DictRes dict) {
        DictDO dictDO = TransferUtils.transfer(dict, DictDO::new);
        dictDAO.insertOrUpdate(dictDO);
        return dictDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dictDelete(List<Long> idList) {
        dictDAO.deleteByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dictUpdate(DictRes dict) {
        dictDAO.updateById(TransferUtils.transfer(dict, DictDO::new));
    }

    @Override
    public DictRes dictVO(Long id) {
        return TransferUtils.transfer(dictDAO.selectById(id), DictRes::new);
    }

    @Override
    public List<DictRes> dictList(DictQuery dictQuery) {
        Page<DictDO> page = dictDAO.selectPage(RepositorySupport.page(dictQuery), dictDAO.getLw(dictQuery));
        return TransferUtils.transfers(page.getRecords(), DictRes::new);
    }

    @Override
    public DictRes dictVOLock(Long id) {
        DictDO dictDO = dictDAO.selectOne(new BaseLambdaQueryWrapper<DictDO>()
                .eq(DictDO::getId, id)
                .last("for update"));
        return TransferUtils.transfer(dictDO, DictRes::new);
    }
}
