package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.domain.interaction.service.StoreTargetInteractionStatService;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.BatchSyncStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.InteractionStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.interaction.StoreTargetInteractionStatPageRes;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionEvent;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionSummaryObj;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
 * 门店对象互动统计控制器。
 *
 * <p>对外提供互动统计的提交、查询、删除及汇总 HTTP 接口。</p>
 *
 * @author sijiwang
 */
@RestController
@RequestMapping("/goods/store")
@RequiredArgsConstructor
@Slf4j
public class StoreTargetInteractionStatController {

    private final StoreTargetInteractionStatService statService;

    /**
     * 提交互动统计 (点赞/浏览/转发)。
     *
     * @param requestVO 互动统计请求
     * @return 成功结果
     */
    @PostMapping("/interaction")
    public ScmResult<String> submitInteraction(@Validated @RequestBody InteractionStatReq requestVO) {
        statService.submitInteraction(requestVO);
        return ScmResult.success();
    }

    /**
     * 查询单个统计记录 (门店+目标)。
     *
     * @param queryVO 查询条件
     * @return 统计记录
     */
    @PostMapping("/interaction/query")
    public ScmResult<StoreTargetInteractionStat> findByStoreTarget(@Validated @RequestBody BatchSyncStatReq queryVO) {
        return ScmResult.success(statService.findByStoreTarget(queryVO));
    }

    /**
     * 删除统计记录 (门店+目标)。
     *
     * @param queryVO 查询条件
     * @return 是否删除成功
     */
    @DeleteMapping("/interaction/delete")
    public ScmResult<Boolean> deleteByStoreTarget(@Validated @RequestBody BatchSyncStatReq queryVO) {
        return ScmResult.success(statService.deleteByStoreTarget(queryVO));
    }

    /**
     * 分页查询统计记录。
     *
     * @param queryVO 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/interaction/page")
    public ScmResult<StoreTargetInteractionStatPageRes> pageQuery(@Validated @RequestBody StoreTargetInteractionStatPageReq queryVO) {
        return ScmResult.success(statService.pageQuery(queryVO));
    }

    /**
     * 批量查询统计记录。
     *
     * @param queryVO 批量查询条件
     * @return 统计记录列表
     */
    @PostMapping("/interaction/batch")
    public ScmResult<List<StoreTargetInteractionStat>> batchQuery(@Validated @RequestBody StoreTargetInteractionStatBatchReq queryVO) {
        return ScmResult.success(statService.batchQuery(queryVO));
    }

    /**
     * 处理互动统计事件 (缓存更新)。
     *
     * @param requestVO 互动统计事件
     * @return 成功结果
     */
    @PostMapping("/remoteProcess")
    public ScmResult<String> remoteProcess(@Validated @RequestBody StoreTargetInteractionEvent requestVO) {
        statService.remoteProcess(requestVO);
        return ScmResult.success();
    }

    /**
     * 按目标类型与目标 ID 汇总统计。
     *
     * @param targetType 目标类型
     * @param targetId   目标 ID
     * @return 汇总统计列表
     */
    @GetMapping("/summary")
    public ScmResult<List<StoreTargetInteractionSummaryObj>> summaryByTargetTypeAndIdList(@RequestParam String targetType,
                                                                                          @RequestParam Long targetId) {
        return ScmResult.success(statService.summaryByTargetTypeAndIdList(
                Collections.singletonList(targetType), Collections.singletonList(targetId)));
    }

    /**
     * 按发布者 ID 汇总统计。
     *
     * @param publisherId 发布者 ID
     * @return 汇总统计结果
     */
    @GetMapping("/selectSummaryByPublisherId")
    public ScmResult<StoreTargetInteractionSummaryObj> selectSummaryByPublisherId(@RequestParam Long publisherId) {
        return ScmResult.success(statService.selectSummaryByPublisherId(publisherId));
    }

    // TODO[debug-drop]: 源 queryCache/deleteCache/existsCache/getAllByPattern 为直连 RedisUtil 的调试端点,
    // 属基建调试脚手架 (非业务), 迁移中裁剪; 如需 Redis 运维接口应经 sys 域统一提供。
}
