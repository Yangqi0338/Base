package com.newzkl.platform.base.biz.market.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.vo.market.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 市场服务控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.market.MarketController},
 * 类级与方法级路径、HTTP verb 逐字沿用。</p>
 *
 * <p>未迁端点: {@code POST /queryMarketUnBindUser} 返回 user 域
 * {@code ChannelOutVO}, 须经 account 域 Dubbo Facade 查询未绑定渠道商,
 * Base 侧 market 域无对应出站端口, 补该端口须为 biz-market-infrastructure
 * 增加 {@code biz-account-facade} 依赖 (pom 改动), 故记阻塞不补。</p>
 *
 * <p>旧 {@code saveMarketCategory} / {@code querySelectorGoodsCategory} 两个端点不迁:
 * 二者在 new-scm 侧均无 {@code @Deprecated} 标记, 判死依据是无存活前端调用方
 * ({@code saveMarketCategory} 仅被 platform-admin 的 {@code marketCategoryList.vue} 引用,
 * 而该文件 import 的 {@code @/api/market} 模块不存在且未挂路由, 属遗留孤儿页)。
 * {@code MarketDomain} 侧对等能力 ({@code saveMarketCategory} /
 * {@code queryMarketGoodsCategory(null, userId)}) 已具备, 后续如确认仍需对外暴露可直接补。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
@Slf4j
@FuncPermission("市场管理")
public class MarketController {

    private final MarketDomain marketDomain;

    /**
     * 获取绑定市场列表
     *
     * @param query 查询条件
     * @return 绑定市场列表
     */
    @PostMapping("/queryBindMarketList")
    public PlatformResult<Page<MarketRes>> queryChannelBindMarketList(@RequestBody MarketQuery query) {
        query.setBindAccountId(SecurityUtils.getAccountId());
        query.setBindIdentity(SecurityUtils.getIdentity());
        return PlatformResult.success(marketDomain.queryMarketList(query));
    }

    /**
     * 获取绑定市场列表对应的商品
     *
     * @param query 查询条件
     * @return 绑定市场商品列表
     */
    @PostMapping("/queryMarketGoodsList")
    public PlatformResult<Page<BindMarketGoodsRes>> queryMarketGoodsList(@RequestBody AppBindMarketGoodsPageQuery query) {
        return PlatformResult.success(marketDomain.queryMarketGoods(query));
    }

    /**
     * 查询市场列表
     *
     * @param req 查询条件
     * @return 市场列表
     */
    @PostMapping("/queryMarketList")
    public PlatformResult<Page<MarketRes>> queryMarketList(@RequestBody MarketQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(marketDomain.queryMarketList(query));
    }

    /**
     * 查询市场
     *
     * @param marketId 市场ID
     * @return 市场详情
     */
    @PostMapping("/queryMarket/{marketId}")
    public PlatformResult<MarketRes> queryMarket(@PathVariable Long marketId) {
        return PlatformResult.success(marketDomain.queryMarket(marketId));
    }

    /**
     * 保存市场
     *
     * @param req 市场请求
     * @return 操作结果
     */
    @PostMapping("/saveMarket")
    @FuncPermission("保存市场")
    public PlatformResult<Object> saveMarket(@RequestBody MarketReq req) {
        MarketDTO marketDTO = TransferUtils.transfer(req, MarketDTO.class);
        marketDTO.setCategoryId(req.getCategoryId());
        marketDomain.saveMarket(marketDTO);
        return PlatformResult.success();
    }

    /**
     * 渠道商绑定交易市场
     *
     * @param req 绑定请求
     * @return 绑定ID
     */
    @PostMapping("/appChannelBindMarket")
    @FuncPermission("APP渠道商绑定市场")
    public PlatformResult<Object> appChannelBindMarket(@RequestBody ClientBindMarketReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        return PlatformResult.success(marketDomain.appChannelBindMarket(req));
    }

    /**
     * 解除绑定
     *
     * @param id 绑定ID
     * @return 操作结果
     */
    @PostMapping("/deBindMarket/{id}")
    @FuncPermission("解除绑定市场")
    public PlatformResult<Object> deBindMarket(@PathVariable Long id) {
        marketDomain.deBindMarket(id, SecurityUtils.getAccountId());
        return PlatformResult.success();
    }

    /**
     * 查询用户绑定市场列表
     *
     * @param req 查询条件
     * @return 绑定市场列表
     */
    @PostMapping("/queryBindMarket")
    public PlatformResult<List<BindMarketVO>> queryBindMarket(@RequestBody BindMarketListReq req) {
        if (req.getBindAccountId() == null) {
            req.setBindAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(marketDomain.queryBindMarket(req));
    }

    /**
     * 查询市场绑定用户列表
     *
     * @param req 查询条件
     * @return 市场用户列表
     */
    @PostMapping("/queryMarketUser")
    public PlatformResult<List<MarketUserVO>> queryMarketUser(@RequestBody MarketUserReq req) {
        return PlatformResult.success(marketDomain.queryMarketUser(req));
    }
}
