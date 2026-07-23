package com.newzkl.platform.base.biz.store.domain.resource.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.biz.store.model.resource.req.QueryPictureResourceReq;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;
import com.newzkl.platform.base.biz.store.domain.resource.repository.PictureResourceRepository;
import com.newzkl.platform.base.biz.store.domain.resource.service.PictureResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author niu
 * @description:
 * @date 2024/4/12 10:51
 */
@Service
public class PictureResourceImpl implements PictureResource {

    private final PictureResourceRepository pictureResourceRepository;

    @Autowired
    public PictureResourceImpl(PictureResourceRepository pictureResourceRepository) {
        this.pictureResourceRepository = pictureResourceRepository;
    }

    @Override
    public void saveMenu(PictureMenuVO pictureMenu) {
        pictureResourceRepository.saveMenu(pictureMenu);
    }

    @Override
    public void savePictureResource(PictureResourceVO pictureResource) {
        pictureResourceRepository.savePictureResource(pictureResource);
    }

    @Override
    public Page<PictureMenuVO> queryMenu(PageQuery query) {
        return pictureResourceRepository.queryMenu(query);
    }

    @Override
    public Page<PictureResourceVO> queryResource(QueryPictureResourceReq req) {
        return pictureResourceRepository.queryResource(req);
    }
}
