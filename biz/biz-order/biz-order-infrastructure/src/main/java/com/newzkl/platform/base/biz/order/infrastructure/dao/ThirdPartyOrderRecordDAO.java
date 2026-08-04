package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.ThirdPartyOrderRecordDO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThirdPartyOrderRecordDAO extends BaseMapper<ThirdPartyOrderRecordDO> {
    
    default BaseLambdaQueryWrapper<ThirdPartyOrderRecordDO> getLw(ThirdPartyOrderRecordQuery query) {
        return new BaseLambdaQueryWrapper<ThirdPartyOrderRecordDO>()
                .notEmptyEq(ThirdPartyOrderRecordDO::getBizOrderNo, query.getBizOrderNo())
                .notEmptyEq(ThirdPartyOrderRecordDO::getPlatformType, query.getPlatformType())
                .notEmptyEq(ThirdPartyOrderRecordDO::getRequestStatus, query.getRequestStatus())
                .notEmptyEq(ThirdPartyOrderRecordDO::getInterfaceName, query.getInterfaceName())
                ;
    }
}