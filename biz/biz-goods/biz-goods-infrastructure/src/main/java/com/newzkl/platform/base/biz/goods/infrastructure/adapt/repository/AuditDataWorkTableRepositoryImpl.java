package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.IAuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.AuditDataWorkTableDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.AuditDataWorkTableDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 工单审核数据
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AuditDataWorkTableRepositoryImpl implements IAuditDataWorkTableRepository {

    private final AuditDataWorkTableDAO auditDataWorkTableDAO;

    @Override
    public AuditDataWorkTable voToAuditDataWorkTable(AuditDataWorkTableVO auditDataWorkTableVO) {
        return TransferUtils.transfer(auditDataWorkTableVO, AuditDataWorkTable::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long auditDataWorkTableSave(AuditDataWorkTable auditDataWorkTable) {
        AuditDataWorkTableDO auditDataWorkTableDO = TransferUtils.transfer(auditDataWorkTable, AuditDataWorkTableDO::new);
        if (auditDataWorkTableDO.getId() == null || auditDataWorkTableDO.getId() == 0) {
            boolean exists = auditDataWorkTableDAO.exists(new LambdaQueryWrapper<AuditDataWorkTableDO>()
                    .eq(AuditDataWorkTableDO::getState, AuditEnum.State.AUDITING)
                    .eq(AuditDataWorkTableDO::getForeignId, auditDataWorkTable.getSpuId())
                    .eq(AuditDataWorkTableDO::getOperateTarget, auditDataWorkTable.getOperateTarget())
            );
            //如果当前商品存在同类型且待审核状态的工单，不允许提交工单
            if (!exists) {
                auditDataWorkTableDO.setId(SnowflakeIdAble.getSnowflakeId());
                auditDataWorkTableDAO.insert(Collections.singletonList(auditDataWorkTableDO));
            }
        } else {
            auditDataWorkTableDAO.updateById(auditDataWorkTableDO);
        }
        return auditDataWorkTableDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
