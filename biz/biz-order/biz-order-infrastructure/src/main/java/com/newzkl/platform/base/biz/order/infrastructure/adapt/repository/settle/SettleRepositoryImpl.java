package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.settle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.settle.SettleGoodsDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.settle.SettleOrderWaitDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.settle.SettleRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.settle.SettleRecordItemDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleOrderWaitDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordAgg;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordItemDTO;
import com.newzkl.platform.base.biz.order.model.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.req.query.SettleGoodsQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleOrderWaitQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class SettleRepositoryImpl implements SettleRepository {

    private final SettleGoodsDAO settleGoodsDAO;
    private final SettleOrderWaitDAO settleOrderWaitDAO;
    private final SettleRecordDAO settleRecordDAO;
    private final SettleRecordItemDAO settleRecordItemDAO;

    private final SupplierApi supplierFacade;

    @Override
    public SettleRecordVO settleRecordVO(Long settleRecordId) {
        SettleRecordDO settleRecordDO = settleRecordDAO.selectById(settleRecordId);
        return TransferUtils.transfer(settleRecordDO, SettleRecordVO.class);
    }
    @Override
    public Page<SettleRecordVO> settleRecordVOList(SettleRecordQuery settleRecordQuery) {
        Page<SettleRecordDO> settleRecordDOPage = settleRecordDAO.selectPage(RepositorySupport.page(settleRecordQuery), new LambdaQueryWrapper<>());
        return TransferUtils.transferPage(settleRecordDOPage,SettleRecordVO.class);
    }

    @Override
    public List<SettleOrderWaitVO> queryWaitSettleOrderTimeNode(Long settleTimeNode) {
        return settleOrderWaitDAO.queryWaitSettleOrderTimeNode(settleTimeNode);
    }

    @Override
    public List<SettleOrderWaitVO> queryWaitSettleOrder(List<Long> spuIds) {
        List<Long> skuOrderId = null;
        try{
            skuOrderId = SecurityContextHolder.get("waitSettlementOrderId", List.class);
        }catch (Exception e){
            log.error("queryWaitSettleOrder", e);
        }
        if(skuOrderId == null){
            return settleOrderWaitDAO.queryWaitSettleOrder(spuIds);
        }else {
            return settleOrderWaitDAO.queryWaitSettleOrderIn(spuIds, skuOrderId);
        }
    }

    @Override
    public Long settleGoodsSave(SettleGoods settleGoods) {
        SettleGoodsDO settleGoodsDO = TransferUtils.transfer(settleGoods, SettleGoodsDO.class);
        if(settleGoodsDO.getId() == null || settleGoodsDO.getId() == 0){
            settleGoodsDO.setId(SnowflakeIdAble.getSnowflakeId());
            settleGoodsDAO.insert(settleGoodsDO);
        }else {
            settleGoodsDAO.updateById(settleGoodsDO);
        }
        return settleGoodsDO.getId();
    }
    @Override
    public Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsQuery settleGoodsQuery) {

        List<SettleGoodsVO> settleGoodsVO = TransferUtils.transfers(settleGoodsDAO.selectList(settleGoodsDAO.getLw(settleGoodsQuery)), SettleGoodsVO.class);
        Page<SettleGoodsVO> page = new Page<>();
        page.setRecords(settleGoodsVO);
        page.setTotal(settleGoodsVO.size());
        return page;
    }

    @Override
    public boolean settleGoodsEditForExecuteSettle(Long supplierId, Long spuId, Money settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime) {
        SettleGoodsQuery settleGoodsQuery = new SettleGoodsQuery();
        settleGoodsQuery.setSupplierId(supplierId);
        settleGoodsQuery.setSpuId(spuId);
        return settleGoodsDAO.update(settleGoodsDAO.getLw(settleGoodsQuery).toUpdate()
                .setIncrBy(SettleGoodsDO::getSettleMoney, settleMoney)
                .set(SettleGoodsDO::getNextSettleTime, nextSettlementTime)
                .setIncrBy(SettleGoodsDO::getSettleNum, 1)
                .setIncrBy(SettleGoodsDO::getSettleGoodsNum, settleSkuCount)
            ) > 0;
    }
    @Override
    public boolean settleGoodsEditForExecuteEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime) {
        SettleGoodsQuery settleGoodsQuery = new SettleGoodsQuery();
        settleGoodsQuery.setSupplierId(supplierId);
        settleGoodsQuery.setSpuId(spuId);
        return settleGoodsDAO.update(settleGoodsDAO.getLw(settleGoodsQuery).toUpdate()
                .set(SettleGoodsDO::getNextSettleTime, nextSettlementTime)
        ) > 0;
    }
    @Override
    public int settleOrderWaitEditForExecuteSettle(List<Long> settleOrderWaitIdList, Integer settleState, LocalDateTime settleTime, Long settleRecordId) {
        SettleOrderWaitQuery query = new SettleOrderWaitQuery();
        query.setIdList(settleOrderWaitIdList);
        return settleOrderWaitDAO.update(settleOrderWaitDAO.getLw(query).toUpdate()
            .set(SettleOrderWaitDO::getSettleState, settleState)
            .set(SettleOrderWaitDO::getSettleTime, settleTime)
            .set(SettleOrderWaitDO::getSettleRecordId, settleRecordId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleRecordAggCreate(SettleRecordAgg settleRecord) {
        SettleRecordDO settleRecordDO = TransferUtils.transfer(settleRecord.getSettleRecord(), SettleRecordDO.class);
        settleRecordDAO.insert(settleRecordDO);

        List<SettleRecordItemDO> settleRecordItemDOList = TransferUtils.transfers(settleRecord.getSettleRecordItemList(), SettleRecordItemDO.class);
        settleRecordItemDOList.forEach(settleRecordItemDO -> {
            settleRecordItemDO.setSettleRecordId(settleRecordDO.getId());
        });
        if(ObjectUtil.isNotEmpty(settleRecordItemDOList)){
            settleRecordItemDAO.insert(settleRecordItemDOList);
        }
    }

    @Override
    public void settleOrderWaitSaveBatch(List<SettleOrderWait> settleOrderWaitList) {
        List<SettleOrderWaitDO> settleOrderWaitDOList = TransferUtils.transfers(settleOrderWaitList, SettleOrderWaitDO.class);
        settleOrderWaitDAO.insert(settleOrderWaitDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterWaitSettleFreightTimeNode(Long spuOrderId,Long settleNodeTime) {
        SettleOrderWaitQuery query = new SettleOrderWaitQuery();
        query.setSpuOrderId(spuOrderId);
        settleOrderWaitDAO.update(settleOrderWaitDAO.getLw(query).toUpdate()
                .set(SettleOrderWaitDO::getSettleTimeNode, settleNodeTime)
        );
    }

    @Override
    public SettleRecordDetailVO settleRecordDetailVO(Long id) {
        SettleRecordDetailVO settleRecordDetailVO = new SettleRecordDetailVO();
        SettleRecordDO settleRecordDO = settleRecordDAO.selectById(id);

        SettleRecordItemQuery settleRecordItemQuery = new SettleRecordItemQuery();
        settleRecordItemQuery.setSettleRecordId(id);
        List<SettleRecordItemDO> settleRecordItemDOS = settleRecordItemDAO.selectList(settleRecordItemDAO.getLw(settleRecordItemQuery));
        List<SettleRecordItemVO> settleRecordItemVOS = TransferUtils.transfers(settleRecordItemDOS, SettleRecordItemVO.class);
        settleRecordDetailVO.setSettleRecordVO(TransferUtils.transfer(settleRecordDO, SettleRecordVO.class));
        settleRecordDetailVO.setSettleRecordItemVOList(settleRecordItemVOS);
        return settleRecordDetailVO;
    }

    @Override
    public Page<SettleRecordItemDTO> settleRecordItemPage(SettleRecordItemQuery query) {
        Page<SettleRecordItemDO> doPage = settleRecordItemDAO.selectPage(RepositorySupport.page(query), settleRecordItemDAO.getLw(query));
        return TransferUtils.transferPage(doPage, SettleRecordItemDTO.class);
    }

    @Override
    public Integer closeSettleOrder(Long skuOrderId, Long refundId) {
        SettleOrderWaitQuery query = new SettleOrderWaitQuery();
        query.setSkuOrderId(skuOrderId);
        query.setType(0);
        SettleOrderWaitDO settleOrderWaitDO = CollUtil.getFirst(settleOrderWaitDAO.selectList(settleOrderWaitDAO.getLw(query)));
        if(settleOrderWaitDO == null){
            return null;
        }else {
            SettleOrderWaitQuery updateQuery = new SettleOrderWaitQuery();
            updateQuery.setId(settleOrderWaitDO.getId());
            updateQuery.setSettleState(0);
            return settleOrderWaitDAO.update(settleOrderWaitDAO.getLw(updateQuery).toUpdate()
                    .set(SettleOrderWaitDO::getRefundState, 1)
                    .set(SettleOrderWaitDO::getRefundId, refundId)
            );
        }
    }

    @Override
    public void editSettleRecord(SettleRecordEditReq settleRecordEditReq) {
        settleRecordDAO.editSettleRecord(settleRecordEditReq);
    }

    @Override
    public List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList) {
        return settleRecordDAO.settleTypeList(settleTypeList);
    }

    @Override
    public Integer querySupplierSettleConfig(Long supplierId) {
        SettlementConfigOutVO config = CollUtil.getFirst(supplierFacade.settlementConfigBatch(CollUtil.newArrayList(supplierId)));
        return config == null ? null : config.getOrderTypeDay();
    }
}
