package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连大额行号查询请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CnapsQueryParams extends TripartiteBaseParam {

    /**
     * 银行编码。
     */
    private String bank_code;

    /**
     * 开户支行名称, 支持模糊查询。
     */
    private String brabank_name;

    /**
     * 开户行所在省市编码。
     */
    private String city_code;
}
