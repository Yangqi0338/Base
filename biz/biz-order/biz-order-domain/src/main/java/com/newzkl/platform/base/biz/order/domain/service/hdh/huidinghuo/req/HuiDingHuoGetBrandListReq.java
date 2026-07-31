package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;

/**
 * 3.10 品牌列表接口请求参数
 */
@Data
public class HuiDingHuoGetBrandListReq extends HuiDingHuoBaseReq {
    /**
     * 页数
     */
    private Integer page;
    
    /**
     * 每页数量
     */
    private Integer limit;
}
