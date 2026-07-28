package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountParentQuery;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账号控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDomain accountDomain;

    /**
     * 下级账号列表。
     *
     * @param query 父账号查询
     * @return 下级账号列表
     */
    @PostMapping("subAccountList")
    public PlatformResult<List<SubAccountVO>> subAccountList(@RequestBody AccountParentQuery query) {
        return PlatformResult.success(accountDomain.subAccountList(query));
    }

    /**
     * 简易账号分页。
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("simpleAccountPage")
    public PlatformResult<Page<SimpleAccountRes>> simpleAccountPage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }
}
