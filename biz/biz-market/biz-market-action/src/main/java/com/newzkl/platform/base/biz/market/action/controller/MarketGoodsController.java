package com.newzkl.platform.base.biz.market.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.application.service.MarketGoodsService;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 市场商品服务控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.relation.MarketGoodsController},
 * 类级与方法级路径、HTTP verb 逐字沿用。</p>
 *
 * <p>旧 {@code appQueryMarketGoodList} / {@code channelDistributionSelectedGoodsList} /
 * {@code channelBatchCancelSelected} 三个端点不迁: 三者在 new-scm 侧均无
 * {@code @Deprecated} 标记, 判死依据是全前端仓无调用方。{@code GoodsRelationDomain}
 * 侧对等能力均已具备, 后续如确认仍需对外暴露可直接补。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/marketGoods")
@RequiredArgsConstructor
@Slf4j
@FuncPermission("市场商品")
public class MarketGoodsController {

    private final GoodsRelationDomain goodsRelationDomain;

    private final MarketGoodsService marketGoodsService;

    /**
     * 市场添加商品
     *
     * @param req 保存商品关系请求
     * @return 操作结果
     */
    @PostMapping("/saveMarketGoodsRelation")
    @FuncPermission("市场添加商品")
    public PlatformResult<Object> saveMarketGoodsRelation(@RequestBody SaveGoodsRelationReq req) {
        marketGoodsService.saveMarketGoodsRelation(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商市场选品
     *
     * <p>行为差异: 旧 application 层落库后另调 user 域 {@code ChannelFacade#editMarketCount}
     * 累加渠道商选品计数, 并按 {@code RedisEnum.Key.CHANNEL_RELATION} 删除该渠道商的选品缓存;
     * 两者均属跨域出站, Base 侧 market 域无对应端口 (补则须为 biz-market-infrastructure
     * 增加 {@code biz-account-facade} 依赖), 故计数与缓存清理未接线, 与同类
     * {@code operatorBindOneMarket} / {@code saveTwoLevelMarket} 的处理一致。</p>
     *
     * @param req 保存商品关系请求
     * @return 操作结果
     */
    @PostMapping("/saveMarketGoodsSelectRelation")
    @FuncPermission("渠道商市场选品")
    public PlatformResult<Object> saveMarketGoodsSelectRelation(@RequestBody SaveGoodsRelationReq req) {
        marketGoodsService.saveMarketGoodsSelectRelation(req, SecurityUtils.getAccountId());
        return PlatformResult.success();
    }

    /**
     * 平台查询市场商品
     *
     * @param req 查询条件
     * @return 市场商品列表
     */
    @PostMapping("/platformQueryMarketGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> platformQueryMarketGoodsList(@RequestBody GoodsListPageQuery req) {
        return PlatformResult.success(goodsRelationDomain.platformQueryMarketGoodsList(req));
    }

    /**
     * 查询客户绑定市场商品关系列表
     *
     * @param req 查询条件
     * @return 市场商品列表
     */
    @PostMapping("/queryClientBindMarketGoodsRelationList")
    public PlatformResult<Page<GoodsRelationListVO>> queryClientBindMarketGoodsRelationList(@RequestBody GoodsListPageQuery req) {
        if (req.getUserId() == null) {
            req.setUserId(SecurityUtils.getAccountId());
        }
        req.setBindType(AccountEnum.Identity.CHANNEL);
        return PlatformResult.success(goodsRelationDomain.queryClientBindMarketGoodsRelationList(req));
    }

    /**
     * 平台市场添加商品查询商品列表
     *
     * @param req 查询条件
     * @return 商品列表
     */
    @PostMapping("/platformQueryMarketNotAddGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> platformQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.platformQueryMarketNotAddGoodsList(req));
    }

    /**
     * 渠道商选品查询市场商品
     *
     * <p>鉴权说明: 旧实现带 {@code @RoleLimit(&#123;RoleEnum.CompanyRole.CHANNEL&#125;)}
     * 限定仅渠道商可访问。Base 不迁鉴权注解, 角色校验由入口 (网关 / 鉴权基建) 统一承担。</p>
     *
     * @param req 查询条件
     * @return 商品列表
     */
    @PostMapping("/channelMarketNotSelectedGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> channelMarketNotSelectedGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.channelMarketNotSelectedGoodsList(req));
    }

    /**
     * 渠道商查询选品列表
     *
     * @param req 查询条件
     * @return 选品列表
     */
    @PostMapping("/channelQuerySelectGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> channelQuerySelectGoodsList(@RequestBody MarketGoodsPageQuery req) {
        return PlatformResult.success(goodsRelationDomain.channelQuerySelectGoodsList(req));
    }

    /**
     * 渠道商取消选品
     *
     * @param id 商品关系ID
     * @return 操作结果
     */
    @PostMapping("/channelCancelSelected/{id}")
    @FuncPermission("渠道商取消选品")
    public PlatformResult<Object> channelCancelSelected(@PathVariable Long id) {
        goodsRelationDomain.channelCancelSelected(id);
        return PlatformResult.success();
    }
}
