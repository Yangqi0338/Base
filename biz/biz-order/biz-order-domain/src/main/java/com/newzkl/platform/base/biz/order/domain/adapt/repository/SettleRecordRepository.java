package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordVO;

import java.util.List;

/**
 * 结算记录仓储接口
 * @author fang
 */
public interface SettleRecordRepository {

    /**
     * 保存/更新结算记录信息
     */
    SettleRecord save(SettleRecord settleRecord);

    /**
     * 根据主键查询结算记录信息
     */
    SettleRecord findById(Long id);

    /**
     * 根据主键批量删除结算记录信息
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据查询条件删除记录
     */
    boolean deleteByQuery(SettleRecordPageReq query);

    /**
     * 根据查询条件查询记录列表
     */
    List<SettleRecordVO> listByQuery(SettleRecordPageReq query);

    /**
     * 根据查询条件统计记录数量
     */
    Integer countByQuery(SettleRecordPageReq query);

    /**
     * 分页查询结算记录信息
     */
    Page<SettleRecordVO> pageByQuery(SettleRecordPageReq query);

    /**
     * 总结算金额
     */
    Integer totalSettleAmount();

    /**
     * 更新结算记录信息
     */
    boolean updateById(SettleRecordEditReq settleRecordEditReq);

    /**
     * 获取结算类型列表
     */
    List<SettleOrderWaitVO> settleTypeList(SettleTypeListReq settleTypeList);

    /**
     * 批量插入结算记录信息
     */
    boolean batchInsert(List<SettleRecord> settleRecords);
}