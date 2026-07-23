package com.newzkl.platform.base.biz.goods.domain.freight.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.freight.FreightTemplateReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;

import java.util.List;

/**
* 运费模板
* @author fang
*/
public interface IFreightDomain {

    /**
     * 运费模板保存
     */
    Long freightTemplateSave(FreightTemplateReq req);
    /**
     * 运费模板实体
     * @param templateId
     * @return
     */
    FreightTemplate freightTemplate(Long templateId);

    void freightTemplateEdit(FreightTemplateReq freightTemplateVO);

    /**
     * 运费模板删除
     * @param idList
     */
    void freightTemplateDelete(List<Long> idList);

    Page<FreightTemplateVO> freightTemplatePage(FreightTemplateQuery freightTemplateQuery);

}
