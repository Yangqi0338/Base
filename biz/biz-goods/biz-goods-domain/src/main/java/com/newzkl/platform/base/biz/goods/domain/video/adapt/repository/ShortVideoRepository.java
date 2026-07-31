package com.newzkl.platform.base.biz.goods.domain.video.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;

import java.util.List;

/**
 * 商品-短视频仓储端口
 *
 * <p>每方法单一持久化动作 (一次 IO), 跨表写/读组装由 {@code ShortVideoDomain} 编排</p>
 *
 * @author KC
 */
public interface ShortVideoRepository {

    /**
     * 插入短视频主记录, 回填并返回主键
     *
     * @param req 短视频请求
     * @return 短视频 ID
     */
    Long insertShortVideo(ShortVideoReq req);

    /**
     * 按 ID 更新短视频主记录
     *
     * @param req 短视频请求 (id 必填)
     */
    void updateShortVideo(ShortVideoReq req);

    /**
     * 按 ID 删除短视频主记录
     *
     * @param id 短视频 ID
     */
    void deleteShortVideo(Long id);

    /**
     * 批量写入视频-SPU 关联关系 (同表单一持久化动作)
     *
     * @param videoId   视频 ID
     * @param spuIdList 关联 SPU ID 列表
     */
    void saveRelation(Long videoId, List<Long> spuIdList);

    /**
     * 按视频 ID 删除全部关联关系
     *
     * @param videoId 视频 ID
     */
    void deleteRelationByVideo(Long videoId);

    /**
     * 按 ID 查短视频主记录视图, 不含关联 SPU
     *
     * @param id 短视频 ID
     * @return 短视频视图对象, 不存在返回 null
     */
    ShortVideoVO shortVideo(Long id);

    /**
     * 按视频 ID 查关联 SPU ID 列表
     *
     * @param videoId 视频 ID
     * @return 关联 SPU ID 列表
     */
    List<Long> relationSpuIdList(Long videoId);

    /**
     * 短视频分页 (join SPU 取编码/名称/图片)
     *
     * @param query 短视频查询
     * @return 短视频分页
     */
    Page<ShortVideoVO> page(ShortVideoQuery query);
}
