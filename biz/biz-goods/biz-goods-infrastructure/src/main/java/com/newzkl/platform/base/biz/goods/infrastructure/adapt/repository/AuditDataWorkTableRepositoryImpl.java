package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.AuditDataWorkTableDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.AuditDataWorkTableDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 工单审核数据
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AuditDataWorkTableRepositoryImpl implements AuditDataWorkTableRepository {

    private final AuditDataWorkTableDAO auditDataWorkTableDAO;

    @Override
    public AuditDataWorkTable voToAuditDataWorkTable(AuditDataWorkTableVO auditDataWorkTableVO) {
        return TransferUtils.transfer(auditDataWorkTableVO, AuditDataWorkTable::new);
    }

    @Override
    public boolean existsAuditing(Long spuId, Integer operateTarget) {
        return auditDataWorkTableDAO.exists(new LambdaQueryWrapper<AuditDataWorkTableDO>()
                .eq(AuditDataWorkTableDO::getState, AuditEnum.State.AUDITING)
                .eq(AuditDataWorkTableDO::getForeignId, spuId)
                .eq(AuditDataWorkTableDO::getOperateTarget, operateTarget)
        );
    }

    @Override
    public Long auditDataWorkTableInsert(AuditDataWorkTable auditDataWorkTable) {
        AuditDataWorkTableDO auditDataWorkTableDO = TransferUtils.transfer(auditDataWorkTable, AuditDataWorkTableDO::new);
        auditDataWorkTableDO.setId(SnowflakeIdAble.getSnowflakeId());
        auditDataWorkTableDAO.insert(Collections.singletonList(auditDataWorkTableDO));
        return auditDataWorkTableDO.getId();
    }

    @Override
    public void auditDataWorkTableEdit(AuditDataWorkTable auditDataWorkTable) {
        auditDataWorkTableDAO.updateById(TransferUtils.transfer(auditDataWorkTable, AuditDataWorkTableDO::new));
    }

    @Override
    public void auditDataWorkTableDelete(List<Long> idList) {
        auditDataWorkTableDAO.deleteByIds(idList);
    }

    @Override
    public void auditDataWorkTableUpdateByQuery(AuditDataWorkTable auditDataWorkTable, AuditDataWorkTableQuery auditDataWorkTableQuery) {
        auditDataWorkTableDAO.update(TransferUtils.transfer(auditDataWorkTable, AuditDataWorkTableDO::new), auditDataWorkTableDAO.buildQueryWrapper(auditDataWorkTableQuery));
    }

    @Override
    public AuditDataWorkTable auditDataWorkTable(Long id) {
        return TransferUtils.transfer(auditDataWorkTableDAO.selectById(id), AuditDataWorkTable::new);
    }

    @Override
    public AuditDataWorkTableVO auditDataWorkTableVO(Long id) {
        return TransferUtils.transfer(auditDataWorkTableDAO.selectById(id), AuditDataWorkTableVO::new);
    }

    @Override
    public List<AuditDataWorkTable> auditDataWorkTableList(AuditDataWorkTableQuery auditDataWorkTableQuery) {
        return TransferUtils.transfers(this.auditDataWorkTablePageVOList(auditDataWorkTableQuery).getRecords(), AuditDataWorkTable::new);
    }

    @Override
    public List<AuditDataWorkTableVO> auditDataWorkTableVOList(AuditDataWorkTableQuery auditDataWorkTableQuery) {
        return this.auditDataWorkTablePageVOList(auditDataWorkTableQuery).getRecords();
    }

    @Override
    public Page<AuditDataWorkTableVO> auditDataWorkTablePageVOList(AuditDataWorkTableQuery auditDataWorkTableQuery) {
        return TransferUtils.transferPage(auditDataWorkTableDAO.selectPage(RepositorySupport.page(auditDataWorkTableQuery), auditDataWorkTableDAO.buildQueryWrapper(auditDataWorkTableQuery)), AuditDataWorkTableVO::new);
    }
}
