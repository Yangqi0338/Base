package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleGoods;
import com.newzkl.platform.base.biz.order.model.order.req.SettleGoodsPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleGoodsVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结算商品仓储接口
 * @author fang
 */
public interface SettleGoodsRepository {

    /**
     * 保存/更新结算商品信息
     */
    SettleGoods save(SettleGoods settleGoods);

    /**
     * 根据主键查询结算商品信息
     */
    SettleGoods findById(Long id);

    /**
     * 根据主键批量删除结算商品信息
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据查询条件删除记录
     */
    boolean deleteByQuery(SettleGoodsPageReq query);

    /**
     * 根据查询条件查询记录列表
     */
    List<SettleGoodsVO> listByQuery(SettleGoodsPageReq query);

    /**
     * 根据查询条件统计记录数量
     */
    Integer countByQuery(SettleGoodsPageReq query);

    /**
     * 分页查询结算商品信息
     */
    Page<SettleGoodsVO> pageByQuery(SettleGoodsPageReq query);

    /**
     * 结算商品表修改 for 执行结算
     */
    boolean executeSettle(Long supplierId, Long spuId, Integer settleMoney, Integer settleSkuCount, LocalDateTime nextSettlementTime);

    /**
     * 结算商品表修改 for 执行空结算
     */
    boolean executeEmptySettle(Long supplierId, Long spuId, LocalDateTime nextSettlementTime);

    /**
     * 批量插入结算商品信息
     */
    boolean batchInsert(List<SettleGoods> settleGoodsList);
}