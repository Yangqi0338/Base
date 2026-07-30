package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;
import com.newzkl.platform.base.biz.store.domain.fitment.repository.TemplateRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.FitmentPageDAO;
import com.newzkl.platform.base.biz.store.infrastructure.dao.FitmentTemplateDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentPageDO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentTemplateDO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/3/29 15:40
 */
@Repository
public class TemplateRepositoryImpl implements TemplateRepository {

    private final FitmentPageDAO fitmentPageDAO;

    private final FitmentTemplateDAO fitmentTemplateDAO;

    @Autowired
    public TemplateRepositoryImpl(FitmentPageDAO fitmentPageDAO, FitmentTemplateDAO fitmentTemplateDAO) {
        this.fitmentPageDAO = fitmentPageDAO;
        this.fitmentTemplateDAO = fitmentTemplateDAO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplate(FitmentTemplateVO fitmentTemplate) {
        FitmentTemplateDO template = new FitmentTemplateDO();
        template.setId(fitmentTemplate.getId());
        template.setTemplateName(fitmentTemplate.getTemplateName());
        template.setTemplateLogo(fitmentTemplate.getTemplateLogo());
        template.setDefaultTemplate(fitmentTemplate.getDefaultTemplate());
        template.setPrice(fitmentTemplate.getPrice());
        template.setOperatorId(fitmentTemplate.getOperatorId());
        template.setOperatorName(fitmentTemplate.getOperatorName());
        template.setPageNavigation(fitmentTemplate.getPageNavigation());
        template.setPageStyle(fitmentTemplate.getPageStyle());
        template.setDetails(fitmentTemplate.getDetails());
        template.setChannelId(fitmentTemplate.getChannelId());
        template.setChannelName(fitmentTemplate.getChannelName());
        template.setState(fitmentTemplate.getState());
        template.setShopId(fitmentTemplate.getShopId());
        template.setCreateTime(fitmentTemplate.getCreateTime());
        if (template.getId() == null){
            template.setId(SnowflakeIdAble.getSnowflakeId());
            template.setCreateTime(LocalDateTime.now());
            fitmentTemplateDAO.insert(template);
        }else {
            fitmentTemplateDAO.updateById(template);
        }
    }

    @Override
    public void savePage(FitmentPageVO fitmentPage) {
        FitmentPageDO page = new FitmentPageDO();
        page.setId(fitmentPage.getId());
        page.setTemplateId(fitmentPage.getTemplateId());
        page.setPageName(fitmentPage.getPageName());
        page.setPageType(fitmentPage.getPageType());
        page.setContent(fitmentPage.getContent());
        page.setGoodsId(JSONUtil.toJsonStr(fitmentPage.getGoodsId()));
        page.setMarkets(JSONUtil.toJsonStr(fitmentPage.getMarkets()));
        page.setKv(fitmentPage.getKv());
        page.setCreateTime(fitmentPage.getCreateTime());
        page.setUpdateTime(fitmentPage.getUpdateTime());
        if (page.getId() == null){
            page.setId(SnowflakeIdAble.getSnowflakeId());
            page.setCreateTime(LocalDateTime.now());
            page.setUpdateTime(LocalDateTime.now());
            fitmentPageDAO.insert(page);
        }else {
            fitmentPageDAO.updateById(page);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyTemplate(Long templateId,Long channelId, Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        FitmentTemplateDO fitmentTemplate = fitmentTemplateDAO.selectById(templateId);
        Long newTemplateId = SnowflakeIdAble.getSnowflakeId();
        fitmentTemplate.setId(newTemplateId);
        fitmentTemplate.setCreateTime(now);
        fitmentTemplate.setChannelId(channelId);
        fitmentTemplate.setDefaultTemplate(0);
        fitmentTemplate.setShopId(shopId);
        fitmentTemplateDAO.insert(fitmentTemplate);

        List<FitmentPageDO> fitmentPages = fitmentPageDAO.selectList(
                new BaseLambdaQueryWrapper<FitmentPageDO>().eq(FitmentPageDO::getTemplateId, templateId));
        fitmentPages.forEach(x->{
            x.setId(SnowflakeIdAble.getSnowflakeId());
            x.setTemplateId(newTemplateId);
            x.setCreateTime(now);
            x.setUpdateTime(now);
            fitmentPageDAO.insert(x);
        });
        return newTemplateId;
    }
}
