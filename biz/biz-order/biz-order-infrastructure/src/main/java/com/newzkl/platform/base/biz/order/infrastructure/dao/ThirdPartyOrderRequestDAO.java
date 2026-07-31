package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.ThirdPartyOrderRequestDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ThirdPartyOrderRequestDAO extends BaseMapper<ThirdPartyOrderRequestDO> {
    Optional<ThirdPartyOrderRequestDO> selectByBizOrderNo(@Param("bizOrderNo") String bizOrderNo);
    
    List<ThirdPartyOrderRequestDO> selectByStatusAndNextRetryTimeBefore(@Param("platformType") String platformType,@Param("requestStatus") int requestStatus,@Param("interfaceName") String interfaceName);
    

}