package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRecordItemRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleRecordItemDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecordItem;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordItemPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordItemVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 结算记录明细仓储实现类
 * @author fang
 */
@Repository
public class SettleRecordItemRepositoryImpl extends ServiceImpl<SettleRecordItemDAO, SettleRecordItemDO> implements SettleRecordItemRepository {

    @Override
    public SettleRecordItem save(SettleRecordItem settleRecordItem) {
        SettleRecordItemDO settleRecordItemDO = TransferUtils.transfer(settleRecordItem, SettleRecordItemDO::new);
        this.saveOrUpdate(settleRecordItemDO);
        return settleRecordItem;
    }

    @Override
    public SettleRecordItem findById(Long id) {
        SettleRecordItemDO settleRecordItemDO = this.getById(id);
        return TransferUtils.transfer(settleRecordItemDO, SettleRecordItem::new);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean deleteByQuery(SettleRecordItemPageReq query) {
        LambdaQueryWrapper<SettleRecordItemDO> wrapper = buildQueryWrapper(query);
        return this.remove(wrapper);
    }

    @Override
    public List<SettleRecordItemVO> listByQuery(SettleRecordItemPageReq query) {
        LambdaQueryWrapper<SettleRecordItemDO> wrapper = buildQueryWrapper(query);
        List<SettleRecordItemDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(list, SettleRecordItemVO::new);
    }

    @Override
    public Integer countByQuery(SettleRecordItemPageReq query) {
        return baseMapper.countByQuery(query);
    }

    @Override
    public Page<SettleRecordItemVO> pageByQuery(SettleRecordItemPageReq query) {
        // 创建分页对象
        Page<SettleRecordItemDO> doPage = new Page<>(query.getCurrent(), query.getSize());

        // 构建查询条件
        LambdaQueryWrapper<SettleRecordItemDO> wrapper = buildQueryWrapper(query);

        // 执行分页查询
        Page<SettleRecordItemDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 转换为VO分页结果
        return TransferUtils.transferPage(resultDoPage, SettleRecordItemVO::new);
    }

    @Override
    public boolean batchInsert(List<SettleRecordItem> settleRecordItems) {
        if (settleRecordItems == null || settleRecordItems.isEmpty()) {
            return false;
        }
        
        List<SettleRecordItemDO> settleRecordItemDOList = TransferUtils.transfers(settleRecordItems, SettleRecordItemDO::new);
        return saveBatch(settleRecordItemDOList);
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<SettleRecordItemDO> buildQueryWrapper(SettleRecordItemPageReq query) {
        LambdaQueryWrapper<SettleRecordItemDO> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getId() != null) {
            wrapper.eq(SettleRecordItemDO::getId, query.getId());
        }
        if (query.getIdList() != null && !query.getIdList().isEmpty()) {
            wrapper.in(SettleRecordItemDO::getId, query.getIdList());
        }
        if (query.getSettleRecordId() != null) {
            wrapper.eq(SettleRecordItemDO::getSettleRecordId, query.getSettleRecordId());
        }
        if (query.getSpuName() != null) {
            wrapper.like(SettleRecordItemDO::getSpuName, query.getSpuName());
        }
        
        return wrapper;
    }
}