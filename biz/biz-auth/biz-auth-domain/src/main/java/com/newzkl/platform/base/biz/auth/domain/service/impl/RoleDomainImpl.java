package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.assembler.RoleAssembler;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.auth.model.role.vo.RoleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色领域服务实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.IRoleDomainImpl}。
 * 旧实现把 {@code dataGroupIdList} 手工 {@code JSONObject.toJSONString} 后写 String 列,
 * 本仓由 DO 的 JSON 列 typeHandler 承担, 领域层不再关心序列化;
 * 旧 {@code role(Long)} 会向实体回塞 repository 引用 (充血), 已去除。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class RoleDomainImpl implements RoleDomain {

    private final RoleRepository roleRepository;
    private final RoleAssembler assembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(RoleReq req) {
        RoleVO item = assembler.req2VO(req);
        item.setId(SnowflakeIdAble.getSnowflakeId());
        return roleRepository.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Long id, RoleReq req) {
        RoleVO item = assembler.req2VO(req);
        item.setId(id);
        return roleRepository.edit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return roleRepository.delete(idList);
    }

    @Override
    public RoleVO role(Long id) {
        return roleRepository.detail(id);
    }

    @Override
    public RoleRes detail(Long id) {
        return assembler.vo2Res(roleRepository.detail(id));
    }

    @Override
    public List<RoleRes> list(RoleQuery query) {
        // 保留旧语义: 旧 rolePage 仅在 pageNo > 0 时启用分页, 否则整表返回
        List<RoleVO> roleList = query.getPageNo() != null && query.getPageNo() > 0
                ? roleRepository.pageList(query).getRecords()
                : roleRepository.list(query);
        if (CollUtil.isEmpty(roleList)) {
            return new ArrayList<>();
        }
        return TransferUtils.transfers(roleList, assembler::vo2Res);
    }

    @Override
    public Page<RoleRes> pageList(RoleQuery query) {
        return TransferUtils.transferPage(roleRepository.pageList(query), assembler::vo2Res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addCount(Long id) {
        return roleRepository.addCount(id);
    }
}
