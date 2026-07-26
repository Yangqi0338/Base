package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 连连大额行号查询响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CnapsQueryResult extends LianLianBaseRes {

    /**
     * 银行编码。
     */
    private String bank_code;

    /**
     * 支行列表。
     */
    private List<BankCnaps> card_list;

    /**
     * 支行大额行号条目。
     *
     * @author KC
     */
    @Data
    public static class BankCnaps {
        /** 大额行号。 */
        private String cnaps_code;
        /** 开户支行名称全称。 */
        private String brabank_name;
    }
}
