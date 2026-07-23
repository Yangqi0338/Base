package com.newzkl.platform.base.biz.finance.model.purse.res.huifu;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 15:25:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenAccountRes extends TripartiteAccountBaseRes {

    private String loginName;

    private String loginPassword;

}
