package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 门店样式领域对象
 */
@Data
public class StoreStyleUpdateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 风格编码 */
    @NotNull(message = "样式Id不能为空")
    private String styleCode;

    /**
     * 样式名称
     */
    private String styleName;

    /**
     * 主色
     */
    private String essentialColour;

    /**
     * 辅色
     */
    private String auxiliaryColor;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 类型
     * @ext 取值范围: 1=默认
     */
    private Integer type;

    /**
     * 样式内容
     */
    private String styleContent;

    /**
     * 商品id集合
     * @ext 逗号隔开存储
     */
    private String goodsIdListStr;

    /**
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
     */
    private Integer state;

    /**
     * 预览图
     */
    private String previewImage;

}