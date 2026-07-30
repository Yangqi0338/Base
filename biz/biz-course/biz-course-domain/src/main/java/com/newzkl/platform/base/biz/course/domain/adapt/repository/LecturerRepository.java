package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;

import java.util.List;

/**
 * 讲师仓储端口
 *
 * @author KC
 */
public interface LecturerRepository {

    /**
     * 新增或更新讲师
     *
     * @param req 讲师请求
     * @return 讲师主键
     */
    Long save(LecturerReq req);

    /**
     * 按主键查讲师
     *
     * @param id 讲师主键
     * @return 讲师视图, 不存在返回 null
     */
    LecturerRes detail(Long id);

    /**
     * 分页查讲师
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<LecturerRes> pageList(LecturerQuery query);

    /**
     * 查全部启用讲师
     *
     * @return 讲师列表, 永远非 null
     */
    List<LecturerRes> listAllEnabled();

    /**
     * 按主体账号ID查讲师
     *
     * @param mainAccountId 主体账号ID
     * @return 讲师视图, 不存在返回 null
     */
    LecturerRes getByMainAccountId(Long mainAccountId);

    /**
     * 更新启用状态
     *
     * @param id        讲师主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, Integer isEnabled);

    /**
     * 冗余字段回填讲师分类名称
     *
     * @param id                    讲师主键
     * @param lecturerCategoryName  讲师分类名称
     * @return 是否更新成功
     */
    boolean updateCategoryName(Long id, String lecturerCategoryName);

    /**
     * 判断同一主体账号下讲师是否已存在
     *
     * @param mainAccountId 主体账号ID
     * @param excludeId     排除的主键, 编辑场景传自身 id, 新增传 null
     * @return 是否存在
     */
    boolean existsByMainAccountId(Long mainAccountId, Long excludeId);

    /**
     * 关注数增减
     *
     * @param id    讲师主键
     * @param delta 增量, 取关传负数
     * @return 是否更新成功
     */
    boolean addFollowCount(Long id, int delta);

    /**
     * 逻辑删除讲师
     *
     * @param idList 讲师主键列表
     * @return 是否删除成功
     */
    boolean delete(List<Long> idList);
}
