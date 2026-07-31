package com.newzkl.platform.base.biz.order.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.DeliverDO;
import com.newzkl.platform.base.biz.order.model.req.DeliverQuery;
import com.newzkl.platform.base.biz.order.model.vo.DeliverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 发货单
* @author fang
*/
@Mapper
@Repository
public interface DeliverDAO extends BaseMapper<DeliverDO> {


    List<DeliverVO> listByOutOrderNo(@Param("outOrderList") List<String> outOrderList);
}