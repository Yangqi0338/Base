package com.newzkl.platform.base.biz.order.infrastructure.dao.settle;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import com.newzkl.platform.base.biz.order.model.req.query.SettleGoodsQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
* 结算商品信息表
* @author fang
*/
@Mapper
public interface SettleGoodsDAO extends BaseMapper<SettleGoodsDO> {

    /**
     * 构建结算商品查询条件
     *
     * <p>迁移自旧 SettleGoodsDAO.xml {@code <where>}: 条件收敛至 LambdaQueryWrapper。
     * del_flag 由 {@code @TableLogic} 自动追加。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<SettleGoodsDO> getLw(SettleGoodsQuery query) {
        BaseLambdaQueryWrapper<SettleGoodsDO> wrapper = new BaseLambdaQueryWrapper<SettleGoodsDO>()
                .notEmptyEq(SettleGoodsDO::getId, query.getId())
                .notEmptyIn(SettleGoodsDO::getId, query.getIdList())
                .notEmptyEq(SettleGoodsDO::getSupplierId, query.getSupplierId())
                .notEmptyEq(SettleGoodsDO::getSpuId, query.getSpuId())
                ;
        wrapper.le(query.getLessSettleTime() != null, SettleGoodsDO::getNextSettleTime, query.getLessSettleTime());
        return wrapper;
    }
}