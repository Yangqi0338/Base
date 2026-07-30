package com.newzkl.platform.base.biz.store.domain.fitment.service.impl;

import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;
import com.newzkl.platform.base.biz.store.domain.fitment.repository.TemplateRepository;
import com.newzkl.platform.base.biz.store.domain.fitment.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author niu
 * @description:
 * @date 2024/3/29 15:29
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void saveTemplate(FitmentTemplateVO fitmentTemplate) {
        templateRepository.saveTemplate(fitmentTemplate);
    }

    @Override
    public void savePage(FitmentPageVO fitmentPage) {
        templateRepository.savePage(fitmentPage);
    }

    @Override
    public Long useTemplate(Long templateId,Long channelId, Long shopId) {
        return templateRepository.copyTemplate(templateId,channelId,shopId);
    }
}
