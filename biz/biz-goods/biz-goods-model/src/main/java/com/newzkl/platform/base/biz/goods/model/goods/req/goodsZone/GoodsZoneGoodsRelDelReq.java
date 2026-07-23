package com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 商品分组-商品关联删除请求
 * @author sijiwang
 */
@Data
public class GoodsZoneGoodsRelDelReq {

    /**
     * 分组ID
     */
    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    /**
     * 商品ID列表(全删无需填)
     */
    private List<Long> spuList;
}
