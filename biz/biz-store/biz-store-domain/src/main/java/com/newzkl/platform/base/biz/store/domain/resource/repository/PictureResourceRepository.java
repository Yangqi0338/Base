package com.newzkl.platform.base.biz.store.domain.resource.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.biz.store.model.resource.req.QueryPictureResourceReq;
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


    /**
     * 查询目录
     * @param query
     * @return
     */
    Page<PictureMenuVO> queryMenu(PageQuery query);

    /**
     * 查询资源
     * @param req
     * @return
     */
    Page<PictureResourceVO> queryResource(QueryPictureResourceReq req);
}
