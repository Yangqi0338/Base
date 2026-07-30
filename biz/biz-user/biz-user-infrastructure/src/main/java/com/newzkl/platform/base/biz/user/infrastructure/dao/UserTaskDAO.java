package com.newzkl.platform.base.biz.user.infrastructure.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserTaskDO;
import com.newzkl.platform.base.biz.user.model.enums.EarningsEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.UserTaskQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户任务(user_task) Mapper
 *
 * <p>迁移说明：源 DAO 被 import 但无文件，据用法补建。getLw 使用原生 MP LambdaQueryWrapper。</p>
 *
 * @author kc
 */
@Mapper
public interface UserTaskDAO extends BaseMapper<UserTaskDO> {

    /**
     * 构造用户任务查询包装器
     *
     * @param query 查询条件
     * @return LambdaQueryWrapper
     */
    default LambdaQueryWrapper<UserTaskDO> getLw(UserTaskQuery query) {
        LambdaQueryWrapper<UserTaskDO> lw = new LambdaQueryWrapper<>();
        lw.eq(query.getType() != null, UserTaskDO::getType, EarningsEnum.ConsumeType.getByType(query.getType()));
        lw.eq(query.getSeq() != null, UserTaskDO::getSeq, query.getSeq());
        lw.eq(query.getCount() != null, UserTaskDO::getCount, query.getCount());
        lw.eq(query.getForeignId() != null, UserTaskDO::getForeignId, query.getForeignId());
        lw.eq(query.getStatus() != null, UserTaskDO::getStatus, query.getStatus());
        lw.in(CollUtil.isNotEmpty(query.getIdList()), UserTaskDO::getId, query.getIdList());
        return lw;
    }

}
