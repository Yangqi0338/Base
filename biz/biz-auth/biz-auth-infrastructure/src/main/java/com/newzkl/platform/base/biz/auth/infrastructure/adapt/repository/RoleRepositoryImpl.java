package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.RoleDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.RoleDO;

import com.newzkl.platform.base.biz.auth.model.permission.dto.RoleDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 角色仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleDAO roleDAO;

    @Override
    public Long insert(RoleDTO dto) {
        RoleDO entity = TransferUtils.transfer(dto, RoleDO::new);
        roleDAO.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(RoleDTO dto) {
        roleDAO.updateById(TransferUtils.transfer(dto, RoleDO::new));
    }

    @Override
    public void deleteById(Long id) {
        roleDAO.deleteById(id);
    }

    @Override
    public RoleDTO getById(Long id) {
        return TransferUtils.transfer(roleDAO.selectById(id), RoleDTO::new);
    }

    @Override
    public RoleDTO getByCode(String code) {
        RoleDO entity = roleDAO.selectOne(new BaseLambdaQueryWrapper<RoleDO>().eq(RoleDO::getCode, code));
        return TransferUtils.transfer(entity, RoleDTO::new);
    }

    @Override
    public List<RoleDTO> listAll() {
        return TransferUtils.transfers(roleDAO.selectList(null), RoleDTO::new);
    }

    @Override
    public List<RoleDTO> listByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        return TransferUtils.transfers(roleDAO.selectByIds(ids), RoleDTO::new);
    }

    @Override
    public Page<RoleDTO> page(RoleQuery query) {
        Page<RoleDO> pageList = roleDAO.selectPage(RepositorySupport.page(query), roleDAO.getLw(query));
        return TransferUtils.transferPage(pageList, RoleDTO::new);
    }
}
