package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.action.cmd.SpuCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.OutSpuEditCommand;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.StockExecuteReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SupplierSpuStatisticsVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SupplierSpuStatisticsQuery;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品-商品库控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/spu")
@RequiredArgsConstructor
public class SpuController {

    private final SpuService spuService;
    private final SpuDomain spuDomain;
    private final GoodsQueryService goodsQueryService;

    /**
     * 外部商品修改。
     *
     * @param outSpuEditCommand 外部商品修改命令
     * @return 成功结果
     */
    @PostMapping("spuSalePriceEdit")
    public ScmResult<Void> outSpuEdit(@Validated @RequestBody OutSpuEditCommand outSpuEditCommand) {
        spuDomain.outSpuEdit(outSpuEditCommand);
        return ScmResult.success();
    }

    /**
     * 商品提交审核。
     *
     * @param idObj 商品 ID 命令
     * @return 提交结果 ID
     */
    @PostMapping("spuSubmit")
    public ScmResult<Long> spuSubmit(@Validated @RequestBody SpuCmd.Id idObj) {
        Long accountId = SecurityUtils.getAccountId();
        return ScmResult.success(spuService.supplierSpuSubmit(accountId, idObj.getId()));
    }

    /**
     * 商品上下架。
     *
     * @param upCommand 上下架命令
     * @return 成功结果
     */
    @PostMapping("spuUp")
    public ScmResult<Void> spuUp(@Validated @RequestBody SpuCmd.UpCommand upCommand) {
        spuService.spuUp(upCommand.getEnable(), upCommand.getSpuIdList());
        return ScmResult.success();
    }

    /**
     * 删除商品。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("spuDelete")
    public ScmResult<Void> spuDelete(@RequestBody IdListCommand idListObj) {
        spuDomain.spuDelete(idListObj.getIdList());
        return ScmResult.success();
    }

    /**
     * 商品详情。
     *
     * @param id            商品 ID
     * @param needExtraInfo 是否需要额外信息
     * @return 商品 VO
     */
    @GetMapping("spu")
    public ScmResult<SpuVO> spu(@RequestParam("id") Long id,
                                @RequestParam(value = "needExtraInfo", required = false, defaultValue = "false") Boolean needExtraInfo) {
        return ScmResult.success(goodsQueryService.spuVO(id, needExtraInfo));
    }

    /**
     * 货盘选品。
     *
     * @param spuVO 货盘商品视图
     * @return 商品 ID
     */
    @PostMapping("/palletSelectGoods")
    public ScmResult<Long> palletSelectGoods(@RequestBody SpuVO spuVO) {
        return ScmResult.success(spuService.palletSelectGoods(spuVO));
    }

    /**
     * 移动 APP 供应商商品统计。
     *
     * @param query 统计查询
     * @return 供应商商品统计
     */
    @GetMapping("supplierSpuStatistics")
    public ScmResult<SupplierSpuStatisticsVO> supplierSpuStatistics(@ModelAttribute SupplierSpuStatisticsQuery query) {
        return ScmResult.success(spuService.supplierSpuStatistics(query));
    }

    /**
     * SKU 列表。
     *
     * @param skuQuery SKU 查询
     * @return SKU 列表
     */
    @PostMapping("skuList")
    public ScmResult<List<SkuVO>> skuList(@RequestBody SkuQuery skuQuery) {
        return ScmResult.success(spuDomain.skuVOList(skuQuery));
    }

    /**
     * SKU 库存操作。
     *
     * @param stockExecuteReq 库存操作请求列表
     * @return 成功结果
     */
    @PostMapping("stockExecute")
    public ScmResult<Void> stockExecute(@RequestBody List<StockExecuteReq> stockExecuteReq) {
        spuDomain.stockExecute(stockExecuteReq);
        return ScmResult.success();
    }
}
