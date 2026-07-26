package com.newzkl.platform.base.biz.goods.domain.video.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;

/**
 * 商品-短视频仓储端口。
 *
 * @author KC
 */
public interface ShortVideoRepository {

    /**
     * 新增短视频 (含 SPU 关联关系写入)。
     *
     * @param req 短视频请求
     * @return 短视频 ID
     */
    Long insert(ShortVideoReq req);

    /**
     * 编辑短视频 (含 SPU 关联关系先删后插)。
     *
     * @param req 短视频请求 (id 必填)
     */
    void edit(ShortVideoReq req);

    /**
     * 删除短视频 (含 SPU 关联关系清理)。
     *
     * @param id 短视频 ID
     */
    void del(Long id);

    /**
     * 短视频详情 (含关联 SPU ID 列表)。
     *
     * @param id 短视频 ID
     * @return 短视频视图对象, 不存在返回 null
     */
    ShortVideoVO detail(Long id);

    /**
     * 短视频分页 (join SPU 取编码/名称/图片)。
     *
     * @param query 短视频查询
     * @return 短视频分页
     */
    Page<ShortVideoVO> page(ShortVideoQuery query);
}
