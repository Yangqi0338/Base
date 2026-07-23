package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRecordRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 结算记录仓储实现类
 * @author fang
 */
@Repository
public class SettleRecordRepositoryImpl extends ServiceImpl<SettleRecordDAO, SettleRecordDO> implements SettleRecordRepository {

    @Override
    public SettleRecord save(SettleRecord settleRecord) {
        SettleRecordDO settleRecordDO = TransferUtils.transfer(settleRecord, SettleRecordDO::new);
        this.saveOrUpdate(settleRecordDO);
        return settleRecord;
    }

    @Override
    public SettleRecord findById(Long id) {
        SettleRecordDO settleRecordDO = this.getById(id);
        return TransferUtils.transfer(settleRecordDO, SettleRecord::new);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean deleteByQuery(SettleRecordPageReq query) {
        LambdaQueryWrapper<SettleRecordDO> wrapper = buildQueryWrapper(query);
        return this.remove(wrapper);
    }

    @Override
    public List<SettleRecordVO> listByQuery(SettleRecordPageReq query) {
        LambdaQueryWrapper<SettleRecordDO> wrapper = buildQueryWrapper(query);
        List<SettleRecordDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(list, SettleRecordVO::new);
    }

    @Override
    public Integer countByQuery(SettleRecordPageReq query) {
        return baseMapper.countByQuery(query);
    }

    @Override
    public Page<SettleRecordVO> pageByQuery(SettleRecordPageReq query) {
        // 创建分页对象
        Page<SettleRecordDO> doPage = new Page<>(query.getCurrent(), query.getSize());

        // 构建查询条件
        LambdaQueryWrapper<SettleRecordDO> wrapper = buildQueryWrapper(query);

        // 执行分页查询
        Page<SettleRecordDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 转换为VO分页结果
        return TransferUtils.transferPage(resultDoPage, SettleRecordVO::new);
    }

    @Override
    public Integer totalSettleAmount() {
        return baseMapper.totalSettleAmount();
    }

    @Override
    public boolean updateById(SettleRecordEditReq settleRecordEditReq) {
        SettleRecordDO transfer = TransferUtils.transfer(settleRecordEditReq, SettleRecordDO::new);
        return this.updateById(transfer);
    }

    @Override
    public List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList) {
        return baseMapper.settleTypeList(settleTypeList);
    }

    @Override
    public boolean batchInsert(List<SettleRecord> settleRecords) {
        if (settleRecords == null || settleRecords.isEmpty()) {
            return false;
        }
        
        List<SettleRecordDO> settleRecordDOList = TransferUtils.transfers(settleRecords, SettleRecordDO::new);
        return saveBatch(settleRecordDOList);
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<SettleRecordDO> buildQueryWrapper(SettleRecordPageReq query) {
        LambdaQueryWrapper<SettleRecordDO> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getId() != null) {
            wrapper.eq(SettleRecordDO::getId, query.getId());
        }
        if (query.getIdList() != null && !query.getIdList().isEmpty()) {
            wrapper.in(SettleRecordDO::getId, query.getIdList());
        }
        if (query.getSupplierId() != null) {
            wrapper.eq(SettleRecordDO::getSupplierId, query.getSupplierId());
        }
        
        wrapper.orderByDesc(SettleRecordDO::getCreateTime);
        
        return wrapper;
    }
}