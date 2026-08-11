package com.newzkl.platform.base.biz.goods.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.goods.infrastructure.entity.ThirdPartyGoodsRecordDO;
import com.newzkl.platform.base.biz.goods.model.biz.req.query.ThirdPartyGoodsRecordQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThirdPartyGoodsRecordDAO extends BaseMapper<ThirdPartyGoodsRecordDO> {

    default BaseLambdaQueryWrapper<ThirdPartyGoodsRecordDO> getLw(ThirdPartyGoodsRecordQuery query) {
        return new BaseLambdaQueryWrapper<ThirdPartyGoodsRecordDO>()
                .notEmptyEq(ThirdPartyGoodsRecordDO::getOutSpuId, query.getOutSpuId())
                .notEmptyEq(ThirdPartyGoodsRecordDO::getPlatformType, query.getPlatformType())
                .notEmptyEq(ThirdPartyGoodsRecordDO::getRequestStatus, query.getRequestStatus())
                .notEmptyEq(ThirdPartyGoodsRecordDO::getInterfaceName, query.getInterfaceName());
    }
}
