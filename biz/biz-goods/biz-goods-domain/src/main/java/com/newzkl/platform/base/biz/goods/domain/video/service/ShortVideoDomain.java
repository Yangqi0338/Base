package com.newzkl.platform.base.biz.goods.domain.video.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;

/**
 * 商品-短视频领域服务
 *
 * @author KC
 */
public interface ShortVideoDomain {

    /**
     * 新增短视频
     *
     * @param req 短视频请求
     * @return 短视频 ID
     */
    Long add(ShortVideoReq req);

    /**
     * 编辑短视频
     *
     * @param req 短视频请求 (id 必填)
     */
    void edit(ShortVideoReq req);

    /**
     * 删除短视频
     *
     * @param id 短视频 ID
     */
    void del(Long id);

    /**
     * 短视频详情
     *
     * @param id 短视频 ID
     * @return 短视频视图对象
     */
    ShortVideoVO detail(Long id);

    /**
     * 短视频分页
     *
     * @param query 短视频查询
     * @return 短视频分页
     */
    Page<ShortVideoVO> page(ShortVideoQuery query);
}
