package com.newzkl.platform.base.biz.user.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.application.relation.service.UserCollectionService;
import com.newzkl.platform.base.biz.user.model.relation.req.UncollectedProductReq;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionCreateReq;
import com.newzkl.platform.base.biz.user.model.relation.query.UserCollectionQuery;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionReq;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserCollectionDTO;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-收藏
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.UserCollectionController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明: 旧 page 回 {@code PageInfo<UserCollectionVO>}, 本仓保留 {@code Page} 分页壳直返;
 * 旧出参 {@code UserCollectionVO} 归并为 {@code UserCollection}(sellNum 字段已补, 但跨域富化未接,
 * 恒为 null, 见服务层跨域缺口)。<b>前端契约变</b> (出参 list → records)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/collection")
@Slf4j
@RequiredArgsConstructor
@FuncPermission("用户收藏")
public class UserCollectionController {

    private final UserCollectionService userCollectionService;

    /**
     * 收藏商品
     *
     * @param createReq 收藏创建入参
     * @return 收藏记录
     */
    @PostMapping("/collect")
    @FuncPermission("收藏商品")
    public PlatformResult<UserCollectionDTO> collectProduct(@Validated @RequestBody UserCollectionCreateReq createReq) {
        createReq.setUserId(SecurityUtils.getAccountId());
        createReq.setUserName(SecurityUtils.getUsername());
        log.info("用户收藏商品: userId={}, storeGoodsId={}", createReq.getUserId(), createReq.getStoreGoodsId());
        return PlatformResult.success(userCollectionService.collectProduct(createReq));
    }

    /**
     * 取消收藏商品
     *
     * @param req 取消收藏入参
     * @return 是否成功
     */
    @PostMapping("/uncollectedProduct")
    @FuncPermission("取消收藏商品")
    public PlatformResult<Boolean> uncollectedProduct(@Validated @RequestBody UncollectedProductReq req) {
        log.info("用户取消收藏商品: {}", req);
        return PlatformResult.success(userCollectionService.uncollectProduct(req));
    }

    /**
     * 取消全部失效收藏
     *
     * @return 是否成功
     */
    @PostMapping("/uncollectedInvalidProduct")
    @FuncPermission("取消全部失效收藏")
    public PlatformResult<Boolean> uncollectedInvalidProduct() {
        return PlatformResult.success(userCollectionService.uncollectedInvalidProduct());
    }

    /**
     * 查询用户的全部收藏
     *
     * @return 收藏列表
     */
    @PostMapping("/list")
    public PlatformResult<List<UserCollectionDTO>> getUserCollections() {
        Long userId = SecurityUtils.getAccountId();
        log.info("查询用户收藏列表: userId={}", userId);
        return PlatformResult.success(userCollectionService.getUserCollections(userId));
    }

    /**
     * 检查用户是否已收藏指定商品
     *
     * @param req 查询入参
     * @return 是否已收藏
     */
    @PostMapping("/check")
    public PlatformResult<Boolean> checkIsCollected(@Validated @RequestBody UserCollectionReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        log.info("检查用户是否收藏商品: userId={}, storeGoodsId={}", req.getUserId(), req.getStoreGoodsId());
        return PlatformResult.success(userCollectionService.checkIsCollected(req));
    }

    /**
     * 分页查询用户收藏
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * {@code mmt-app} 在调此端点 ({@code pages/subOrder/collection.vue})</p>
     *
     * @param query 分页查询
     * @return 收藏分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<UserCollectionDTO>> getUserCollectionsPage(@Validated @RequestBody UserCollectionQuery query) {
        query.setUserId(SecurityUtils.getAccountId());
        log.info("分页查询用户收藏: userId={}, pageNo={}, pageSize={}", query.getUserId(), query.getPageNo(), query.getPageSize());
        return PlatformResult.success(userCollectionService.getUserCollectionsPage(query));
    }
}
