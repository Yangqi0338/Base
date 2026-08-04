package com.newzkl.platform.base.biz.order.infrastructure.dao.settle;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
* 结算记录明细表
* @author fang
*/
@Mapper
public interface SettleRecordItemDAO extends BaseMapper<SettleRecordItemDO> {

    /**
     * 构建结算记录明细查询条件
     *
     * <p>迁移自旧 SettleRecordItemDAO.xml {@code <where>}: 条件收敛至 LambdaQueryWrapper。
     * del_flag 由 {@code @TableLogic} 自动追加。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<SettleRecordItemDO> getLw(SettleRecordItemQuery query) {
        return new BaseLambdaQueryWrapper<SettleRecordItemDO>()
                .notEmptyEq(SettleRecordItemDO::getId, query.getId())
                .notEmptyIn(SettleRecordItemDO::getId, query.getIdList())
                .notEmptyEq(SettleRecordItemDO::getSettleRecordId, query.getSettleRecordId())
                .notEmptyLike(SettleRecordItemDO::getSpuName, query.getSpuName());
    }
}