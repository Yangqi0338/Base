package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.biz.market.model.vo.relation.MarketGoodsInfoVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * @author 
 * 市场商品关系
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class MarketGoodsRelationDO extends BaseDO {
    /**
     * 商品id
     */
    @Index
    private Long goodsId;
    /**
     * 市场id
     */
    @Index
    private Long marketId;
    /**
     * 关系类型
     * @ext 2：市场商品  3：市场选品商品
     */
    private GoodsRelationEnum.GoodsRelation relationType;
    /**
     * 用户id
     * @ext 0：为平台   >0:为客户
     */
    @Index
    private Long userId;
    /**
     * 销量
     */
    private Integer sellNum;
    /**
     * 销售额
     */
    private Money sellAmount;
    /**
     * 状态
     *
     * @ext 1：正常  0：删除
     */
    private Integer state;
    /**
     * 解绑时间
     */
    private LocalDateTime deBindTime;
    /**
     * 让利比例
     */
    private Integer discountRate;
    /**
     * 商品信息
     */
    @JsonSerializable
    private MarketGoodsInfoVO goodsInfo;
}