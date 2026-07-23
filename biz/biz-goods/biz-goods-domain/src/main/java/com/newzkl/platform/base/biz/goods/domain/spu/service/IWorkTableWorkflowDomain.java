package com.newzkl.platform.base.biz.goods.domain.spu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;

/**
 * @author muc_fang
 * @Description: 工单审核
 * @date 2023/10/1811:58
 */
public interface IWorkTableWorkflowDomain {

    void editBusinessData(Long flowId, String editCommand);

    String getContextParams(AuditDataWorkTableVO dataVO);

    AuditDataWorkTableVO detail(Long flowId);

    Page<AuditDataWorkTableVO> page(AuditDataWorkTableQuery pageQuery);

    void saveData(AuditDataWorkTableVO dataVO);
}
