package com.newzkl.platform.base.biz.content.domain.service;

import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.videocategory.query.VideoCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.videocategory.req.VideoCategoryReq;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.biz.content.model.videocategory.vo.VideoCategoryWeightVO;

import java.util.List;

/**
 * 视频分类领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.IVideoCategoryDomain}</p>
 *
 * @author KC
 */
public interface VideoCategoryDomain {

    /**
     * 分页查询视频分类
     *
     * <p>TODO[fe-contract]: 出参壳由旧 mybatis-plus {@code Page} 改为 {@code ContentPage},
     * 字段名一致, 前端仍建议回归。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    ContentPage<VideoCategoryRes> getCategoryPage(VideoCategoryPageQuery query);

    /**
     * 创建视频分类
     *
     * @param req 视频分类入参
     */
    void createCategory(VideoCategoryReq req);

    /**
     * 修改视频分类
     *
     * @param req 视频分类入参(含主键ID)
     */
    void updateCategory(VideoCategoryReq req);

    /**
     * 删除视频分类
     *
     * @param id 主键ID
     */
    void deleteCategory(Long id);

    /**
     * 增量更新分类下的视频数量
     *
     * @param id  分类主键ID
     * @param num 增量值, 可为负数
     */
    void updateVideoCount(Long id, Integer num);

    /**
     * 获取视频分类详情
     *
     * @param id 主键ID
     * @return 视频分类详情, 不存在返回 null
     */
    VideoCategoryRes getById(Long id);

    /**
     * 按推荐人群查询启用的视频分类列表
     *
     * @param recommendGroups 推荐人群名称集合
     * @return 视频分类列表
     */
    List<VideoCategoryRes> getCategoryList(List<String> recommendGroups);

    /**
     * 查询启用分类的权重占比列表
     *
     * @return 分类权重占比集合
     */
    List<VideoCategoryWeightVO> getCategoryWeightList();
}
