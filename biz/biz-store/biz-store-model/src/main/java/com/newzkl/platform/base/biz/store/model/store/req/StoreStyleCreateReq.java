package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.biz.store.model.enums.StoreStyleEnum;
import lombok.Data;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 门店样式领域对象
 */
@Data
public class StoreStyleCreateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 样式名称
     */
    @NotNull(message = "样式名称不能为空")
    @Size(max = 20, message = "样式名称长度不能超过20")
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
     * 页面类型
     * @ext 取值范围: HOME_PAGE=首页, 见 {@link StoreStyleEnum.PageType}
     */
    @NotNull
    private String pageType;

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
     * 预览图
     */
    private String previewImage;

}