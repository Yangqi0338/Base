package com.newzkl.platform.base.biz.store.domain.template.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopUseRecordDTO;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店使用记录仓储接口
 */
public interface ModelShopUseRecordRepository {

    void create(ModelShopUseRecordDTO modelShopUseRecord);

    Page<ModelShopStorePageRes> modelShopStorePage(ModelShopStorePageQuery query);

    /**
     * 按天统计新增使用门店数量
     * @param modelShopId 样板店ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天的新增门店数量统计
     */
    List<ModeShopDataSummary> countNewStoreByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime);
}