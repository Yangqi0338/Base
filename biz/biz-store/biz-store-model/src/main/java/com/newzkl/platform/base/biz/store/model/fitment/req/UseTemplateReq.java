package com.newzkl.platform.base.biz.store.model.fitment.req;

import lombok.Data;

/**
 * @author niu
 * @description: 使用模板req
 * @date 2024/4/7 17:32
 */
@Data
public class UseTemplateReq {

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 门店id 不传默认主营店铺
     */
    private Long shopId;
}
