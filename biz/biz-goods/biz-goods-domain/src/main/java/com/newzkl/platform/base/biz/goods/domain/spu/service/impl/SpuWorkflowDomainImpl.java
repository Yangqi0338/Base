package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataSpuRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuWorkflowDomain;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataSpu;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AuditErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/1811:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuWorkflowDomainImpl implements SpuWorkflowDomain {

    private final AuditDataSpuRepository auditDataSpuRepository;

    @Override
    public void editBusinessData(Long flowId, String editCommand) {
        AuditDataSpu auditDataSpu = new AuditDataSpu();
        auditDataSpu.setSkuSalePriceJson(editCommand);
        auditDataSpuRepository.auditDataSpuSave(auditDataSpu);
    }

    @Override
    public AuditDataSpuVO detail(Long flowId) {
        AuditDataSpuQuery query = new AuditDataSpuQuery();
        query.setId(flowId);
        List<AuditDataSpuVO> auditDataSpus = auditDataSpuRepository.auditDataSpuVOList(query);
        if (ObjectUtil.isEmpty(auditDataSpus)) {
            throw new PlatformException(AuditErrorCode.AUDIT_DATA_LOSE);
        }
        return auditDataSpus.get(0);
    }

    @Override
    public Page<AuditDataSpuVO> page(AuditDataSpuQuery pageQuery) {
        pageQuery.setIsNew(CommonEnum.YesOrNo.YES.getCode());
        return auditDataSpuRepository.auditDataSpuPageVOList(pageQuery);
    }

    @Override
    public Long saveData(AuditDataSpuVO dataVO) {
        return auditDataSpuRepository.auditDataSpuSave(auditDataSpuRepository.voToAuditDataSpu(dataVO));
    }

    @Override
    public List<Long> getOldFlowIdList(Long auditFlowId, Long accountId, Long roleId, AuditDataSpuVO dataVO) {
        AuditDataSpuQuery auditDataSpuQuery = new AuditDataSpuQuery();
        auditDataSpuQuery.setSpuIdList(Collections.singletonList(dataVO.getSpuId()));
        List<Long> flowIdList = auditDataSpuRepository.auditDataSpuPageVOList(auditDataSpuQuery).getRecords().stream().map(AuditDataSpuVO::getId).collect(Collectors.toList());
        flowIdList.remove(auditFlowId);
        return flowIdList;
    }
}
