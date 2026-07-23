package com.newzkl.platform.base.biz.goods.model.goods.query.freight;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 运费模板
* @author fang
*/
@Data
public class FreightTemplateQuery extends PageQuery {

    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
    * 名称 查询
    */
    private String name;
    /**
     * 账号ID (查询)
     */
    private Long accountId;
    /**
     * 账号ID 和 平台Id
     */
    private Long accountIdAndAdmin;
}
