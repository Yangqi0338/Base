package com.newzkl.platform.base.biz.market.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.service.MarketDomain;
import com.newzkl.platform.base.biz.market.domain.relation.service.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.enums.GoodsRelationEnum;
import com.newzkl.platform.base.biz.market.model.enums.MarketEnum;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.SaveGoodsRelationReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
public class MarketGoodsController {

    private final GoodsRelationDomain goodsRelationDomain;

    private final MarketDomain marketDomain;

    /**
     * 一级市场添加商品
     *
     * <p>沿用旧语义: 未加入一级市场的商品统一按二级市场商品 ({@code relationType=2}) 落库,
     * {@code userId} 固定为 0。</p>
     *
     * @param req 保存商品关系请求
     * @return 操作结果
     */
    @PostMapping("/saveOneMarketGoodsRelation")
    public PlatformResult<Object> saveOneMarketGoodsRelation(@RequestBody SaveGoodsRelationReq req) {
        req.setUserId(0L);
        req.setRelationType(2);
        goodsRelationDomain.saveGoodsRelation(req);
        marketDomain.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(req.getMarketId(), MarketEnum.NumType.GOODS_NUM, req.getGoodsIds().size()));
        return PlatformResult.success();
    }

    /**
     * 二级市场添加商品
     *
     * @param req 保存商品关系请求
     * @return 操作结果
     */
    @PostMapping("/saveTwoMarketGoodsRelation")
    public PlatformResult<Object> saveTwoMarketGoodsRelation(@RequestBody SaveGoodsRelationReq req) {
        req.setUserId(0L);
        req.setRelationType(2);
        goodsRelationDomain.saveGoodsRelation(req);
        marketDomain.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(req.getMarketId(), MarketEnum.NumType.GOODS_NUM, req.getGoodsIds().size()));
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
    public PlatformResult<Object> saveMarketGoodsSelectRelation(@RequestBody SaveGoodsRelationReq req) {
        req.setUserId(SecurityUtils.getAccountId());
        req.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getRelationType());
        assertGoodsNotSelected(req);
        goodsRelationDomain.saveGoodsRelation(req);
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
     * 运营商查询二级市场
     *
     * @param req 查询条件
     * @return 市场商品列表
     */
    @PostMapping("/operatorQueryMarketGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> operatorQueryMarketGoodsList(@RequestBody MarketGoodsPageQuery req) {
        return PlatformResult.success(goodsRelationDomain.operatorQueryMarketGoodsList(req));
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
        req.setBindType(3);
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
     * 运营商二级市场添加商品查询商品列表
     *
     * @param req 查询条件
     * @return 商品列表
     */
    @PostMapping("/operateQueryMarketNotAddGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> operateQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.operateQueryMarketNotAddGoodsList(req));
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
    public PlatformResult<Object> channelCancelSelected(@PathVariable Long id) {
        goodsRelationDomain.channelCancelSelected(id);
        return PlatformResult.success();
    }

    /**
     * 校验请求内商品尚未被当前渠道商选品
     *
     * <p>沿用旧 application 层语义: 只要有一件商品已在选品库即整单拒绝, 抛
     * {@code EXIST_DATA}。领域层 {@code saveGoodsRelation} 自身对已存在关系是静默跳过,
     * 不抛错, 故显式前置校验以保留旧接口的失败反馈。</p>
     *
     * @param req 保存商品关系请求
     */
    private void assertGoodsNotSelected(SaveGoodsRelationReq req) {
        GoodsRelationQueryDTO query = new GoodsRelationQueryDTO();
        query.setGoodsIdList(req.getGoodsIds());
        query.setUserId(req.getUserId());
        query.setRelationType(req.getRelationType());
        if (CollUtil.isNotEmpty(goodsRelationDomain.queryGoodsRelationListByDTO(query))) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "选品");
        }
    }
}
