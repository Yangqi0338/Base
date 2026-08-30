package com.newzkl.platform.base.biz.content.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.videocategory.entity.VideoCategory;
import com.newzkl.platform.base.biz.content.model.videocategory.query.VideoCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.videocategory.req.VideoCategoryReq;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.biz.content.model.videocategory.vo.VideoCategoryWeightVO;

import java.util.List;

/**
 * 视频分类仓储接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.repository.IVideoCategoryRepository}</p>
 *
 * @author KC
 */
public interface VideoCategoryRepository {

    /**
     * 分页查询视频分类
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<VideoCategoryRes> getCategoryPage(VideoCategoryPageQuery query);

    /**
     * 按主键查询视频分类
     *
     * @param id 主键ID
     * @return 视频分类, 不存在返回 null
     */
    VideoCategory getById(Long id);

    /**
     * 按主键查询视频分类出参
     *
     * @param id 主键ID
     * @return 视频分类出参, 不存在返回 null
     */
    VideoCategoryRes getResById(Long id);

    /**
     * 按主键集合批量查询视频分类
     *
     * @param idList 主键ID集合
     * @return 视频分类集合
     */
    List<VideoCategory> getByIdList(List<Long> idList);

    /**
     * 新增视频分类
     *
     * @param req 视频分类入参
     */
    void save(VideoCategoryReq req);

    /**
     * 按主键修改视频分类
     *
     * @param req 视频分类入参(含主键ID)
     */
    void update(VideoCategoryReq req);

    /**
     * 按主键删除视频分类
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 增量更新分类下的视频数量
     *
     * @param id  分类主键ID
     * @param num 增量值, 可为负数
     */
    void updateVideoCount(Long id, Integer num);

    /**
     * 按推荐人群查询启用的视频分类列表
     *
     * @param recommendGroups 推荐人群名称集合, 为空则不限
     * @return 视频分类出参集合
     */
    List<VideoCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups);

    /**
     * 查询启用分类的权重占比列表
     *
     * <p>把各分类权重折算为合计 10 的整数占比, 供视频推荐按比例取数。</p>
     *
     * @return 分类权重占比集合
     */
    List<VideoCategoryWeightVO> getCategoryWeightList();
}
