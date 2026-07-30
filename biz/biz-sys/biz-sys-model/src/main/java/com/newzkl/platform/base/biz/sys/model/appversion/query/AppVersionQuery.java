package com.newzkl.platform.base.biz.sys.model.appversion.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * app 版本分页查询
 *
 * @author niu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppVersionQuery extends PageQuery {

    /**
     * app 名称
     */
    private String appName;
}
