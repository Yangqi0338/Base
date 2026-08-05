package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1211:08
 */
@Data
@TableName
public class AuditDataWorkTableDO extends AuditBaseDO {

    /**
     * 商品名称 (查询)
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
    private String spuInfoJson;
    /**
     * 商品修改信息
     */
    private String spuEditInfoJson;
    /**
     * sku销售价 json (Map格式)
     */
    private String skuSalePriceJson;
    /**
     * 审批状态
     */
    private AuditEnum.State state;
    /**
     * 申请人ID
     */
    @OldColumnName("accountId")
    private Long creator;

}
