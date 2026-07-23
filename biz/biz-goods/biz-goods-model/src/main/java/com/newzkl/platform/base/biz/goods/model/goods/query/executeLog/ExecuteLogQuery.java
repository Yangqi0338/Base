package com.newzkl.platform.base.biz.goods.model.goods.query.executeLog;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 操作日志
* @author fang
*/
@Data
public class ExecuteLogQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
}
