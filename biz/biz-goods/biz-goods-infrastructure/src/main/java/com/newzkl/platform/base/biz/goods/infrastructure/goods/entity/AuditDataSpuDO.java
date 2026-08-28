package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1211:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AuditDataSpuDO extends AuditBaseDO {
    /**
     * 商品URL
     */
    private String img;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品分类
     */
    @Index
    private Long categoryId;
    /**
     * 商品分类名称完整
     */
    private String categoryName;
    /**
     * 商品创建信息
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String spuCreateInfoJson;
    /**
     * sku销售价
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String skuSalePriceJson;
    /**
     * sku审批人值对象
     */
    private String adminUserName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 供货价
     */
    private Money supplyPrice;
    /**
     * 市场价
     */
    private Money marketPrice;
    /**
     * 建议零售价
     */
    private Money unitPrice;
    /**
     * 审批状态
     */
    private AuditEnum.State state;

}
