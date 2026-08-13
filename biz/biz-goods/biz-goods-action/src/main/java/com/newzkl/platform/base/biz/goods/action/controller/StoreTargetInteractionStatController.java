package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.domain.interaction.service.StoreTargetInteractionStatService;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.BatchSyncStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.InteractionStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.interaction.StoreTargetInteractionStatPageRes;
import com.newzkl.platform.base.common.ddd.facade.StoreTargetInteractionSummaryObj;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 门店对象互动统计控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/store")
@RequiredArgsConstructor
@Slf4j
public class StoreTargetInteractionStatController {

    private final StoreTargetInteractionStatService statService;

    /**
     * 提交互动统计 (点赞/浏览/转发)
     *
     * @param req 互动统计请求
     * @return 空结果
     */
    @PostMapping("/interaction")
    public PlatformResult<String> submitInteraction(@Validated @RequestBody InteractionStatReq req) {
        log.info("接收互动统计请求：{}", req);
        statService.submitInteraction(req);
        return PlatformResult.success();
    }

    /**
     * 查询单条统计记录 (门店 + 目标)
     *
     * @param req 门店目标查询请求
     * @return 统计记录
     */
    @PostMapping("/interaction/query")
    public PlatformResult<StoreTargetInteractionStat> findByStoreTarget(@Validated @RequestBody BatchSyncStatReq req) {
        return PlatformResult.success(statService.findByStoreTarget(req));
    }

    /**
     * 删除统计记录 (门店 + 目标)
     *
     * @param req 门店目标查询请求
     * @return 是否成功
     */
    @DeleteMapping("/interaction/delete")
    public PlatformResult<Boolean> deleteByStoreTarget(@Validated @RequestBody BatchSyncStatReq req) {
        return PlatformResult.success(statService.deleteByStoreTarget(req));
    }

    /**
     * 统计记录分页
     *
     * @param req 分页查询请求
     * @return 分页结果
     */
    @PostMapping("/interaction/page")
    public PlatformResult<StoreTargetInteractionStatPageRes> pageQuery(@Validated @RequestBody StoreTargetInteractionStatPageReq req) {
        return PlatformResult.success(statService.pageQuery(req));
    }

    /**
     * 批量查询统计记录
     *
     * @param req 批量查询请求
     * @return 统计记录列表
     */
    @PostMapping("/interaction/batch")
    public PlatformResult<List<StoreTargetInteractionStat>> batchQuery(@Validated @RequestBody StoreTargetInteractionStatBatchReq req) {
        return PlatformResult.success(statService.batchQuery(req));
    }


    /**
     * 按目标类型与目标主键查询汇总统计
     *
     * @param targetType 目标类型
     * @param targetId   目标主键
     * @return 汇总统计列表
     */
    @GetMapping("/summary")
    public PlatformResult<List<StoreTargetInteractionSummaryObj>> summaryByTargetTypeAndIdList(@RequestParam String targetType,
                                                                                              @RequestParam Long targetId) {
        return PlatformResult.success(statService.summaryByTargetTypeAndIdList(
                Collections.singletonList(targetType), Collections.singletonList(targetId)));
    }





    /**
     * 按发布者主键查询汇总统计
     *
     * @param publisherId 发布者主键
     * @return 汇总统计
     */
    @GetMapping("/selectSummaryByPublisherId")
    public PlatformResult<StoreTargetInteractionSummaryObj> selectSummaryByPublisherId(@RequestParam Long publisherId) {
        return PlatformResult.success(statService.selectSummaryByPublisherId(publisherId));
    }
}
