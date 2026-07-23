package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商修改模板
 */
@Data
public class SupplierTemplateUpdateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 样式内容
     */
    private String styleContent;
}