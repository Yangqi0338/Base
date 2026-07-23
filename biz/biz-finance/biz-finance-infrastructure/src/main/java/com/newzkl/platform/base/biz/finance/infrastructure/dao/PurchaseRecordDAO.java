package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PurchaseRecordDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;

/**
 * 购买记录 (purchase_record)表数据库访问层
 *
 * @author kc
 * @since 2025-11-25 17:24:23
 */
public interface PurchaseRecordDAO extends BaseMapper<PurchaseRecordDO> {

    default BaseLambdaQueryWrapper<PurchaseRecordDO> getLw(PurchaseRecordQuery query) {
        BaseLambdaQueryWrapper<PurchaseRecordDO> ew = new BaseLambdaQueryWrapper<>();
        ew.notNullEq(PurchaseRecordDO::getId, query.getId());
        ew.notEmptyLike(PurchaseRecordDO::getPurchaseNo, query.getPurchaseNo());
        ew.notNullEq(PurchaseRecordDO::getType, query.getType());
        ew.notNullEq(PurchaseRecordDO::getTradeNo, query.getTradeNo());
        ew.notNullEq(PurchaseRecordDO::getOrderNo, query.getOrderNo());
        ew.notNullEq(PurchaseRecordDO::getAccountId, query.getAccountId());
        ew.notEmptyLike(PurchaseRecordDO::getAccountName, query.getAccountName());
        ew.notNullEq(PurchaseRecordDO::getPayAmount, query.getPayAmount());
        ew.between(PurchaseRecordDO::getGoodsAmount, query.getGoodsAmountRange());
        ew.notNullEq(PurchaseRecordDO::getPayType, query.getPayMode());
        ew.notNullEq(PurchaseRecordDO::getPayState, query.getPayState());
        ew.notEmptyLike(PurchaseRecordDO::getTripartiteTradeNo, query.getTripartiteTradeNo());
        ew.jsonEq(PurchaseRecordDO::getOrderInfo, "buyMode", false, query.getBuyMode());
        ew.jsonLike(PurchaseRecordDO::getOrderInfo, "seatPackageName", false, query.getSeatPackageName());
        // 手机号/用户名
        // TODO[exector-migrate]: creator 由独立列改为 exector JSON 列, mp lambda 无法引 JSON 内字段, 暂移除按创建人模糊查
        ew.likeList(query.getKeyword(), PurchaseRecordDO::getAccountName);
        ew.between(PurchaseRecordDO::getCreateTime, query.getCreateTime());
        return ew;
    }

}

