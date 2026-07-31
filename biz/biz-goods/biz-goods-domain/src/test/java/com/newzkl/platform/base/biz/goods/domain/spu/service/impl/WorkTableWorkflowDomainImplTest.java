package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 工单审核工作流领域服务单元测试 (仓储端口 mock, 不连库)
 *
 * <p>重点验证: exists 判断后的条件写编排 (原 Repository 内多 IO 已上移)</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class WorkTableWorkflowDomainImplTest {

    @Mock
    private AuditDataWorkTableRepository auditDataWorkTableRepository;

    @InjectMocks
    private WorkTableWorkflowDomainImpl workTableWorkflowDomain;

    @Test
    void saveData_insertsWhenNewAndNotExists() {
        AuditDataWorkTableVO dataVO = new AuditDataWorkTableVO();
        AuditDataWorkTable entity = new AuditDataWorkTable();
        entity.setSpuId(10L);
        entity.setOperateTarget(1);
        when(auditDataWorkTableRepository.voToAuditDataWorkTable(dataVO)).thenReturn(entity);
        when(auditDataWorkTableRepository.existsAuditing(10L, 1)).thenReturn(false);

        workTableWorkflowDomain.saveData(dataVO);

        verify(auditDataWorkTableRepository).auditDataWorkTableInsert(entity);
        verify(auditDataWorkTableRepository, never()).auditDataWorkTableEdit(any());
    }

    @Test
    void saveData_skipsInsertWhenAuditingExists() {
        AuditDataWorkTableVO dataVO = new AuditDataWorkTableVO();
        AuditDataWorkTable entity = new AuditDataWorkTable();
        entity.setSpuId(10L);
        entity.setOperateTarget(1);
        when(auditDataWorkTableRepository.voToAuditDataWorkTable(dataVO)).thenReturn(entity);
        when(auditDataWorkTableRepository.existsAuditing(10L, 1)).thenReturn(true);

        workTableWorkflowDomain.saveData(dataVO);

        verify(auditDataWorkTableRepository, never()).auditDataWorkTableInsert(any());
        verify(auditDataWorkTableRepository, never()).auditDataWorkTableEdit(any());
    }

    @Test
    void saveData_editsWhenIdPresent() {
        AuditDataWorkTableVO dataVO = new AuditDataWorkTableVO();
        AuditDataWorkTable entity = new AuditDataWorkTable();
        entity.setId(99L);
        when(auditDataWorkTableRepository.voToAuditDataWorkTable(dataVO)).thenReturn(entity);

        workTableWorkflowDomain.saveData(dataVO);

        verify(auditDataWorkTableRepository).auditDataWorkTableEdit(entity);
        verify(auditDataWorkTableRepository, never()).existsAuditing(any(), any());
        verify(auditDataWorkTableRepository, never()).auditDataWorkTableInsert(any());
    }
}
