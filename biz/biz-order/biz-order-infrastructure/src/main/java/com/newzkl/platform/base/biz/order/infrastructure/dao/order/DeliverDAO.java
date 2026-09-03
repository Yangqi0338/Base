package com.newzkl.platform.base.biz.order.infrastructure.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.DeliverDO;
import com.newzkl.platform.base.biz.order.model.req.query.DeliverQuery;
import com.newzkl.platform.base.biz.order.model.vo.DeliverVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
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

    // TODO 将各类的订单号冗余到Deliver，让这个接口不连表查询
    List<DeliverVO> listByOutOrderNo(@Param("outOrderList") List<String> outOrderList);

    default BaseLambdaQueryWrapper<DeliverDO> getLw(DeliverQuery deliverQuery) {
        return new BaseLambdaQueryWrapper<DeliverDO>()
                .notEmptyIn(DeliverDO::getOrderNo, deliverQuery.getOrderNoList());
    }
}