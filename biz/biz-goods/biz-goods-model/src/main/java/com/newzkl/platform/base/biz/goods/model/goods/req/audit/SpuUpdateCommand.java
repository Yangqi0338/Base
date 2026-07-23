package com.newzkl.platform.base.biz.goods.model.goods.req.audit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author fang
 */
@Data
public class SpuUpdateCommand implements Serializable {
    /**
     * 审批模板
     * (1,"角色申请审批模板"),
     * (2,"保证金缴纳审批模板"),
     * (3,"SPU上传审批模板"),
     * (4,"品牌申请审批模板"),
     * (5,"SPU工单审批模板"),
     */
    @NotNull
    private Long templateType;
    /**
     * 审批流ID
     */
    @NotNull
    private Long flowId;
    /**
     * SKU价格修改信息, key:skuId, value:价格
     */
    private Map<Long, Integer> skuSalePrice;
}