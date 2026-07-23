package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.order.req.*;
import com.newzkl.platform.base.biz.order.model.order.vo.*;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
public interface SettleDomain {
    /**
     * 结算记录表-创建
     * @param settleRecord
     * @return
     */
    Long settleRecordSave(SettleRecord settleRecord);
    /**
     * 结算记录表-实体
     * @param settleRecordId
     * @return
     */
    SettleRecord settleRecord(Long settleRecordId);
    /**
     * 结算记录表-值对象
     * @param settleRecordId
     * @return
     */
    SettleRecordVO settleRecordVO(Long settleRecordId);
    /**
     * 结算记录表-值对象列表
     *
     * @param settleRecordQuery
     * @return
     */
    Page<SettleRecordVO> settleRecordVOList(SettleRecordPageReq settleRecordQuery);
    /**
     * 待结算订单信息表-创建
     * @param settleOrderWait
     * @return
     */
    Long settleOrderWaitSave(SettleOrderWait settleOrderWait);
    /**
     * 待结算订单信息表-实体
     * @param settleOrderWaitId
     * @return
     */
    SettleOrderWait settleOrderWait(Long settleOrderWaitId);
    /**
     * 待结算订单信息表-值对象
     * @param settleOrderWaitId
     * @return
     */
    SettleOrderWaitVO settleOrderWaitVO(Long settleOrderWaitId);
    /**
     * 待结算订单信息表-值对象列表
     *
     * @param settleOrderWaitQuery
     * @return
     */
    Page<SettleOrderWaitVO> settleOrderWaitVOList(SettleOrderWaitPageReq settleOrderWaitQuery);

    List<SettleOrderWaitVO> queryWaitSettleOrderTimeNode(Long settleTimeNode);

    /**
     * 查询商品待结算
     * @param spuIds
     * @return
     */
    List<SettleOrderWaitVO> queryWaitSettleOrder(List<Long> spuIds);
    /**
     * 结算商品信息表-创建
     * @param settleGoods
     * @return
     */
    Long settleGoodsSave(SettleGoods settleGoods);
    /**
     * 结算商品信息表-实体
     * @param settleGoodsId
     * @return
     */
    SettleGoods settleGoods(Long settleGoodsId);
    /**
     * 结算商品信息表-值对象
     * @param settleGoodsId
     * @return
     */
    SettleGoodsVO settleGoodsVO(Long settleGoodsId);
    /**
     * 结算商品信息表-值对象列表
     *
     * @param settleGoodsQuery
     * @return
     */
    Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsPageReq settleGoodsQuery);

    /**
     * 结算商品表修改 for 执行结算
     * @param supplierId 供应商ID
     * @param spuId spuId
     * @param settleMoney 结算金额
     * @param settleSkuCount 结算商品数量
     * @param nextSettlementTime 下次结算时间
     */
    boolean settleGoodsEditForExecuteSettle(Long supplierId, Long spuId, Integer settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime);
    /**
     * 结算商品表修改 for 执行空结算
     * @param supplierId 供应商ID
     * @param spuId spuId
     * @param nextSettlementTime 下次结算时间
     */
    boolean settleGoodsEditForExecuteEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime);
    /**
     * 待结算订单修改 for 执行结算
     * @param settleOrderWaitIdList 待结算订单ID
     * @param settleState 结算状态
     * @param settleTime 结算时间
     * @param settleRecordId 结算记录ID
     */
    void settleOrderWaitEditForExecuteSettle(List<Long> settleOrderWaitIdList, Integer settleState, LocalDateTime settleTime, Long settleRecordId);
    /**
     * 结算记录聚合保存
     * @param settleRecord
     */
    void settleRecordAggCreate(SettleRecordAggVO settleRecord);

    /**
     * 待结算订单信息表-批量创建
     * @param settleOrderWaitList
     */
    void settleOrderWaitSaveBatch(List<SettleOrderWait> settleOrderWaitList);

    /**
     * 更新待结算运费时间节点
     *
     * @param spuOrderNo
     */
    void alterWaitSettleFreightTimeNode(String spuOrderNo,Long settleNodeTime);

    SettleRecordDetailVO settleRecordDetailVO(Long id);

    Page<SettleRecordItemVO> settleRecordItemPage(SettleRecordItemPageReq settleRecordItemQuery);

    Integer closeSettleOrder(String skuOrderNo, Long refundId);

    Integer totalSettleAmount();

    void editSettleRecord(SettleRecordEditReq settleRecordEditReq);

    List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList);

//    Integer querySupplierSettleConfig(Long supplierId);
}
