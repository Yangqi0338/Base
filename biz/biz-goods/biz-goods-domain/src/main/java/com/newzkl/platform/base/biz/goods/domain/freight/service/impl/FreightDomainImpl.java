package com.newzkl.platform.base.biz.goods.domain.freight.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.freight.repository.FreightTemplateRepository;
import com.newzkl.platform.base.biz.goods.domain.freight.service.FreightDomain;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.freight.FreightTemplateReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运费模板
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class FreightDomainImpl implements FreightDomain {

    private final FreightTemplateRepository freightTemplateRepository;

    @Override
    public Long freightTemplateSave(FreightTemplateReq req) {
        FreightTemplate freightTemplate = TransferUtils.transfer(req, FreightTemplate::new);
        freightTemplate.init();
        return freightTemplateRepository.freightTemplateSave(freightTemplate);
    }

    @Override
    public FreightTemplate freightTemplate(Long templateId) {
        return freightTemplateRepository.freightTemplate(templateId);
    }

    @Override
    public void freightTemplateEdit(FreightTemplateReq req) {
        FreightTemplate freightTemplate = TransferUtils.transfer(req, FreightTemplate::new);
        freightTemplate.init();
        freightTemplateRepository.freightTemplateEdit(freightTemplate);
    }

    @Override
    public void freightTemplateDelete(List<Long> idList) {
        freightTemplateRepository.freightTemplateDelete(idList);
    }

    @Override
    public Page<FreightTemplateVO> freightTemplatePage(FreightTemplateQuery freightTemplateQuery) {
        return freightTemplateRepository.freightTemplatePage(freightTemplateQuery);
    }
}
