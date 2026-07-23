package com.newzkl.platform.base.biz.store.domain.fitment.service.impl;

import com.github.pagehelper.PageInfo;
import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentPageQueryReq;
import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentTemplateQueryReq;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;
import com.newzkl.platform.base.biz.store.domain.fitment.repository.ITemplateRepository;
import com.newzkl.platform.base.biz.store.domain.fitment.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/3/29 15:29
 */
@Service
public class TemplateServiceImpl implements ITemplateService {

    private final ITemplateRepository templateRepository;

    @Autowired
    public TemplateServiceImpl(ITemplateRepository templateRepository) {
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
    public PageInfo<FitmentTemplateVO> queryTemplate(FitmentTemplateQueryReq req) {
        return templateRepository.queryTemplate(req);
    }

    @Override
    public FitmentTemplateVO queryTemplate(Long id) {
        return templateRepository.queryTemplate(id);
    }

    @Override
    public PageInfo<FitmentPageVO> queryPage(FitmentPageQueryReq req) {
        return templateRepository.queryPage(req);
    }

    @Override
    public List<FitmentPageVO> queryPageList(Long templateId) {
        return templateRepository.queryPageList(templateId);
    }

    @Override
    public FitmentPageVO queryPage(Long id) {
        return templateRepository.queryPage(id);
    }

    @Override
    public FitmentTemplateVO queryTemplateDefault() {
        return templateRepository.queryTemplateDefault();
    }

    @Override
    public Long useTemplate(Long templateId,Long channelId, Long shopId) {
        return templateRepository.copyTemplate(templateId,channelId,shopId);
    }

    @Override
    public Long queryModelShopTemplateId(Long modelShopId) {
        return templateRepository.queryModelShopTemplateId(modelShopId);
    }
}
