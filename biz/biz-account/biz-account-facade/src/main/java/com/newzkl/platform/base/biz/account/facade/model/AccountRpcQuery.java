package com.newzkl.platform.base.biz.account.facade.model;


import cn.hutool.db.Page;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 渠道商
 * @author fang
 */
@Data
public class AccountRpcQuery extends BizPageQuery {

    private String yqm;
    private String username;
    private AccountEnum.Client client;

}