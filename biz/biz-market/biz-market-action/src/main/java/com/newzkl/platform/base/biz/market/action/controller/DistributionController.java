package com.newzkl.platform.base.biz.market.action.controller;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.distribution.service.DistributionDomain;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionRandomPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.distribution.DistributionsQuery;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.GoodsStateAlterReq;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsDetailRes;
import com.newzkl.platform.base.biz.market.model.res.distribution.DistributionGoodsListRes;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionGoodsListOPVO;
import com.newzkl.platform.base.biz.market.model.vo.market.DistributionRandomVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 铺货相关控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/distribution")
@RequiredArgsConstructor
public class DistributionController {

    private final DistributionDomain distributionDomain;

    /**
     * 查询铺货列表-渠道商。
     *
     * @param req 查询请求
     * @return 铺货分页
     */
    @PostMapping("/queryDistributions")
    public PlatformResult<Page<DistributionGoodsListRes>> queryDistributions(@RequestBody DistributionsQuery req) {
        if (req.getChannelId() == null) {
            req.setChannelId(SecurityUtils.getAccountId());
        }
        if (req.getStoreId() == null) {
            req.setStoreId(req.getChannelId());
        }
        return PlatformResult.success(distributionDomain.queryDistributionsChannel(req));
    }

    /**
     * 查询铺货列表-总平台。
     *
     * @param query 查询请求
     * @return 铺货分页
     */
    @PostMapping("/queryDistributionsOverallPlatform")
    public PlatformResult<Page<DistributionGoodsListOPVO>> queryDistributionsOverallPlatform(@RequestBody DistributionsPageQuery query) {
        return PlatformResult.success(distributionDomain.queryDistributionsOverallPlatform(query));
    }

    /**
     * 铺货。
     *
     * @param goodsId 商品 ID
     * @return 成功结果
     */
    @PostMapping("/goodsDistribution/{goodsId}")
    public PlatformResult<Boolean> goodsDistribution(@PathVariable Long goodsId) {
        distributionDomain.goodsDistribution(SecurityUtils.getAccountId(), goodsId, false);
        return PlatformResult.success();
    }

    /**
     * 更新商品状态。
     *
     * @param req 状态更新请求
     * @return 成功结果
     */
    @PostMapping("/alterGoodsState")
    public PlatformResult<Boolean> alterGoodsState(@RequestBody GoodsStateAlterReq req) {
        req.setChannelId(SecurityUtils.getAccountId());
        if (req.getStoreId() == null) {
            req.setStoreId(SecurityUtils.getAccountId());
        }
        distributionDomain.alterGoodsState(req);
        return PlatformResult.success();
    }

    /**
     * 批量修改商品。
     *
     * @param req 批量更新请求
     * @return 成功结果
     */
    @PostMapping("/batchUpdateDistributions")
    public PlatformResult<Boolean> batchUpdateDistributions(@RequestBody DistributionsBatchUpdateReq req) {
        req.setChannelId(SecurityUtils.getAccountId());
        distributionDomain.batchUpdateDistributions(req);
        return PlatformResult.success();
    }

    /**
     * 删除商品。
     *
     * @param goodsId 商品 ID
     * @return 成功结果
     */
    @PostMapping("/delGoods/{goodsId}")
    public PlatformResult<Boolean> delGoods(@PathVariable Long goodsId) {
        distributionDomain.delGoods(goodsId);
        return PlatformResult.success();
    }

    /**
     * 批量删除商品。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("/batchDelGoods")
    public PlatformResult<Boolean> batchDelGoods(@RequestBody IdListCommand idListObj) {
        distributionDomain.batchDelGoods(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 铺货列表随机查询。
     *
     * @param query 随机查询请求
     * @return 随机铺货分页
     */
    @PostMapping("/randomSelectedGoodsList")
    public PlatformResult<Page<DistributionRandomVO>> randomSelectedGoodsList(@RequestBody DistributionRandomPageQuery query) {
        return PlatformResult.success(distributionDomain.randomSelectedGoodsPage(query));
    }

    /**
     * 获取铺货商品分类。
     *
     * @param storeId 门店 ID, 为空时取当前账号
     * @return 铺货分类列表
     */
    @GetMapping("/getDistributionGoodsCategory")
    public PlatformResult<List<DistributionCategoryVO>> getDistributionGoodsCategory(@RequestParam(value = "storeId", required = false) Long storeId) {
        return PlatformResult.success(distributionDomain.getDistributionGoodsCategory(Opt.ofNullable(storeId).orElse(SecurityUtils.getAccountId())));
    }

    /**
     * 推荐商品。
     *
     * @param id 铺货 ID
     * @return 成功结果
     */
    @GetMapping("/recommendationGoods")
    public PlatformResult<Void> recommendationGoods(@RequestParam("id") Long id) {
        distributionDomain.recommendationGoods(id);
        return PlatformResult.success();
    }

    /**
     * 取消推荐商品。
     *
     * @param id 铺货 ID
     * @return 成功结果
     */
    @GetMapping("/cancelRecommendationGoods")
    public PlatformResult<Void> cancelRecommendationGoods(@RequestParam("id") Long id) {
        distributionDomain.cancelRecommendationGoods(id);
        return PlatformResult.success();
    }

    /**
     * 平台门店上架。
     *
     * @param id 铺货 ID
     * @return 成功结果
     */
    @GetMapping("/platformStoreListed")
    public PlatformResult<Void> platformStoreListed(@RequestParam("id") Long id) {
        distributionDomain.platformStoreListed(id);
        return PlatformResult.success();
    }

    /**
     * 平台门店下架。
     *
     * @param id 铺货 ID
     * @return 成功结果
     */
    @GetMapping("/platformStoreUnlisted")
    public PlatformResult<Void> platformStoreUnlisted(@RequestParam("id") Long id) {
        distributionDomain.platformStoreUnlisted(id);
        return PlatformResult.success();
    }

    /**
     * 查询推荐铺货列表。
     *
     * @return 推荐铺货列表
     */
    @GetMapping("/queryRecommendationDistributions")
    public PlatformResult<List<DistributionGoodsDetailRes>> queryRecommendationDistributions() {
        return PlatformResult.success(distributionDomain.queryRecommendationDistributions());
    }

    /**
     * 查询推荐门店列表。
     *
     * @return 推荐门店列表
     */
    @GetMapping("/queryRecommendationStores")
    public PlatformResult<List<DistributionGoodsDetailRes>> queryRecommendationStores() {
        return PlatformResult.success(distributionDomain.queryRecommendationStores());
    }
}
