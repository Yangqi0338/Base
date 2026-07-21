package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 市场交易师
 *
 * @author fang
 */
@Data
@NoArgsConstructor
public class DealerQuery extends PageQuery {
    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 交易师名称
     */
    private String name;
    /**
     * 交易师账号
     */
    private String username;
    /**
     * 运营商名称 (查询)
     */
    private String operatorName;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
     * 状态大于
     */
    private Integer stateOver;

    public DealerQuery(Long id) {
        this.id = id;
    }
}
