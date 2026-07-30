package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.RoleDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.po.RoleDO;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.vo.RoleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.RoleRepositoryImpl}。
 * 旧 mapper xml 的 {@code insert} / {@code updateByPrimaryKeySelective} / {@code deleteByQuery} /
 * {@code listByQuery} / {@code addCount} 全部改由 MyBatis-Plus 通用方法与 wrapper 表达,
 * 本仓不写 mapper xml。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleDAO roleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(RoleVO role) {
        RoleDO roleDO = TransferUtils.transfer(role, RoleDO::new);
        // 角色 ID 由领域层雪花生成后透传, 不能走 preInsert 清 id
        roleDAO.insert(roleDO);
        return roleDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(RoleVO role) {
        return roleDAO.updateById(TransferUtils.transfer(role, RoleDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        return roleDAO.deleteByIds(idList);
    }

    @Override
    public RoleVO detail(Long id) {
        return TransferUtils.transfer(roleDAO.selectById(id), RoleVO::new);
    }

    @Override
    public List<RoleVO> list(RoleQuery query) {
        List<RoleDO> roleList = roleDAO.selectList(roleDAO.getLw(query));
        List<RoleVO> result = TransferUtils.transfers(roleList, RoleVO::new);
        return result == null ? new ArrayList<>() : result;
    }

    @Override
    public Page<RoleVO> pageList(RoleQuery query) {
        Page<RoleDO> pageList = roleDAO.selectPage(RepositorySupport.page(query), roleDAO.getLw(query));
        return TransferUtils.transferPage(pageList, RoleVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addCount(Long id) {
        LambdaUpdateWrapper<RoleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("total_user_num = total_user_num + 1")
                .eq(RoleDO::getId, id);
        return roleDAO.update(null, wrapper);
    }
}
