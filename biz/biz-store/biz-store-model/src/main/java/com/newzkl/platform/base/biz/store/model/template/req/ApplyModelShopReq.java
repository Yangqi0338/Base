package com.newzkl.platform.base.biz.store.model.template.req;

import lombok.Data;

/**
 * 申请样板店请求
 *
 * @author niu
 * @date 2024/4/7 16:14
 */
@Data
public class ApplyModelShopReq {

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 样板店描述
     */
    private String modelDescription;

}
