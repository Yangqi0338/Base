package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.WorkTableWorkflowDomain;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.goods.model.exception.user.AuditErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description: 工单审核
 * @date 2023/10/1811:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTableWorkflowDomainImpl implements WorkTableWorkflowDomain {

    private final AuditDataWorkTableRepository auditDataWorkTableRepository;

    @Override
    public void editBusinessData(Long flowId, String editCommand) {
        AuditDataWorkTableVO auditDataWorkTableVO = this.detail(flowId);
        AuditDataWorkTable auditDataWorkTable = new AuditDataWorkTable();
        auditDataWorkTable.setSkuSalePriceJson(editCommand);
        auditDataWorkTableRepository.auditDataWorkTableSave(auditDataWorkTable);
    }

    @Override
    public String getContextParams(AuditDataWorkTableVO dataVO) {
        Map<String, Object> map = new HashMap<>(5);
        if (SpuEnum.OperateTarget.SALE_ATTRIBUTE.getCode().equals(dataVO.getOperateTarget())
                || SpuEnum.OperateTarget.SKU_BASE.getCode().equals(dataVO.getOperateTarget())) {
            if (SpuEnum.OperateType.UPDATE.getCode().equals(dataVO.getOperateType())
                    || SpuEnum.OperateType.ADD.getCode().equals(dataVO.getOperateType())) {
                map.put("price", "1");
            } else {
                map.put("price", "0");
            }
        } else {
            map.put("price", "0");
        }
        return JSONObject.toJSONString(map);
    }

    @Override
    public AuditDataWorkTableVO detail(Long flowId) {
        AuditDataWorkTableQuery query = new AuditDataWorkTableQuery();
        query.setId(flowId);
        List<AuditDataWorkTableVO> auditDataWorkTables = auditDataWorkTableRepository.auditDataWorkTableVOList(query);
        if (ObjectUtil.isEmpty(auditDataWorkTables)) {
            throw new PlatformException(AuditErrorCode.AUDIT_DATA_LOSE);
        }
        return auditDataWorkTables.get(0);
    }

    @Override
    public Page<AuditDataWorkTableVO> page(AuditDataWorkTableQuery pageQuery) {
        return auditDataWorkTableRepository.auditDataWorkTablePageVOList(pageQuery);
    }

    @Override
    public void saveData(AuditDataWorkTableVO dataVO) {
        AuditDataWorkTable auditDataWorkTable = auditDataWorkTableRepository.voToAuditDataWorkTable(dataVO);
        auditDataWorkTableRepository.auditDataWorkTableSave(auditDataWorkTable);
    }
}
