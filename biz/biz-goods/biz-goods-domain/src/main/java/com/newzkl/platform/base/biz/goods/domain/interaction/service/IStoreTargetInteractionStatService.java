package com.newzkl.platform.base.biz.goods.domain.interaction.service;

import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.BatchSyncStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.InteractionStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.interaction.StoreTargetInteractionStatPageRes;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionEvent;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionSummaryObj;

import java.util.List;

/**
 * 统计应用服务接口
 * @author sijiwang
 */
public interface IStoreTargetInteractionStatService {

    /**
     * 获取门店对象互动统计
     * @param targetTypeCode 目标类型编码
     * @param targetId 目标ID
     * @return 统计结果
     */
    StoreTargetInteractionStat getStat(String targetTypeCode, Long targetId);

    /**
     * 接收互动请求，发送MQ消息（快速响应）
     */
    void submitInteraction(InteractionStatReq requestVO);

    /**
     * MQ消息处理
     */
    void remoteProcess(StoreTargetInteractionEvent event);

    /**
     * 同步缓存数据到数据库
     */
    void syncCacheToDb();

    /**
     * 根据门店+目标查询统计记录（缓存未命中时查询数据库）
     */
    StoreTargetInteractionStat findByStoreTarget(BatchSyncStatReq queryVO);

    /**
     * 物理删除统计记录
     */
    boolean deleteByStoreTarget(BatchSyncStatReq queryVO);

    /**
     * 分页查询门店对象互动统计
     * @param queryVO 分页查询入参
     * @return 分页结果
     */
    StoreTargetInteractionStatPageRes pageQuery(StoreTargetInteractionStatPageReq queryVO);

    /**
     * 批量查询门店对象互动统计（多条件组合）
     * @param queryVO 批量查询入参
     * @return 统计列表
     */
    List<StoreTargetInteractionStat> batchQuery(StoreTargetInteractionStatBatchReq queryVO);

    /**
     * 按target_type和target_id分组统计累计值
     * @param targetTypeList 目标类型列表
     * @param targetIdList   目标ID列表
     * @return 分组统计结果
     */
    List<StoreTargetInteractionSummaryObj> summaryByTargetTypeAndIdList(List<String> targetTypeList, List<Long> targetIdList);

    /**
     * 按publisher_id分组统计累计值
     *
     * @param publisherId@return 分组统计结果
     */
    StoreTargetInteractionSummaryObj selectSummaryByPublisherId(Long publisherId);
}