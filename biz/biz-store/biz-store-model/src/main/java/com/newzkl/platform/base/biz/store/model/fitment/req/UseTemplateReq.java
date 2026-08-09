package com.newzkl.platform.base.biz.store.model.fitment.req;

import lombok.Data;

/**
 * 使用模板请求
 *
 * @author niu
 * @date 2024/4/7 17:32
 */
@Data
public class UseTemplateReq {

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 门店id
     * @ext 不传默认主营店铺
     */
    private Long shopId;
}
