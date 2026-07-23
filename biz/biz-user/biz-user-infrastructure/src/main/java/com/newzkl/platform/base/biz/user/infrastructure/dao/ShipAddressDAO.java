package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.ShipAddressDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址数据访问接口。
 *
 * @author sijiwang
 */
@Mapper
public interface ShipAddressDAO extends BaseMapper<ShipAddressDO> {
}
