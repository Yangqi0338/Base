package com.newzkl.platform.base.biz.goods.domain.freight.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;

import java.util.List;

/**
* 运费模板
* @author fang
*/
public interface FreightTemplateRepository {

    Long freightTemplateSave(FreightTemplate freightTemplate);

    void freightTemplateDelete(List<Long> idList);

    void freightTemplateEdit(FreightTemplate freightTemplate);

    FreightTemplate freightTemplate(Long id);

    Page<FreightTemplateVO> freightTemplatePage(FreightTemplateQuery freightTemplateQuery);

}
