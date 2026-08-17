package com.newzkl.platform.base.biz.market.action.controller;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.distribution.DistributionDomain;
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
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 铺货相关控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.distribution.DistributionController},
 * 类级与方法级路径、HTTP verb 逐字沿用。</p>
 *
 * <p>未迁端点 (依赖跨域 RPC 或 Base 缺失端口):</p>
 * <ul>
 *   <li>{@code GET /getDistributionGoodsDetail}、{@code GET /getDistributionGoodsDetailByStoreId}
 *       (后者与前者在旧侧是同名重载, 差别为门店 ID 取当前账号还是入参):
 *       旧实现须经 goods 域 {@code SpuFacade}(spu/sku 详情)、{@code ShortVideoFacade}(商品视频)
 *       与 account 域 {@code AccountFacade}(门店账号) 三个 Dubbo Facade 组装
 *       {@code DistributionGoodsDetailRes}; Base 侧 market 域尚无对应出站端口。
 *       响应主体字段 (商品名 / 图 / 属性 / sku / 视频) 全部来自这三个域, 仅凭 market
 *       自有铺货表只能拼出空壳, 补端口须为 biz-market-infrastructure 增加
 *       {@code biz-goods-facade} 与 {@code biz-account-facade} 依赖 (pom 改动), 故记阻塞不补。</li>
 *   <li>{@code POST /recoverGoods/&#123;goodsId&#125;}: {@code DistributionDomain} 与
 *       {@code DistributionRepository} 均无 {@code recoverGoods} 端口,
 *       {@code StoreDistributionDAO} 亦无对应语句。</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/distribution")
@RequiredArgsConstructor
@Slf4j
public class DistributionController {

    private final DistributionDomain distributionDomain;

    /**
     * 查询铺货列表-渠道商
     *
     * @param req 查询条件
     * @return 铺货列表
     */
    @PostMapping("/queryDistributions")
    public PlatformResult<Page<DistributionGoodsListRes>> queryDistributions(@RequestBody DistributionsQuery req) {
        if (SecurityUtils.getRole() == RoleEnum.CompanyRole.MEMBER) {
            req.setChannelId(SecurityUtils.getUpId());
        } else {
            if (req.getChannelId() == null) {
                req.setChannelId(SecurityUtils.getAccountId());
            }
            if (req.getChannelId() != null) {
                req.setStoreId(req.getChannelId());
            }
        }
        if (req.getStoreId() == null) {
            req.setStoreId(req.getChannelId());
        }
        return PlatformResult.success(distributionDomain.queryDistributionsChannel(req));
    }

    /**
     * 查询铺货列表-总平台
     *
     * @param query 查询条件
     * @return 铺货列表
     */
    @PostMapping("/queryDistributionsOverallPlatform")
    public PlatformResult<Page<DistributionGoodsListOPVO>> queryDistributionsOverallPlatform(@RequestBody DistributionsPageQuery query) {
        return PlatformResult.success(distributionDomain.queryDistributionsOverallPlatform(query));
    }

    /**
     * 铺货
     *
     * <p>行为差异: 旧 application 层在铺货成功后取返回的 {@code categoryId} 调
     * {@code CategoryDomain#syncCategory} 同步商户分类; 新 {@code DistributionDomain#goodsDistribution}
     * 返回 {@code void}, 无 categoryId 可用, 故分类同步这一步未接线。</p>
     *
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @PostMapping("/goodsDistribution/{goodsId}")
    public PlatformResult<Object> goodsDistribution(@PathVariable Long goodsId) {
        distributionDomain.goodsDistribution(SecurityUtils.getAccountId(), goodsId, false);
        return PlatformResult.success();
    }

    /**
     * 更新商品状态
     *
     * @param req 状态更新请求
     * @return 操作结果
     */
    @PostMapping("/alterGoodsState")
    public PlatformResult<Object> alterGoodsState(@RequestBody GoodsStateAlterReq req) {
        req.setChannelId(SecurityUtils.getAccountId());
        if (req.getStoreId() == null) {
            req.setStoreId(SecurityUtils.getAccountId());
        }
        distributionDomain.alterGoodsState(req);
        return PlatformResult.success();
    }

    /**
     * 批量修改商品
     *
     * @param req 批量修改请求
     * @return 操作结果
     */
    @PostMapping("/batchUpdateDistributions")
    public PlatformResult<Void> batchUpdateDistributions(@RequestBody DistributionsBatchUpdateReq req) {
        req.setChannelId(SecurityUtils.getAccountId());
        distributionDomain.batchUpdateDistributions(req);
        return PlatformResult.success();
    }

    /**
     * 删除商品
     *
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @PostMapping("/delGoods/{goodsId}")
    public PlatformResult<Void> delGoods(@PathVariable Long goodsId) {
        distributionDomain.delGoods(goodsId);
        return PlatformResult.success();
    }

    /**
     * 批量删除商品
     *
     * @param idListCommand 商品ID列表
     * @return 操作结果
     */
    @PostMapping("/batchDelGoods")
    public PlatformResult<Void> batchDelGoods(@RequestBody IdCommand idListCommand) {
        distributionDomain.batchDelGoods(idListCommand.getIdList());
        return PlatformResult.success();
    }

    /**
     * 铺货列表随机查询
     *
     * @param query 查询条件
     * @return 随机铺货列表
     */
    @PostMapping("/randomSelectedGoodsList")
    public PlatformResult<Page<DistributionRandomVO>> randomSelectedGoodsList(@RequestBody DistributionRandomPageQuery query) {
        return PlatformResult.success(distributionDomain.randomSelectedGoodsPage(query));
    }

    /**
     * 获取铺货商品分类
     *
     * @param storeId 门店ID, 不传取当前账号
     * @return 分类列表
     */
    @GetMapping("/getDistributionGoodsCategory")
    public PlatformResult<List<DistributionCategoryVO>> getDistributionGoodsCategory(@RequestParam(value = "storeId", required = false) Long storeId) {
        return PlatformResult.success(distributionDomain.getDistributionGoodsCategory(Opt.ofNullable(storeId).orElse(SecurityUtils.getAccountId())));
    }

    /**
     * 推荐商品
     *
     * @param id 铺货ID
     * @return 操作结果
     */
    @GetMapping("/recommendationGoods")
    public PlatformResult<Void> recommendationGoods(@RequestParam("id") Long id) {
        distributionDomain.recommendationGoods(id);
        return PlatformResult.success();
    }

    /**
     * 取消推荐商品
     *
     * @param id 铺货ID
     * @return 操作结果
     */
    @GetMapping("/cancelRecommendationGoods")
    public PlatformResult<Void> cancelRecommendationGoods(@RequestParam("id") Long id) {
        distributionDomain.cancelRecommendationGoods(id);
        return PlatformResult.success();
    }

    /**
     * 平台门店上架
     *
     * @param id 铺货ID
     * @return 操作结果
     */
    @GetMapping("/platformStoreListed")
    public PlatformResult<Void> platformStoreListed(@RequestParam("id") Long id) {
        distributionDomain.platformStoreListed(id);
        return PlatformResult.success();
    }

    /**
     * 平台门店下架
     *
     * @param id 铺货ID
     * @return 操作结果
     */
    @GetMapping("/platformStoreUnlisted")
    public PlatformResult<Void> platformStoreUnlisted(@RequestParam("id") Long id) {
        distributionDomain.platformStoreUnlisted(id);
        return PlatformResult.success();
    }

    /**
     * 查询推荐铺货列表
     *
     * @return 推荐铺货列表
     */
    @GetMapping("/queryRecommendationDistributions")
    public PlatformResult<List<DistributionGoodsDetailRes>> queryRecommendationDistributions() {
        return PlatformResult.success(distributionDomain.queryRecommendationDistributions());
    }

    /**
     * 查询推荐门店列表 (每个门店销量最好的商品, 按销量排序返回10个)
     *
     * @return 推荐门店列表
     */
    @GetMapping("/queryRecommendationStores")
    public PlatformResult<List<DistributionGoodsDetailRes>> queryRecommendationStores() {
        return PlatformResult.success(distributionDomain.queryRecommendationStores());
    }
}
