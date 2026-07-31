package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import java.util.List;

/**
 * 3.11 根据品牌ID获取品牌接口请求参数
 */
@Data
public class HuiDingHuoGetBrandsReq extends HuiDingHuoBaseReq {
    /**
     * 品牌ID数组（限制300个）
     */
    private List<String> ids;
}
