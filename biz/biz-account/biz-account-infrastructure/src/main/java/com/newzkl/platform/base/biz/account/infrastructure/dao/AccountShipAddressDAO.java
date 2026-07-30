package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.ShipAddressDO;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址 DAO
 *
 * @author KC
 */
@Mapper
public interface AccountShipAddressDAO extends BaseMapper<ShipAddressDO> {

    /**
     * 构建收货地址查询条件
     *
     * <p>保留旧 mapper xml {@code listByQuery} 的排序: 默认地址优先, 其次创建时间倒序。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<ShipAddressDO> getLw(ShipAddressQuery query) {
        BaseLambdaQueryWrapper<ShipAddressDO> wrapper = new BaseLambdaQueryWrapper<ShipAddressDO>()
                .notEmptyIn(ShipAddressDO::getId, query.getIdList())
                .notNullEq(ShipAddressDO::getRoleId, query.getRoleId())
                .notNullEq(ShipAddressDO::getAccountId, query.getAccountId())
                .notNullEq(ShipAddressDO::getIsDefault, query.getIsDefault());
        wrapper.orderByDesc(ShipAddressDO::getIsDefault)
                .orderByDesc(ShipAddressDO::getCreateTime);
        return wrapper;
    }
}
