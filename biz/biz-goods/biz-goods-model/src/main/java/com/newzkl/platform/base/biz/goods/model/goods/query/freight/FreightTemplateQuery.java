package com.newzkl.platform.base.biz.goods.model.goods.query.freight;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 运费模板
* @author fang
*/
@Data
public class FreightTemplateQuery extends BizPageQuery {
    /**
    * 名称 查询
    */
    private String name;
}
