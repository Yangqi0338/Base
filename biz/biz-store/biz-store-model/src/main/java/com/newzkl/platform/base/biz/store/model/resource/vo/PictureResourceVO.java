package com.newzkl.platform.base.biz.store.model.resource.vo;

import lombok.Data;

/**
 * @author niu
 * @description: 图片资源vo
 * @date 2024/4/12 10:38
 */
@Data
public class PictureResourceVO {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 资源地址
     */
    private String resourceLink;

}
