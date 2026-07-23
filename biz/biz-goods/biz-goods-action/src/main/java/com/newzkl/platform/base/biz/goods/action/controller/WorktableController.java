package com.newzkl.platform.base.biz.goods.action.controller;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SaleAttributeCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SkuBaseCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SpuBaseCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SpuStateCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.utils.WorktableFactory;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 商品工单控制器。
 *
 * <p>提交 SPU/SKU 基础信息、SPU 状态、销售属性等变更工单, 由工单工厂按操作目标分发处理策略。</p>
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/worktable")
@RequiredArgsConstructor
@Slf4j
public class WorktableController {

    private final WorktableFactory worktableFactory;
    private final GoodsQueryService goodsQueryService;

    /**
     * 修改 SPU 信息。
     *
     * @param spuUpdate SPU 基础信息修改命令
     * @return 成功结果
     */
    @PostMapping("spuUpdate")
    public ScmResult<Void> spuUpdate(@RequestBody SpuBaseCommand.Update spuUpdate) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SPU_BASE.getCode())
                .submitWorkTable(Collections.singletonList(spuUpdate.getSpuDTO().getId()),
                        SpuEnum.OperateType.UPDATE.getCode(), null, spuUpdate, null);
        return ScmResult.success();
    }

    /**
     * 修改 SKU 基本信息。
     *
     * @param skuUpdate SKU 基础信息修改命令
     * @return 成功结果
     */
    @PostMapping("skuUpdate")
    public ScmResult<Void> skuUpdate(@RequestBody SkuBaseCommand.Update skuUpdate) {
        for (SkuDTO sku : skuUpdate.getSkuVOList()) {
            sku.setTempId(SnowflakeIdAble.getSnowflakeId());
        }
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SKU_BASE.getCode())
                .submitWorkTable(Collections.singletonList(skuUpdate.getSpuId()),
                        SpuEnum.OperateType.UPDATE.getCode(), null, skuUpdate, null);
        return ScmResult.success();
    }

    /**
     * 修改 SPU 状态。
     *
     * @param spuStateCommand SPU 状态修改命令
     * @return 成功结果
     */
    @PostMapping("spuStateUpdate")
    public ScmResult<Void> spuStateUpdate(@RequestBody SpuStateCommand.Update spuStateCommand) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SPU_STATE.getCode())
                .submitWorkTable(spuStateCommand.getSpuId(),
                        SpuEnum.OperateType.UPDATE.getCode(), null, spuStateCommand, null);
        return ScmResult.success();
    }

    /**
     * 删除规格。
     *
     * @param saleAttribute 销售属性删除命令
     * @return 成功结果
     */
    @PostMapping("saleAttributeDelete")
    public ScmResult<Void> saleAttributeDelete(@RequestBody SaleAttributeCommand.Delete saleAttribute) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .submitWorkTable(Collections.singletonList(saleAttribute.getSpuId()),
                        SpuEnum.OperateType.DELETE.getCode(), null, null, saleAttribute);
        return ScmResult.success();
    }

    /**
     * 新增规格。
     *
     * @param saleAttribute 销售属性新增命令
     * @return 成功结果
     */
    @PostMapping("saleAttributeAdd")
    public ScmResult<Void> saleAttributeAdd(@RequestBody SaleAttributeCommand.Add saleAttribute) {
        SaleAttributeCommand.Check check = new SaleAttributeCommand.Check();
        check.setSpuId(saleAttribute.getSpuId());
        check.setOldSpu(goodsQueryService.spuVO(saleAttribute.getSpuId()));
        for (SkuDTO sku : saleAttribute.getSkuList()) {
            sku.setTempId(SnowflakeIdAble.getSnowflakeId());
        }
        check.setSkuList(saleAttribute.getSkuList());
        check.setAttributeVOList(saleAttribute.getAttributeVOList());
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .check(JSONObject.toJSONString(check));
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .submitWorkTable(Collections.singletonList(saleAttribute.getSpuId()),
                        SpuEnum.OperateType.ADD.getCode(), saleAttribute, null, null);
        return ScmResult.success();
    }
}
