package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
* 结算商品信息表
* @author fang
*/
@Mapper
public interface SettleGoodsDAO extends BaseMapper<SettleGoodsDO> {
    /**
     * 结算商品表修改 for 执行结算
     * @param supplierId 供应商ID
     * @param spuId spuId
     * @param settleMoney 结算金额
     * @param settleSkuCount 结算商品数量
     * @param nextSettlementTime 结算周期
     */
    int settleGoodsEditForExecuteSettle(@Param("supplierId") Long supplierId, @Param("spuId") Long spuId, @Param("settleMoney") Integer settleMoney, @Param("settleSkuCount") Integer settleSkuCount, @Param("nextSettlementTime") LocalDateTime nextSettlementTime);
    /**
     * 结算商品表修改 for 执行空结算
     * @param supplierId 供应商ID
     * @param spuId spuId
     * @param nextSettlementTime 结算周期
     */
    int settleGoodsEditForExecuteEmptySettle(@Param("supplierId") Long supplierId, @Param("spuId") Long spuId, @Param("nextSettlementTime") LocalDateTime nextSettlementTime);
}