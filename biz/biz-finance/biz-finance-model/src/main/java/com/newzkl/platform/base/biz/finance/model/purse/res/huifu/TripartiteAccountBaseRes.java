package com.newzkl.platform.base.biz.finance.model.purse.res.huifu;

import com.newzkl.platform.base.biz.finance.model.support.TripartiteBaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class TripartiteAccountBaseRes extends TripartiteBaseRes {

    private String huifuId;

    @Override
    public String getTripartiteNo() {
        return huifuId;
    }

}