package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.util.ObjectUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISettleRepository;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.SettleGoodsAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.SettleOrderWaitAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.SettleRecordAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.SettleRecordItemAssembler;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleGoodsDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleOrderWaitDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SettleRecordItemDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleOrderWaitDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordAgg;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class SettleRepositoryImpl implements ISettleRepository {

    private final SettleGoodsDAO settleGoodsDAO;

    private final SettleGoodsAssembler settleGoodsAssembler;
    private final SettleOrderWaitDAO settleOrderWaitDAO;
    private final SettleOrderWaitAssembler settleOrderWaitAssembler;
    private final SettleRecordDAO settleRecordDAO;
    private final SettleRecordAssembler settleRecordAssembler;
    private final SettleRecordItemDAO settleRecordItemDAO;
    private final SettleRecordItemAssembler settleRecordItemAssembler;

    @DubboReference
    private ISupplierFacade supplierFacade;

    @Override
    public SettleRecordVO settleRecordVO(Long settleRecordId) {
        SettleRecordDO settleRecordDO = settleRecordDAO.selectById(settleRecordId);
        return settleRecordAssembler.doToVO(settleRecordDO);
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
        SettleGoodsDO settleGoodsDO = settleGoodsAssembler.domainToDO(settleGoods);
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

        List<SettleGoodsVO> settleGoodsVO = settleGoodsDAO.listByQuery(settleGoodsQuery);
        Page<SettleGoodsVO> page = new Page<SettleGoodsVO>( settleGoodsVO);
        return page;
    }

    @Override
    public boolean settleGoodsEditForExecuteSettle(Long supplierId, Long spuId, Integer settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime) {
        return settleGoodsDAO.settleGoodsEditForExecuteSettle(supplierId, spuId, settleMoney, settleSkuCount, nextSettlementTime) > 0;
    }
    @Override
    public boolean settleGoodsEditForExecuteEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime) {
        return settleGoodsDAO.settleGoodsEditForExecuteEmptySettle(supplierId, spuId, nextSettlementTime) > 0;
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
    public void settleRecordAggCreate(SettleRecordAgg settleRecord) {
        SettleRecordDO settleRecordDO = settleRecordAssembler.domainToDO(settleRecord.getSettleRecord());
        settleRecordDAO.insert(settleRecordDO);
        List<SettleRecordItemDO> settleRecordItemDOList = TransferUtils.transfers(settleRecord.getSettleRecordItemList(), item -> settleRecordItemAssembler.domainToDO(item), (c, v) -> {
            v.setId(SnowflakeIdAble.getSnowflakeId());
            v.setSettleRecordId(settleRecordDO.getId());
        });
        if(ObjectUtil.isNotEmpty(settleRecordItemDOList)){
            settleRecordItemDAO.insert(settleRecordItemDOList);
        }
    }

    @Override
    public void settleOrderWaitSaveBatch(List<SettleOrderWait> settleOrderWaitList) {
        List<SettleOrderWaitDO> settleOrderWaitDOList = TransferUtils.transfers(settleOrderWaitList, item -> settleOrderWaitAssembler.domainToDO(item));
        settleOrderWaitDAO.insert(settleOrderWaitDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterWaitSettleFreightTimeNode(Long spuOrderId,Long settleNodeTime) {
        settleOrderWaitDAO.alterWaitSettleFreightTimeNode(spuOrderId, settleNodeTime);
    }

    @Override
    public SettleRecordDetailVO settleRecordDetailVO(Long id) {
        SettleRecordDetailVO settleRecordDetailVO = new SettleRecordDetailVO();
        SettleRecordVO settleRecordVO = settleRecordDAO.selectById(id);
        SettleRecordItemQuery settleRecordItemQuery = new SettleRecordItemQuery();
        settleRecordItemQuery.setSettleRecordId(id);
        List<SettleRecordItemVO> settleRecordItemVOS = settleRecordItemDAO.listByQuery(settleRecordItemQuery);
        settleRecordDetailVO.setSettleRecordVO(settleRecordVO);
        settleRecordDetailVO.setSettleRecordItemVOList(settleRecordItemVOS);
        return settleRecordDetailVO;
    }

    @Override
    public Page<SettleRecordItemVO> settleRecordItemPage(SettleRecordItemQuery query) {

        List<SettleRecordItemVO> list = settleRecordItemDAO.listByQuery(query);
        Page<SettleRecordItemVO> page = new Page<SettleRecordItemVO>(list);
        return page;
    }

    @Override
    public Integer closeSettleOrder(Long skuOrderId, Long refundId) {
        Long id = settleOrderWaitDAO.idBySkuOrderIdAndType(skuOrderId, 0);
        if(id == null){
            return null;
        }else {
            return settleOrderWaitDAO.editRefundState(id, 0, 1, refundId);
        }
    }

    @Override
    public void editSettleRecord(SettleRecordEditReq settleRecordEditReq) {
        settleRecordDAO.updateById(settleRecordEditReq);
    }

    @Override
    public List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList) {
        return settleRecordDAO.settleTypeList(settleTypeList);
    }

    @Override
    public Integer querySupplierSettleConfig(Long supplierId) {
        SettlementConfigOutVO settlementConfigOutVO = supplierFacade.settlementConfig(supplierId);
        return settlementConfigOutVO.getOrderTypeDay();
    }
}
