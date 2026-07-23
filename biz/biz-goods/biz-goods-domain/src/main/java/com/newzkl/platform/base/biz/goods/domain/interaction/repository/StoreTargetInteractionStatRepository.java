package com.newzkl.platform.base.biz.goods.domain.interaction.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionSummaryObj;

import java.util.List;

/**
 * 统计仓储接口（DDD 领域仓储，定义领域操作契约）
 * @author sijiwang
 */
public interface StoreTargetInteractionStatRepository {

    /**
     * 批量保存或更新统计记录（数据库同步用）
     * @param statDOList 统计记录列表
     * @return 操作结果
     */
    boolean batchSaveOrUpdate(List<StoreTargetInteractionStat> statDOList);

    /**
     * 根据门店+目标查询统计记录（缓存未命中时查询数据库）
     */
    StoreTargetInteractionStat findByStoreTarget(Long storeId, String targetType, Long targetId);

    /**
     * 物理删除统计记录
     */
    boolean deleteByStoreTarget(Long storeId, String targetType, Long targetId);


    /**
     * 分页查询统计记录
     */
    Page<StoreTargetInteractionStat> pageQuery(StoreTargetInteractionStatPageReq queryVO);

    /**
     * 批量查询统计记录（多条件组合）
     */
    List<StoreTargetInteractionStat> batchQuery(StoreTargetInteractionStatBatchReq queryVO);

    boolean save(StoreTargetInteractionStat domainModel);

    boolean update(StoreTargetInteractionStat domainModel);

    /**
     * 按target_type和target_id查询
     *
     * @param targetTypeList 目标类型列表
     * @param targetIdList   目标ID列表
     * @return
     */
    List<StoreTargetInteractionStat> selectByTargetTypeAndIdList(List<String> targetTypeList, List<Long> targetIdList);

    /**
     * 按targetId分组统计累计值
     *
     * @param targetIdList 目标id
     * @return 分组统计结果
     */
    StoreTargetInteractionSummaryObj selectSummaryByStoreIdList(List<Long> targetIdList);

    /**
     * 按publisher_id分组统计累计值
     *
     * @param publisherId 发布者ID列表
     * @return 分组统计结果
     */
    StoreTargetInteractionSummaryObj selectSummaryByPublisherId(Long publisherId);

    List<StoreTargetInteractionStat> selectByPublisherId(Long publisherId);
}