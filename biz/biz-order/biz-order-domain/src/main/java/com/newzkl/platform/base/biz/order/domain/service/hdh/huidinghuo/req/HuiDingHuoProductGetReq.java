package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 根据商品ID或编码获取商品信息请求类（支持批量查询）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoProductGetReq extends HuiDingHuoBaseReq {

    /**
     * 商品ID数组（如["237","294"]），限制300个，与itemCodes二选一（ids优先）
     */
    @Size(max = 300, message = "商品ID数组最多支持300个")
    private List<String> ids;

    /**
     * 商品编码数组，与ids二选一（ids优先）
     */
    @Size(max = 300, message = "商品编码数组最多支持300个")
    private List<String> itemCodes;

    /**
     * 校验：ids和itemCodes至少有一个不为空
     */
    @AssertTrue(message = "ids和itemCodes至少需填写一项")
    public boolean isIdsOrItemCodesNotEmpty() {
        return (ids != null && !ids.isEmpty()) || (itemCodes != null && !itemCodes.isEmpty());
    }
}