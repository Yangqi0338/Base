package com.newzkl.platform.base.biz.order.domain.adapt.repository;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordAgg;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordItemDTO;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.SettleGoodsQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
public interface ISettleRepository {
    /**
     * 结算记录表-值对象
     * @param settleRecordId
     * @return
     */
    SettleRecordVO settleRecordVO(Long settleRecordId);
    /**
     * 结算记录表-值对象列表
     * @param settleRecordQuery
     * @return
     */
    Page<SettleRecordVO> settleRecordVOList(SettleRecordQuery settleRecordQuery);

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
     * 结算商品信息表-值对象列表
     * @param settleGoodsQuery
     * @return
     */
    Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsQuery settleGoodsQuery);

    /**
     * 结算商品表修改 for 执行结算
     * @param supplierId 供应商ID
     * @param spuId spuId
     * @param settleMoney 结算金额
     * @param settleSkuCount 结算商品数量
     * @param nextSettlementTime 下次结算时间
     */
    boolean settleGoodsEditForExecuteSettle(Long supplierId, Long spuId, Money settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime);
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
    int settleOrderWaitEditForExecuteSettle(List<Long> settleOrderWaitIdList, Integer settleState, LocalDateTime settleTime, Long settleRecordId);
    /**
     * 结算记录聚合保存
     * @param settleRecord
     */
    void settleRecordAggCreate(SettleRecordAgg settleRecord);

    /**
     * 待结算订单信息表-批量创建
     * @param settleOrderWaitList
     */
    void settleOrderWaitSaveBatch(List<SettleOrderWait> settleOrderWaitList);

    /**
     * 更新待结算运费时间节点
     * @param spuOrderId
     */
    void alterWaitSettleFreightTimeNode(Long spuOrderId,Long settleNodeTime);

    SettleRecordDetailVO settleRecordDetailVO(Long id);

    Page<SettleRecordItemDTO> settleRecordItemPage(SettleRecordItemQuery settleRecordItemQuery);

    Integer closeSettleOrder(Long skuOrderId, Long refundId);

    void editSettleRecord(SettleRecordEditReq settleRecordEditReq);

    List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList);

    /**
     * 查询供应商结算延迟天数(periodSetConfig.orderTypeDay), 用于结算类型2的N天后结算
     *
     * @param supplierId 供应商ID
     * @return 延迟天数
     */
    Integer querySupplierSettleConfig(Long supplierId);
}
