package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CdkRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.CdkDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.CdkDO;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkEditReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 开通码仓储实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.CdkRepositoryImpl}。
 * 旧 mapper xml 的 {@code insertBatch} / {@code cdkEditForToCdk} / {@code existValue} /
 * {@code idByQuery} 自定义 SQL 改由 MyBatis-Plus 通用方法与 wrapper 表达, 本仓不写 mapper xml。
 * 旧 {@code cdkListForApi}(openapi) 与 {@code jfCreateCdk}(直连账号 Dubbo facade) 未随本切片迁移。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CdkRepositoryImpl implements CdkRepository {

    private final CdkDAO cdkDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CdkVO cdk) {
        CdkDO cdkDO = TransferUtils.transfer(cdk, CdkDO::new);
        // 开通码 ID 由领域层雪花生成后透传, 不能走 preInsert 清 id
        cdkDAO.insert(cdkDO);
        return cdkDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<CdkVO> cdkList) {
        if (CollUtil.isEmpty(cdkList)) {
            return;
        }
        cdkDAO.insert(TransferUtils.transfers(cdkList, CdkDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(CdkVO cdk) {
        return cdkDAO.updateById(TransferUtils.transfer(cdk, CdkDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return cdkDAO.deleteByIds(idList);
    }

    @Override
    public CdkVO detail(Long id) {
        return TransferUtils.transfer(cdkDAO.selectById(id), CdkVO::new);
    }

    @Override
    public Long idByValue(String value) {
        CdkDO cdkDO = cdkDAO.selectOne(new LambdaQueryWrapper<CdkDO>()
                .select(CdkDO::getId)
                .eq(CdkDO::getValue, value), false);
        return cdkDO == null ? null : cdkDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editByQuery(CdkVO cdk, CdkQuery query) {
        return cdkDAO.update(TransferUtils.transfer(cdk, CdkDO::new), cdkDAO.getLw(query));
    }

    @Override
    public Set<String> existValue(Integer systemType, Set<String> valueList) {
        if (CollUtil.isEmpty(valueList)) {
            return new HashSet<>();
        }
        List<CdkDO> cdkList = cdkDAO.selectList(new LambdaQueryWrapper<CdkDO>()
                .select(CdkDO::getValue)
                .eq(CdkDO::getSystemType, systemType)
                .in(CdkDO::getValue, valueList));
        if (CollUtil.isEmpty(cdkList)) {
            return new HashSet<>();
        }
        return cdkList.stream().map(CdkDO::getValue).collect(Collectors.toSet());
    }

    @Override
    public List<Long> idByQuery(CdkQuery query) {
        List<CdkDO> cdkList = cdkDAO.selectList(cdkDAO.getLw(query).select(CdkDO::getId));
        if (CollUtil.isEmpty(cdkList)) {
            return new ArrayList<>();
        }
        return cdkList.stream().map(CdkDO::getId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editForToCdk(CdkEditReq edit, List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        LambdaUpdateWrapper<CdkDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(edit.getToState() != null, CdkDO::getToState, edit.getToState())
                .set(edit.getBelowRole() != null, CdkDO::getBelowRole, edit.getBelowRole())
                .set(edit.getOperatorId() != null, CdkDO::getOperatorId, edit.getOperatorId())
                .set(edit.getDealerId() != null, CdkDO::getDealerId, edit.getDealerId())
                .set(edit.getChannelId() != null, CdkDO::getChannelId, edit.getChannelId())
                .set(edit.getToDealerTime() != null, CdkDO::getToDealerTime, edit.getToDealerTime())
                .set(edit.getToChannelTime() != null, CdkDO::getToChannelTime, edit.getToChannelTime())
                .in(CdkDO::getId, idList);
        // 保留旧 SQL 的重复分配保护
        wrapper.isNull(edit.getDealerId() != null, CdkDO::getDealerId)
                .isNull(edit.getChannelId() != null, CdkDO::getChannelId);
        return cdkDAO.update(null, wrapper);
    }

    @Override
    public Page<CdkVO> pageList(CdkQuery query) {
        Page<CdkDO> pageList = cdkDAO.selectPage(RepositorySupport.page(query), cdkDAO.getLw(query));
        return TransferUtils.transferPage(pageList, CdkVO::new);
    }
}
