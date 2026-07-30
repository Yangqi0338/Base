package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;
import com.newzkl.platform.base.biz.store.domain.resource.repository.PictureResourceRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ResourcePictureDAO;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ResourcePictureMenuDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureDO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ResourcePictureMenuDO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description:
 * @date 2024/4/12 11:34
 */
@Repository
@RequiredArgsConstructor
public class PictureResourceRepositoryImpl implements PictureResourceRepository {

    private final ResourcePictureDAO resourcePictureDAO;

    private final ResourcePictureMenuDAO resourcePictureMenuDAO;

    @Override
    public void saveMenu(PictureMenuVO pictureMenu) {
        ResourcePictureMenuDO resourcePictureMenu = new ResourcePictureMenuDO();
        resourcePictureMenu.setMenuName(pictureMenu.getMenuName());
        if (pictureMenu.getId() != null) {
            resourcePictureMenu.setId(pictureMenu.getId());
            resourcePictureMenuDAO.updateById(resourcePictureMenu);
        } else {
            resourcePictureMenu.setId(SnowflakeIdAble.getSnowflakeId());
            resourcePictureMenu.setAccountId(SecurityUtils.getAccountId());
            resourcePictureMenu.setCreateTime(LocalDateTime.now());
            resourcePictureMenuDAO.insert(resourcePictureMenu);
        }
    }

    @Override
    public void savePictureResource(PictureResourceVO pictureResource) {
        ResourcePictureDO resourcePicture = new ResourcePictureDO();
        resourcePicture.setPictureName(pictureResource.getPictureName());
        resourcePicture.setResourceLink(pictureResource.getResourceLink());
        resourcePicture.setMenuId(pictureResource.getMenuId());
        if (resourcePicture.getId() != null) {
            resourcePicture.setId(resourcePicture.getId());
            resourcePictureDAO.updateById(resourcePicture);
        } else {
            resourcePicture.setId(SnowflakeIdAble.getSnowflakeId());
            resourcePicture.setAccountId(SecurityUtils.getAccountId());
            resourcePicture.setCreateTime(LocalDateTime.now());
            resourcePictureDAO.insert(resourcePicture);
        }
    }
}
