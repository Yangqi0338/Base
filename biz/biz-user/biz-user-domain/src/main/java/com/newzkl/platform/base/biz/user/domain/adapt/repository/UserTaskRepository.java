package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.relation.query.UserTaskQuery;
import com.newzkl.platform.base.biz.user.model.relation.res.UserTaskRes;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserTaskDTO;

import java.util.List;

/**
 * 用户任务(user_task)存储接口
 *
 * @author kc
 */
public interface UserTaskRepository {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    UserTaskRes detail(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<UserTaskDTO> queryList(UserTaskQuery query);

    /**
     * 新增数据
     *
     * @param userTask 新增实体
     */
    void insert(UserTaskRes userTask);

    /**
     * 修改数据
     *
     * @param userTask 编辑实体
     * @param query    编辑查询
     */
    void edit(UserTaskRes userTask, UserTaskQuery query);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

    /**
     * 最后的任务
     *
     * @param query 查询条件
     * @return 最后一条任务
     */
    UserTaskDTO lastTask(UserTaskQuery query);

}
