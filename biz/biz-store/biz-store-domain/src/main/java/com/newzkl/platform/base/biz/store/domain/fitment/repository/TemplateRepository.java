package com.newzkl.platform.base.biz.store.domain.fitment.repository;

import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;

/**
 * @author niu
 * @description: 模板数仓
 * @date 2024/3/29 15:29
 */
public interface TemplateRepository {

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
     * 复制模版
     * @param templateId
     * @param channelId
     * @param shopId
     * @return
     */
    Long copyTemplate(Long templateId, Long channelId, Long shopId);
}
