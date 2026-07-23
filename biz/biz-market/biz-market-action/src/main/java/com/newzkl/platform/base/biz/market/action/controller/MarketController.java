package com.newzkl.platform.base.biz.market.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.service.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.vo.market.AppBindMarketGoodsVO;
import com.newzkl.platform.base.biz.market.model.vo.market.AppBindMarketVO;
import com.newzkl.platform.base.biz.market.model.vo.market.BindMarketVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketGoodsCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketUserVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 市场服务控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketDomain marketDomain;

    /**
     * APP渠道商获取绑定市场列表。
     *
     * @param query 查询请求
     * @return 绑定市场分页
     */
    @GetMapping("/queryChannelBindMarket")
    public ScmResult<Page<AppBindMarketVO>> queryChannelBindMarket(@ModelAttribute ChannelMarketPageQuery query) {
        if (query.getClientId() == null) {
            query.setClientId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(marketDomain.queryChannelBindMarket(query));
    }

    /**
     * APP渠道商获取绑定市场列表对应的商品。
     *
     * @param query 查询请求
     * @return 绑定市场商品分页
     */
    @GetMapping("/queryChannelBindMarketGoods")
    public ScmResult<Page<AppBindMarketGoodsVO>> queryChannelBindMarketGoods(@ModelAttribute AppBindMarketGoodsPageQuery query) {
        if (query.getUserId() == null) {
            query.setUserId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(marketDomain.queryChannelBindMarketGoods(query));
    }

    /**
     * 查询市场列表。
     *
     * @param query 查询请求
     * @return 市场分页
     */
    @PostMapping("/queryMarketList")
    public ScmResult<Page<MarketVO>> queryMarketList(@RequestBody MarketPageQuery query) {
        if (query.getClientId() == null) {
            query.setClientId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(marketDomain.queryMarketList(query));
    }

    /**
     * 查询市场。
     *
     * @param marketId 市场 ID
     * @return 市场详情
     */
    @PostMapping("/queryMarket/{marketId}")
    public ScmResult<MarketVO> queryMarket(@PathVariable Long marketId) {
        return ScmResult.success(marketDomain.queryMarket(marketId));
    }

    /**
     * 交易师绑定二级市场。
     *
     * @param req 绑定请求
     * @return 绑定 ID
     */
    @PostMapping("/tradersBindTwoMarket")
    public ScmResult<Long> tradersBindTwoMarket(@RequestBody ClientBindMarketReq req) {
        return ScmResult.success(marketDomain.bindMarket(req));
    }

    /**
     * 移动APP渠道商绑定交易市场。
     *
     * @param req 绑定请求
     * @return 绑定 ID
     */
    @PostMapping("/appChannelBindMarket")
    public ScmResult<Long> appChannelBindMarket(@RequestBody ClientBindMarketReq req) {
        return ScmResult.success(marketDomain.appChannelBindMarket(req));
    }

    /**
     * 交易师给渠道商绑定二级市场。
     *
     * @param req 绑定请求
     * @return 绑定 ID
     */
    @PostMapping("/channelBindTradersMarket")
    public ScmResult<Long> channelBindTradersMarket(@RequestBody ClientBindMarketReq req) {
        return ScmResult.success(marketDomain.channelBindTradersMarket(req));
    }

    /**
     * 解除绑定。
     *
     * @param id 绑定 ID
     * @return 成功结果
     */
    @PostMapping("/deBindMarket/{id}")
    public ScmResult<Void> deBindMarket(@PathVariable Long id) {
        marketDomain.deBindMarket(id);
        return ScmResult.success();
    }

    /**
     * 查询用户绑定市场列表。
     *
     * @param req 查询请求
     * @return 绑定市场列表
     */
    @PostMapping("/queryBindMarket")
    public ScmResult<List<BindMarketVO>> queryBindMarket(@RequestBody BindMarketListReq req) {
        if (req.getClientId() == null) {
            req.setClientId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(marketDomain.queryBindMarket(req));
    }

    /**
     * 查询市场绑定用户列表。
     *
     * @param req 查询请求
     * @return 市场用户列表
     */
    @PostMapping("/queryMarketUser")
    public ScmResult<List<MarketUserVO>> queryMarketUser(@RequestBody MarketUserReq req) {
        return ScmResult.success(marketDomain.queryMarketUser(req));
    }

    /**
     * 保存市场分类。
     *
     * @param marketCategory 市场分类
     * @return 成功或失败结果
     */
    @PostMapping("/saveMarketCategory")
    public ScmResult<Object> saveMarketCategory(@RequestBody MarketCategoryVO marketCategory) {
        if (marketDomain.saveMarketCategory(marketCategory)) {
            return ScmResult.success();
        } else {
            return ScmResult.fail();
        }
    }

    /**
     * 查询市场分类列表。
     *
     * @param query 查询请求
     * @return 市场分类分页
     */
    @PostMapping("/queryMarketCategoryList")
    public ScmResult<Page<MarketCategoryVO>> queryMarketCategoryList(@RequestBody MarketCategoryPageQuery query) {
        return ScmResult.success(marketDomain.queryMarketCategoryPage(query));
    }

    /**
     * 删除分类。
     *
     * @param id 分类 ID
     * @return 成功结果
     */
    @PostMapping("/deleteMarketCategory/{id}")
    public ScmResult<Void> deleteMarketCategory(@PathVariable Long id) {
        marketDomain.deleteMarketCategory(id);
        return ScmResult.success();
    }

    /**
     * 查询市场商品分类。
     *
     * @param marketId 市场 ID
     * @return 市场商品分类列表
     */
    @PostMapping("/queryMarketGoodsCategory/{marketId}")
    public ScmResult<List<MarketGoodsCategoryVO>> queryMarketGoodsCategory(@PathVariable Long marketId) {
        return ScmResult.success(marketDomain.queryMarketGoodsCategory(marketId, null));
    }

    /**
     * 查询选品库商品分类。
     *
     * @return 选品库商品分类列表
     */
    @PostMapping("/querySelectorGoodsCategory")
    public ScmResult<List<MarketGoodsCategoryVO>> querySelectorGoodsCategory() {
        return ScmResult.success(marketDomain.queryMarketGoodsCategory(null, SecurityUtils.getAccountId()));
    }
}
