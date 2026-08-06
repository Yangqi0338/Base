package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.PermissionRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.PermissionDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionDO;
import com.newzkl.platform.base.biz.auth.model.enums.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 权限仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionDAO permissionDAO;

    @Override
    public Long insert(PermissionDTO dto) {
        PermissionDO entity = TransferUtils.transfer(dto, PermissionDO::new);
        permissionDAO.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(PermissionDTO dto) {
        permissionDAO.updateById(TransferUtils.transfer(dto, PermissionDO::new));
    }

    @Override
    public void softDeleteByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        permissionDAO.deleteByIds(ids);
    }

    @Override
    public PermissionDTO getById(Long id) {
        return TransferUtils.transfer(permissionDAO.selectById(id), PermissionDTO::new);
    }

    @Override
    public PermissionDTO getByCode(String code) {
        PermissionDO entity = permissionDAO.selectOne(new BaseLambdaQueryWrapper<PermissionDO>()
                .eq(PermissionDO::getCode, code));
        return TransferUtils.transfer(entity, PermissionDTO::new);
    }

    @Override
    public List<PermissionDTO> listByType(PermissionEnum.Type type) {
        List<PermissionDO> list = permissionDAO.selectList(permissionDAO.getLw(type));
        return TransferUtils.transfers(list, PermissionDTO::new);
    }

    @Override
    public List<PermissionDTO> listByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        return TransferUtils.transfers(permissionDAO.selectByIds(ids), PermissionDTO::new);
    }

    @Override
    public List<PermissionDTO> listByCodes(Collection<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return List.of();
        }
        return TransferUtils.transfers(permissionDAO.selectList(permissionDAO.getLwByCodes(codes)), PermissionDTO::new);
    }
}
