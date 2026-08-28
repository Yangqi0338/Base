package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1211:08
 */
@Data
@TableName
public class AuditDataWorkTableDO extends AuditBaseDO {

    /**
     * 商品名称
     * @ext 查询
     */
    private String spuName;
    /**
     * 操作类型
     */
    private SpuEnum.OperateType operateType;
    /**
     * 操作目标
     */
    @Index
    private SpuEnum.OperateTarget operateTarget;
    /**
     * 原商品信息
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String spuInfoJson;
    /**
     * 商品修改信息
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String spuEditInfoJson;
    /**
     * sku销售价
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String skuSalePriceJson;
    /**
     * 审批状态
     */
    private AuditEnum.State state;

}
