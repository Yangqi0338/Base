package com.newzkl.platform.base.biz.store.model.store.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店样式领域对象
 */
@Data
public class SupplierTemplateRes implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 样式名称
     */
    private String styleName;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 预览图
     */
    private String previewImage;

}