package com.newzkl.platform.base.biz.store.domain.fitment.repository;

import com.github.pagehelper.PageInfo;
import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentPageQueryReq;
import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentTemplateQueryReq;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;

import java.util.List;

/**
 * @author niu
 * @description: 模板数仓
 * @date 2024/3/29 15:29
 */
public interface ITemplateRepository {

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
     * 查询模板列表
     * @param req
     * @return
     */
    PageInfo<FitmentTemplateVO> queryTemplate(FitmentTemplateQueryReq req);

    /**
     * 查询模板
     * @param id
     * @return
     */
    FitmentTemplateVO queryTemplate(Long id);

    /**
     * 查询页面
     * @param req
     * @return
     */
    PageInfo<FitmentPageVO> queryPage(FitmentPageQueryReq req);

    /**
     * 查询页面列表
     * @param templateId
     * @return
     */
    List<FitmentPageVO> queryPageList(Long templateId);

    /**
     * 查询页面详情
     * @param id
     * @return
     */
    FitmentPageVO queryPage(Long id);

    /**
     * 查询默认模板
     * @return
     */
    FitmentTemplateVO queryTemplateDefault();

    /**
     * 复制模版
     * @param templateId
     * @param channelId
     * @param shopId
     * @return
     */
    Long copyTemplate(Long templateId, Long channelId, Long shopId);

    /**
     * 查询样板店模板id
     * @param modelShopId
     * @return
     */
    Long queryModelShopTemplateId(Long modelShopId);
}
