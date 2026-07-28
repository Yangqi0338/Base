package com.newzkl.platform.base.biz.market.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.relation.service.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.query.relation.MarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
    public PlatformResult<Page<GoodsRelationListVO>> platformQueryMarketGoodsList(@RequestBody GoodsListPageQuery req) {
        return PlatformResult.success(goodsRelationDomain.platformQueryMarketGoodsList(req));
    }

    /**
     * 运营商查询二级市场。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/operatorQueryMarketGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> operatorQueryMarketGoodsList(@RequestBody MarketGoodsPageQuery req) {
        return PlatformResult.success(goodsRelationDomain.operatorQueryMarketGoodsList(req));
    }

    /**
     * 查询客户绑定市场商品关系列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
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
     * 平台市场添加商品查询商品列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/platformQueryMarketNotAddGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> platformQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.platformQueryMarketNotAddGoodsList(req));
    }

    /**
     * 运营商二级市场添加商品查询商品列表。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/operateQueryMarketNotAddGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> operateQueryMarketNotAddGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.operateQueryMarketNotAddGoodsList(req));
    }

    /**
     * 渠道商选品查询市场商品。
     *
     * @param req 查询请求
     * @return 市场商品分页
     */
    @PostMapping("/channelMarketNotSelectedGoodsList")
    public PlatformResult<Page<GoodsRelationListVO>> channelMarketNotSelectedGoodsList(@RequestBody PlatformQueryMarketNotAddGoodsReq req) {
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsRelationDomain.channelMarketNotSelectedGoodsList(req));
    }

    /**
     * 渠道商取消选品。
     *
     * @param id 选品关系 ID
     * @return 成功结果
     */
    @PostMapping("/channelCancelSelected/{id}")
    public PlatformResult<Boolean> channelCancelSelected(@PathVariable Long id) {
        goodsRelationDomain.channelCancelSelected(id);
        return PlatformResult.success();
    }
}
