package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecordItem;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordItemPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordItemVO;

import java.util.List;

/**
 * 结算记录明细仓储接口
 * @author fang
 */
public interface SettleRecordItemRepository {

    /**
     * 保存/更新结算记录明细信息
     */
    SettleRecordItem save(SettleRecordItem settleRecordItem);

    /**
     * 根据主键查询结算记录明细信息
     */
    SettleRecordItem findById(Long id);

    /**
     * 根据主键批量删除结算记录明细信息
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据查询条件删除记录
     */
    boolean deleteByQuery(SettleRecordItemPageReq query);

    /**
     * 根据查询条件查询记录列表
     */
    List<SettleRecordItemVO> listByQuery(SettleRecordItemPageReq query);

    /**
     * 根据查询条件统计记录数量
     */
    Integer countByQuery(SettleRecordItemPageReq query);

    /**
     * 分页查询结算记录明细信息
     */
    Page<SettleRecordItemVO> pageByQuery(SettleRecordItemPageReq query);

    /**
     * 批量插入结算记录明细信息
     */
    boolean batchInsert(List<SettleRecordItem> settleRecordItems);
}