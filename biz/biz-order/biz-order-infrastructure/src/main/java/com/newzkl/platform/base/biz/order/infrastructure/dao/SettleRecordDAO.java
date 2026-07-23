package com.newzkl.platform.base.biz.order.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算记录表
 * @author fang
 */
@Mapper
public interface SettleRecordDAO extends BaseMapper<SettleRecordDO> {

    /**
     * 总结算金额
     */
    Integer totalSettleAmount();

    /**
     * 更新结算记录信息
     */
    void updateById(@Param("model") SettleRecordEditReq settleRecordEditReq);

    /**
     * 获取结算类型列表
     */
    List<SettleOrderWaitVO> settleTypeList(@Param("query") SettleTypeListReq settleTypeList);

    /**
     * 根据查询条件统计记录数量
     */
    Integer countByQuery(@Param("query") SettleRecordPageReq query);
}