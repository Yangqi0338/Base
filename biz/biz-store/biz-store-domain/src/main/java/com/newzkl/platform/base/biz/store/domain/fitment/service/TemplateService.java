package com.newzkl.platform.base.biz.store.domain.fitment.service;

import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;

/**
 * @author niu
 * @description: 模板服务
 * @date 2024/3/29 15:16
 */
public interface TemplateService {

    /**
     * 保存模版
     * @param fitmentTemplate
     */
    void saveTemplate(FitmentTemplateVO fitmentTemplate);

    /**
     * 保存页面
     * @param fitmentPage
     */
    void savePage(FitmentPageVO fitmentPage);

    /**
     * 使用模版
     * @param templateId
     * @param channelId
     * @param shopId
     * @return
     */
    Long useTemplate(Long templateId,Long channelId, Long shopId);

}
