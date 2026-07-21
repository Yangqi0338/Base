package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserTaskRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserTaskDAO;
import com.newzkl.platform.base.biz.user.infrastructure.po.UserTaskDO;
import com.newzkl.platform.base.biz.user.model.relation.req.UserTaskQuery;
import com.newzkl.platform.base.biz.user.model.relation.res.UserTaskRes;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserTaskVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户任务(user_task)存储实现。
 *
 * <p>迁移说明：源使用 MapStruct UserTaskAssembler(domain2VO)，改用 TransferUtils 直接转换，
 * 避免引入 MapStruct 注解处理链路。</p>
 *
 * @author kc
 */
@Repository
@RequiredArgsConstructor
public class UserTaskRepositoryImpl implements UserTaskRepository {

    private final UserTaskDAO dao;

    @Override
    public UserTaskRes detail(Long id) {
        UserTaskDO userTaskDO = dao.selectById(id);
        return TransferUtils.transfer(userTaskDO, UserTaskRes::new);
    }

    @Override
    public List<UserTaskVO> queryList(UserTaskQuery query) {
        List<UserTaskDO> doList = dao.selectList(dao.getLw(query));
        return TransferUtils.transfers(doList, UserTaskVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(UserTaskRes userTask) {
        UserTaskDO userTaskDO = TransferUtils.transfer(userTask, UserTaskDO::new);
        dao.insert(userTaskDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(UserTaskRes userTask, UserTaskQuery query) {
        LambdaQueryWrapper<UserTaskDO> queryWrapper = dao.getLw(query);
        if (dao.selectCount(queryWrapper) == 0) {
            throw new ScmException(BaseErrorCode.INVALID_UPDATE);
        }
        UserTaskDO userTaskDO = TransferUtils.transfer(userTask, UserTaskDO::new);
        dao.updateById(userTaskDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        dao.deleteById(id);
    }

    @Override
    public UserTaskVO lastTask(UserTaskQuery query) {
        query.setSortField(CollUtil.newArrayList("count"));
        query.setSortMode(CollUtil.newArrayList("desc"));
        UserTaskDO entity = dao.selectOne(dao.getLw(query));
        return TransferUtils.transfer(entity, UserTaskVO::new);
    }
}
