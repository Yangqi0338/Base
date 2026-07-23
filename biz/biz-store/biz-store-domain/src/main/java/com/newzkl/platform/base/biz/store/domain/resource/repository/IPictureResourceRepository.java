package com.newzkl.platform.base.biz.store.domain.resource.repository;

import com.github.pagehelper.PageInfo;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.biz.store.model.resource.req.QueryPictureResourceReq;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureMenuVO;
import com.newzkl.platform.base.biz.store.model.resource.vo.PictureResourceVO;

/**
 * @author niu
 * @description:
 * @date 2024/4/12 10:51
 */
public interface IPictureResourceRepository {

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
    PageInfo<PictureMenuVO> queryMenu(PageQuery query);

    /**
     * 查询资源
     * @param req
     * @return
     */
    PageInfo<PictureResourceVO> queryResource(QueryPictureResourceReq req);
}
