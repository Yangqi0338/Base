package com.newzkl.platform.base.biz.goods.action.controller;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SaleAttributeCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SkuBaseCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SpuBaseCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.command.SpuStateCommand;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.utils.WorktableFactory;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuQuery;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 商品-工单控制器
 *
 * <p>商品变更走工单审批: 提交后落审批数据, 由平台审核后再生效。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/worktable")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class WorktableController {

    private final WorktableFactory worktableFactory;
    private final SpuDomain spuDomain;

    /**
     * 提交 SPU 基础信息修改工单
     *
     * @param spuUpdate SPU 基础信息修改命令
     * @return 空结果
     */
    @PostMapping("spuUpdate")
    public PlatformResult<Void> spuUpdate(@RequestBody SpuBaseCommand.Update spuUpdate) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SPU_BASE.getCode())
                .submitWorkTable(Collections.singletonList(spuUpdate.getSpuDTO().getId()),
                        SpuEnum.OperateType.UPDATE.getCode(), null, spuUpdate, null);
        return PlatformResult.success();
    }

    /**
     * 提交 SKU 基础信息修改工单
     *
     * @param skuUpdate SKU 基础信息修改命令
     * @return 空结果
     */
    @PostMapping("skuUpdate")
    public PlatformResult<Void> skuUpdate(@RequestBody SkuBaseCommand.Update skuUpdate) {
        for (SkuDTO skuDTO : skuUpdate.getSkuVOList()) {
            skuDTO.setTempId(SnowflakeIdAble.getSnowflakeId());
        }
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SKU_BASE.getCode())
                .submitWorkTable(Collections.singletonList(skuUpdate.getSpuId()),
                        SpuEnum.OperateType.UPDATE.getCode(), null, skuUpdate, null);
        return PlatformResult.success();
    }

    /**
     * 提交 SPU 状态修改工单
     *
     * @param spuStateCommand SPU 状态修改命令
     * @return 空结果
     */
    @PostMapping("spuStateUpdate")
    public PlatformResult<Void> spuStateUpdate(@RequestBody SpuStateCommand.Update spuStateCommand) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SPU_STATE.getCode())
                .submitWorkTable(spuStateCommand.getSpuId(),
                        SpuEnum.OperateType.UPDATE.getCode(), null, spuStateCommand, null);
        return PlatformResult.success();
    }

    /**
     * 提交规格删除工单
     *
     * @param saleAttribute 规格删除命令
     * @return 空结果
     */
    @PostMapping("saleAttributeDelete")
    public PlatformResult<Void> saleAttributeDelete(@RequestBody SaleAttributeCommand.Delete saleAttribute) {
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .submitWorkTable(Collections.singletonList(saleAttribute.getSpuId()),
                        SpuEnum.OperateType.DELETE.getCode(), null, null, saleAttribute);
        return PlatformResult.success();
    }

    /**
     * 提交规格新增工单
     *
     * <p>提交前先做规格数据校验, 校验通过再落工单。</p>
     *
     * @param saleAttribute 规格新增命令
     * @return 空结果
     */
    @PostMapping("saleAttributeAdd")
    public PlatformResult<Void> saleAttributeAdd(@RequestBody SaleAttributeCommand.Add saleAttribute) {
        SaleAttributeCommand.Check check = new SaleAttributeCommand.Check();
        check.setSpuId(saleAttribute.getSpuId());
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setId(saleAttribute.getSpuId());
        check.setOldSpu(spuDomain.voByQuery(spuQuery));
        for (SkuDTO skuDTO : saleAttribute.getSkuList()) {
            skuDTO.setTempId(SnowflakeIdAble.getSnowflakeId());
        }
        check.setSkuList(saleAttribute.getSkuList());
        check.setAttributeVOList(saleAttribute.getAttributeVOList());
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .check(JSONObject.toJSONString(check));
        worktableFactory.getPolicy(SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode())
                .submitWorkTable(Collections.singletonList(saleAttribute.getSpuId()),
                        SpuEnum.OperateType.ADD.getCode(), saleAttribute, null, null);
        return PlatformResult.success();
    }
}
