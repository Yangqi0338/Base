package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleGoodsRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleOrderWaitRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRecordItemRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SettleRecordRepository;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecordItem;
import com.newzkl.platform.base.biz.order.model.order.req.*;
import com.newzkl.platform.base.biz.order.model.order.vo.*;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SettleDomainImpl implements SettleDomain {

    private final SettleGoodsRepository settleGoodsDAO;
    private final SettleRecordRepository settleRecordDAO;
    private final SettleOrderWaitRepository settleOrderWaitDAO;
    private final SettleRecordItemRepository settleRecordItemDAO;

    @Override
    public Long settleRecordSave(SettleRecord settleRecord) {
        SettleRecord save = settleRecordDAO.save(settleRecord);
        return save.getId();
    }
    @Override
    public SettleRecord settleRecord(Long settleRecordId) {
        return settleRecordDAO.findById(settleRecordId);
    }
    @Override
    public SettleRecordVO settleRecordVO(Long settleRecordId) {
        SettleRecord byId = settleRecordDAO.findById(settleRecordId);
        return TransferUtils.transfer(byId, SettleRecordVO::new);
    }
    @Override
    public Page<SettleRecordVO> settleRecordVOList(SettleRecordPageReq settleRecordQuery) {
        return settleRecordDAO.pageByQuery(settleRecordQuery);

    }
    @Override
    public Long settleOrderWaitSave(SettleOrderWait settleOrderWait) {
        SettleRecord transfer = TransferUtils.transfer(settleOrderWait, SettleRecord::new);
        settleRecordDAO.save(transfer);
        return transfer.getId();
    }
    @Override
    public SettleOrderWait settleOrderWait(Long settleOrderWaitId) {
        return settleOrderWaitDAO.findById(settleOrderWaitId);
    }
    @Override
    public SettleOrderWaitVO settleOrderWaitVO(Long settleOrderWaitId) {
        SettleOrderWait byId = settleOrderWaitDAO.findById(settleOrderWaitId);

        return TransferUtils.transfer(byId, SettleOrderWaitVO::new);
    }
    @Override
    public Page<SettleOrderWaitVO> settleOrderWaitVOList(SettleOrderWaitPageReq settleOrderWaitQuery) {
        return settleOrderWaitDAO.pageByQuery(settleOrderWaitQuery);
    }

    @Override
    public List<SettleOrderWaitVO> queryWaitSettleOrderTimeNode(Long settleTimeNode) {
        return settleOrderWaitDAO.queryWaitSettleOrderTimeNode(settleTimeNode);
    }

    @Override
    public List<SettleOrderWaitVO> queryWaitSettleOrder(List<Long> spuIds) {
        List<String> skuOrderId = null;
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
        SettleGoods save = settleGoodsDAO.save(settleGoods);
        return save.getId();
    }
    @Override
    public SettleGoods settleGoods(Long settleGoodsId) {
        return settleGoodsDAO.findById(settleGoodsId);
    }
    @Override
    public SettleGoodsVO settleGoodsVO(Long settleGoodsId) {
        SettleGoods byId = settleGoodsDAO.findById(settleGoodsId);
        return TransferUtils.transfer(byId, SettleGoodsVO::new);
    }
    @Override
    public Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsPageReq settleGoodsQuery) {
        return settleGoodsDAO.pageByQuery(settleGoodsQuery);
    }

    @Override
    public boolean settleGoodsEditForExecuteSettle(Long supplierId, Long spuId, Integer settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime) {
        return settleGoodsDAO.executeSettle(supplierId, spuId, settleMoney, settleSkuCount, nextSettlementTime);
    }
    @Override
    public boolean settleGoodsEditForExecuteEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime) {
        return settleGoodsDAO.executeEmptySettle(supplierId, spuId, nextSettlementTime);
    }
    @Override
    public void settleOrderWaitEditForExecuteSettle(List<Long> settleOrderWaitIdList, Integer settleState, LocalDateTime settleTime, Long settleRecordId) {
        if(ObjectUtil.isEmpty(settleOrderWaitIdList)){
            return;
        }
        settleOrderWaitDAO.settleOrderWaitEditForExecuteSettle(settleOrderWaitIdList, settleState, settleTime, settleRecordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleRecordAggCreate(SettleRecordAggVO settleRecord) {
        SettleRecord settleRecord1 = settleRecord.getSettleRecord();
        settleRecordDAO.save(settleRecord1);
        List<SettleRecordItem> settleRecordItemList = settleRecord.getSettleRecordItemList();
        settleRecordItemList.forEach(item -> {
            item.setId(SnowflakeIdAble.getSnowflakeId());
            item.setSettleRecordId(settleRecord1.getId());
        });
        if(ObjectUtil.isNotEmpty(settleRecordItemList)){
            settleRecordItemDAO.batchInsert(settleRecordItemList);
        }
    }

    @Override
    public void settleOrderWaitSaveBatch(List<SettleOrderWait> settleOrderWaitList) {
        settleOrderWaitDAO.saveBatch(settleOrderWaitList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterWaitSettleFreightTimeNode(String spuOrderNo, Long settleNodeTime) {
        settleOrderWaitDAO.alterWaitSettleFreightTimeNode(spuOrderNo, settleNodeTime);
    }

    @Override
    public SettleRecordDetailVO settleRecordDetailVO(Long id) {
        SettleRecordDetailVO settleRecordDetailVO = new SettleRecordDetailVO();
        SettleRecord settleRecordVO = settleRecordDAO.findById(id);
        SettleRecordItemPageReq settleRecordItemQuery = new SettleRecordItemPageReq();
        settleRecordItemQuery.setSettleRecordId(id);
        List<SettleRecordItemVO> settleRecordItemVOS = settleRecordItemDAO.listByQuery(settleRecordItemQuery);
        settleRecordDetailVO.setSettleRecordVO(TransferUtils.transfer(settleRecordVO, SettleRecordVO::new));
        settleRecordDetailVO.setSettleRecordItemVOList(settleRecordItemVOS);
        return settleRecordDetailVO;
    }

    @Override
    public Page<SettleRecordItemVO> settleRecordItemPage(SettleRecordItemPageReq query) {
        return settleRecordItemDAO.pageByQuery(query);
    }

    @Override
    public Integer closeSettleOrder(String skuOrderNo, Long refundId) {
        Long id = settleOrderWaitDAO.idBySkuOrderIdAndType(skuOrderNo, 0L);
        if(id == null){
            return null;
        }else {
            return settleOrderWaitDAO.editRefundState(id, 0, 1, refundId);
        }
    }

    @Override
    public Integer totalSettleAmount() {
        return settleRecordDAO.totalSettleAmount();
    }

    @Override
    public void editSettleRecord(SettleRecordEditReq settleRecordEditReq) {
        settleRecordDAO.updateById(settleRecordEditReq);
    }

    @Override
    public List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList) {
        return settleRecordDAO.settleTypeList(settleTypeList);
    }

//    @Override
//    public Integer querySupplierSettleConfig(Long supplierId) {
//        SettlementConfigOutVO settlementConfigOutVO = supplierFacade.settlementConfig(supplierId);
//        return settlementConfigOutVO.getOrderTypeDay();
//    }
}
