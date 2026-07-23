package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleGoodsRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleGoodsDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.order.req.SettleGoodsPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleGoodsVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 结算商品仓储实现类
 * @author fang
 */
@Repository
public class SettleGoodsRepositoryImpl extends ServiceImpl<SettleGoodsDAO, SettleGoodsDO> implements SettleGoodsRepository {

    @Override
    public SettleGoods save(SettleGoods settleGoods) {
        SettleGoodsDO settleGoodsDO = TransferUtils.transfer(settleGoods, SettleGoodsDO::new);
        this.saveOrUpdate(settleGoodsDO);
        return settleGoods;
    }

    @Override
    public SettleGoods findById(Long id) {
        SettleGoodsDO settleGoodsDO = this.getById(id);
        return TransferUtils.transfer(settleGoodsDO, SettleGoods::new);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean deleteByQuery(SettleGoodsPageReq query) {
        LambdaQueryWrapper<SettleGoodsDO> wrapper = buildQueryWrapper(query);
        return this.remove(wrapper);
    }

    @Override
    public List<SettleGoodsVO> listByQuery(SettleGoodsPageReq query) {
        LambdaQueryWrapper<SettleGoodsDO> wrapper = buildQueryWrapper(query);
        List<SettleGoodsDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        return TransferUtils.transfers(list, SettleGoodsVO::new);
    }

    @Override
    public Integer countByQuery(SettleGoodsPageReq query) {
        return baseMapper.countByQuery(query);
    }

    @Override
    public Page<SettleGoodsVO> pageByQuery(SettleGoodsPageReq query) {
        // 创建分页对象
        Page<SettleGoodsDO> doPage = new Page<>(query.getCurrent(), query.getSize());

        // 构建查询条件
        LambdaQueryWrapper<SettleGoodsDO> wrapper = buildQueryWrapper(query);

        // 执行分页查询
        Page<SettleGoodsDO> resultDoPage = baseMapper.selectPage(doPage, wrapper);

        // 转换为VO分页结果
        return TransferUtils.transferPage(resultDoPage, SettleGoodsVO::new);
    }

    @Override
    public boolean executeSettle(Long supplierId, Long spuId, Integer settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime) {
        return baseMapper.settleGoodsEditForExecuteSettle(supplierId, spuId, settleMoney, settleSkuCount, nextSettlementTime) > 0;
    }

    @Override
    public boolean executeEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime) {
        return baseMapper.settleGoodsEditForExecuteEmptySettle(supplierId, spuId, nextSettlementTime) > 0;
    }

    @Override
    public boolean batchInsert(List<SettleGoods> settleGoodsList) {
        if (settleGoodsList == null || settleGoodsList.isEmpty()) {
            return false;
        }
        
        List<SettleGoodsDO> settleGoodsDOList = TransferUtils.transfers(settleGoodsList, SettleGoodsDO::new);
        return saveBatch(settleGoodsDOList);
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<SettleGoodsDO> buildQueryWrapper(SettleGoodsPageReq query) {
        LambdaQueryWrapper<SettleGoodsDO> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getId() != null) {
            wrapper.eq(SettleGoodsDO::getId, query.getId());
        }
        if (query.getIdList() != null && !query.getIdList().isEmpty()) {
            wrapper.in(SettleGoodsDO::getId, query.getIdList());
        }
        if (query.getLessSettleTime() != null) {
            wrapper.le(SettleGoodsDO::getNextSettleTime, query.getLessSettleTime());
        }
        
        return wrapper;
    }
}