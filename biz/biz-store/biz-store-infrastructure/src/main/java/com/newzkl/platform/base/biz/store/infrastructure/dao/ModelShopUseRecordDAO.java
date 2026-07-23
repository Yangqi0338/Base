package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopUseRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店使用记录数据访问接口
 */
@Mapper
public interface ModelShopUseRecordDAO extends BaseMapper<ModelShopUseRecordDO> {

    Page<ModelShopStorePageRes> selectLatestRecordsByGroup(Page<?> page, @Param("query") ModelShopStorePageQuery query);

    /**
     * 按天统计新增使用门店数量
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的新增门店数量统计
     */
    List<ModeShopDataSummary> countNewStoreByDay(@Param("modelShopId") Long modelShopId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}