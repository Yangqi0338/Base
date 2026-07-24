package com.newzkl.platform.base.biz.market.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.relation.service.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 市场商品服务控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/marketGoods")
@RequiredArgsConstructor
public class MarketGoodsController {

    private final GoodsRelationDomain goodsRelationDomain;

    /**
     * 平台查询市场商品。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/platformQueryMarketGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> platformQueryMarketGoodsList(@RequestBody GoodsListPageQuery req) {
        return ScmResult.success(goodsRelationDomain.platformQueryMarketGoodsList(req));
    }

    /**
     * 移动APP查询市场商品。
     *
     * @param req 查询请求
     * @return 市场商品分页
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/appQueryMarketGoodList")
    public ScmResult<Page<GoodsRelationListVO>> appQueryMarketGoodList(@RequestBody GoodsListPageQuery req) {
        req.setUserId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsRelationDomain.appQueryMarketGoodList(req));
    }

    /**
     * 运营商查询二级市场。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/operatorQueryMarketGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> operatorQueryMarketGoodsList(@RequestBody MarketGoodsPageQuery req) {
        return ScmResult.success(goodsRelationDomain.operatorQueryMarketGoodsList(req));
    }

    /**
     * 渠道商查询选品列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/channelQuerySelectGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> channelQuerySelectGoodsList(@RequestBody MarketGoodsPageQuery req) {
        return ScmResult.success(goodsRelationDomain.channelQuerySelectGoodsList(req));
    }

    /**
     * 查询客户绑定市场商品关系列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/queryClientBindMarketGoodsRelationList")
    public ScmResult<Page<GoodsRelationListVO>> queryClientBindMarketGoodsRelationList(@RequestBody GoodsListPageQuery req) {
        if (req.getUserId() == null) {
            req.setUserId(SecurityUtils.getAccountId());
        }
        req.setBindType(3);
        return ScmResult.success(goodsRelationDomain.queryClientBindMarketGoodsRelationList(req));
    }

    /**
     * 平台市场添加商品查询商品列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/platformQueryMarketNotAddGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> platformQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsRelationDomain.platformQueryMarketNotAddGoodsList(req));
    }

    /**
     * 运营商二级市场添加商品查询商品列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/operateQueryMarketNotAddGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> operateQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsRelationDomain.operateQueryMarketNotAddGoodsList(req));
    }

    /**
     * 渠道商选品查询市场商品。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/channelMarketNotSelectedGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> channelMarketNotSelectedGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsRelationDomain.channelMarketNotSelectedGoodsList(req));
    }

    /**
     * 渠道商铺货查询选品列表。
     *
     * @param query 查询请求
     * @return 市场商品分页
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/channelDistributionSelectedGoodsList")
    public ScmResult<Page<GoodsRelationListVO>> channelDistributionSelectedGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq query) {
        return ScmResult.success(goodsRelationDomain.channelDistributionSelectedGoodsList(query));
    }

    /**
     * 渠道商取消选品。
     *
     * @param id 选品关系 ID
     * @return 成功结果
     */
    @PostMapping("/channelCancelSelected/{id}")
    public ScmResult<Void> channelCancelSelected(@PathVariable Long id) {
        goodsRelationDomain.channelCancelSelected(id);
        return ScmResult.success();
    }

    /**
     * 渠道商批量取消选品。
     *
     * @param idList ID 列表
     * @return 成功结果
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("/channelBatchCancelSelected")
    public ScmResult<Void> channelBatchCancelSelected(@RequestBody IdListCommand idList) {
        idList.getIdList().forEach(goodsRelationDomain::channelCancelSelected);
        return ScmResult.success();
    }
}
