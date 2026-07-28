package com.newzkl.platform.base.biz.auth.model.rbac.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import lombok.Data;

import java.util.List;

@Data
public class FunctionPageQuery extends BizPageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * id集合
     */
    private List<Long> idList;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口路径
     */
    private String urlPath;

    /**
     * 类型
     *
     * @see AuthEnum.FunctionType
     */
    private Integer type;

}
