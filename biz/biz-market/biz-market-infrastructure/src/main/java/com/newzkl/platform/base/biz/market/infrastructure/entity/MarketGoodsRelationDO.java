package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.biz.market.model.vo.relation.MarketGoodsInfoVO;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import java.time.LocalDateTime;

/**
 * @author 
 * 市场商品关系
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
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
     * 关系类型：  1：一级市场商品  2：二级市场商品  3：市场选品商品
     */
    private Integer relationType;
    /**
     * 用户id   0：为平台   >0:为客户
     */
    @Index
    private Long userId;
    /**
     * 销量
     */
    private Integer sellNum;
    /**
     * 销售额 (Money, 落库 BIGINT 分)
     */
    private Money sellAmount;
    /**
     * 状态  1：正常  0：删除
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
     *
     * @ext 库中以 JSON 列存放, 走 {@code JacksonTypeHandler} 序列化; 依赖类级 {@code autoResultMap = true} 才生效
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private MarketGoodsInfoVO goodsInfo;
}