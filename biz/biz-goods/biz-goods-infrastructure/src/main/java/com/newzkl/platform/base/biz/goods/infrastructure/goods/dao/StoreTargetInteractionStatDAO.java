package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.StoreTargetInteractionStatDO;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionSummaryObj;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 统计DAO
 * @author sijiwang
 */
@Repository
public interface StoreTargetInteractionStatDAO {

    /**
     * 批量插入或更新统计记录
     */
    int batchInsertOrUpdate(@Param("list") List<StoreTargetInteractionStatDO> statList);

    /**
     * 根据门店ID+目标类型+目标ID查询统计记录
     */
    StoreTargetInteractionStatDO selectByStoreTarget(@Param("storeId") Long storeId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    /**
     * 根据条件删除统计记录（替代MP的LambdaQueryWrapper删除）
     */
    int deleteByStoreTarget(@Param("storeId") Long storeId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    /**
     * 分页查询统计记录
     */
    Page<StoreTargetInteractionStatDO> selectPageByCondition(Page<?> page, @Param("condition") Map<String, Object> condition);

    /**
     * 批量查询统计记录（根据条件）
     */
    List<StoreTargetInteractionStatDO> selectListByCondition(@Param("condition") Map<String, Object> condition);

    /**
     * 单条插入（替代MP的insert）
     */
    int insert(StoreTargetInteractionStatDO statDO);

    /**
     * 单条更新（替代MP的updateById）
     */
    int updateById(StoreTargetInteractionStatDO statDO);

    /**
     * 按target_type和target_id分组统计累计值
     * @param targetTypeList 目标类型列表
     * @param targetIdList   目标ID列表
     * @return 分组统计结果
     */
    List<StoreTargetInteractionStatDO> selectByTargetTypeAndIdList(@Param("targetTypeList") List<String> targetTypeList, @Param("targetIdList") List<Long> targetIdList);

    /**
     * 按store_id分组统计累计值
     * @param targetIdList     门店ID列表
     * @return 分组统计结果
     */
    StoreTargetInteractionSummaryObj selectSummaryByStoreIdList(@Param("targetIdList") List<Long> targetIdList);

    /**
     * 按publisher_id分组统计累计值
     * @param publisherId  发布者ID列表
     * @return 分组统计结果
     */
    StoreTargetInteractionSummaryObj selectSummaryByPublisherId(@Param("publisherId") Long publisherId);

    /**
     * 按publisher_id查询统计记录
     * @param publisherId  发布者ID列表
     * @return 统计记录列表
     */
    List<StoreTargetInteractionStatDO> selectByPublisherId(@Param("publisherId") Long publisherId);

}