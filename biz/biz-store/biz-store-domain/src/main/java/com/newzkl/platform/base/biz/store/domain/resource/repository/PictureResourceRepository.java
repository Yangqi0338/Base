package com.newzkl.platform.base.biz.store.domain.resource.repository;

import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;

/**
 * @author niu
 * @description:
 * @date 2024/4/12 10:51
 */
public interface PictureResourceRepository {

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
