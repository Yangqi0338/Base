package com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 商品-套餐请求对象
 *
 * <p>本套餐为商品域「商品套餐/组包」配置 (席位数 + 套餐价), 与甄选师「入会礼包」无关。</p>
 *
 * @author KC
 */
@Data
public class GoodPackageReq {

    /**
     * 主键 ID (更新时必填)
     */
    @NotNull(groups = UpdateCommand.class, message = "ID不能为空")
    private Long id;

    /**
     * 套餐业务编码 (唯一)
     */
    @NotBlank(message = "套餐ID不能为空")
    private String packageId;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套餐名称不能为空")
    private String packageName;

    /**
     * 商品席位数
     */
    @NotNull(message = "商品席位不能为空")
    private Long goodsNum;

    /**
     * 套餐价格 (Money, 落库 BIGINT 分)
     */
    @NotNull(message = "套餐价格不能为空")
    private Money packagePrice;

    /**
     * 套餐描述
     */
    private String packageDesc;

    /**
     * 状态
     * @ext 1 启用, 0 停用
     */
    private Integer state;
}
