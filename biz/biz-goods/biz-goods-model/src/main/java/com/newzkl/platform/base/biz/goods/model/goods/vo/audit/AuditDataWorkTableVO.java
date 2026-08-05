package com.newzkl.platform.base.biz.goods.model.goods.vo.audit;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

/**
 * 工单审核数据
 * @author fang
 */
@Data
public class AuditDataWorkTableVO {
     /**
     * 商品ID (查询)
     */
     private Long spuId;
     /**
      * 商品名称 (查询)
      */
     private String spuName;
     /**
     * 操作类型  1 修改  2 新增  3 删除
     */
     private Integer operateType;
     /**
     * 操作目标  1 SPU基本信息  2 销售规格  3 属性规格   4 SKU信息 5 spu状态
     */
     private Integer operateTarget;
     /**
     * 原商品信息 SpuVO.class
     */
     private String spuInfoJson;
     /**
     * 商品修改信息 SpuVO.class
     */
     private String spuEditInfoJson;
     /**
      * 审批流ID (查询)
      */
     private Long id;
     /**
      * sku销售价 json (Map格式)
      */
     private String skuSalePriceJson;
    /**
     * 审批状态
     *
     * @see AuditEnum.State
     */
    private String state;
    /**
     * 账号ID
     */
    private Long accountId;
}