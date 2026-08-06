package com.newzkl.platform.base.biz.sys.model.project.query;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目分页查询
 *
 * <p>迁移说明: 源 {@code ProjectQuery extends BusinessPageQuery}, Base 对应基类为
 * {@code BizPageQuery}(同样提供 idList/accountId/createTime 区间)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectQuery extends BizPageQuery {

    /**
     * 名称 (模糊匹配)
     */
    private String name;

    /**
     * 省编码 (精确匹配)
     */
    private Integer province;

    /**
     * 市编码 (精确匹配)
     */
    private Integer city;

    /**
     * 区编码 (精确匹配)
     */
    private Integer area;

    /**
     * 合作金额区间起点 (含, Money)
     */
    private Money basicStartAmount;

    /**
     * 合作金额区间终点 (含, Money)
     */
    private Money basicEndAmount;

    /**
     * 标签 (模糊匹配逗号分隔串)
     */
    private String flags;

    /**
     * 意向人数 (精确匹配)
     */
    private Integer interestNum;

    /**
     * 意向人 (模糊匹配)
     */
    private String interestPerson;
}
