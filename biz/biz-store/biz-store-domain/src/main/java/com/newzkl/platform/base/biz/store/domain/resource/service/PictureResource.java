package com.newzkl.platform.base.biz.store.domain.resource.service;

import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;

/**
 * @author niu
 * @description: 图片资源库
 * @date 2024/4/12 10:35
 */
public interface PictureResource {

    /**
     * 保存菜单
     * @param pictureMenu
     */
    void saveMenu(PictureMenuVO pictureMenu);


    /**
     * 保存资源
     * @param pictureResource
     */
    void savePictureResource(PictureResourceVO pictureResource);
}
