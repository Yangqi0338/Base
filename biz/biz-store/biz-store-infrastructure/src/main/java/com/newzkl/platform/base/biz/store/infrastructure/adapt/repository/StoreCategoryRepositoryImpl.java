package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseQueryWrapper;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreCategoryRepository;

import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreCategoryDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreCategoryDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 门店分类 (StoreCategory)存储实现
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
@Repository
@RequiredArgsConstructor
public class StoreCategoryRepositoryImpl implements StoreCategoryRepository {
    private final StoreCategoryDAO dao;

    /**
     * 根据query封装LambdaEw
     *
     * @param query 查询条件
     * @return ew
     */
    public BaseQueryWrapper<StoreCategoryDO> buildLambdaQw(StoreCategoryQuery query) {
        BaseLambdaQueryWrapper<StoreCategoryDO> ew = new BaseLambdaQueryWrapper<>();
        ew.notEmptyIn(StoreCategoryDO::getId, query.getIdList());
        ew.notEmptyLike(StoreCategoryDO::getName, query.getName());
        ew.between(StoreCategoryDO::getCreateTime, query.getCreateStartTime(), query.getCreateEndTime());
        return ew.orderBy(query);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public StoreCategory detail(Long id) {
        StoreCategoryDO storeCategoryDO = dao.selectById(id);
        return TransferUtils.transfer(storeCategoryDO, StoreCategory::new);
    }

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<StoreCategoryRes> queryList(StoreCategoryQuery query) {
        List<StoreCategoryDO> doList = dao.selectList(buildLambdaQw(query));
        return TransferUtils.transfers(doList, StoreCategoryRes::new);
    }

    /**
     * 查询分页
     *
     * @param query 查询条件
     * @return 分页
     */
    @Override
    public IPage<StoreCategoryRes> queryPage(StoreCategoryQuery query) {
        IPage<StoreCategoryDO> doList = dao.selectPage(com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport.page(query), buildLambdaQw(query));
        return doList.convert(source -> TransferUtils.transfer(source, StoreCategoryRes::new));
    }

    /**
     * 新增数据
     *
     * @param storeCategory 新增实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(StoreCategory storeCategory) {
        StoreCategoryDO storeCategoryDO = TransferUtils.transfer(storeCategory, StoreCategoryDO::new);
        dao.insert(storeCategoryDO);
    }

    /**
     * 修改数据
     *
     * @param storeCategory 编辑实体
     * @param query         编辑查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(StoreCategory storeCategory, StoreCategoryQuery query) {
        QueryWrapper<StoreCategoryDO> ew = buildLambdaQw(query);
        if (dao.selectCount(ew) == 0) {
            throw new ScmException(BaseErrorCode.INVALID_UPDATE);
        }

        StoreCategoryDO storeCategoryDO = TransferUtils.transfer(storeCategory, StoreCategoryDO::new);
        dao.update(storeCategoryDO, ew);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        dao.deleteById(id);
    }
}
