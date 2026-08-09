package com.newzkl.platform.base.biz.order.infrastructure.dao.settle;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.model.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 结算记录表
* @author fang
*/
@Mapper
public interface SettleRecordDAO extends BaseMapper<SettleRecordDO> {

    List<SettleOrderWaitVO> settleTypeList(@Param("query") SettleTypeListReq settleTypeList);
}