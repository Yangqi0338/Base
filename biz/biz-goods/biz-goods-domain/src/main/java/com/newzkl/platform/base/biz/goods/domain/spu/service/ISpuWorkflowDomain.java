package com.newzkl.platform.base.biz.goods.domain.spu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1811:58
 */
public interface ISpuWorkflowDomain {

    void editBusinessData(Long flowId, String editCommand);

    AuditDataSpuVO detail(Long flowId);

    Page<AuditDataSpuVO> page(AuditDataSpuQuery pageQuery);

    Long saveData(AuditDataSpuVO dataVO);

    List<Long> getOldFlowIdList(Long auditFlowId, Long accountId, Long roleId, AuditDataSpuVO dataVO);

}
