package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.AccountJobDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountJobQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountJobReq;
import com.newzkl.platform.base.biz.account.model.res.AccountJobRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 职位控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/accountJob")
@RequiredArgsConstructor
public class AccountJobController {

    private final AccountJobDomain accountJobDomain;

    /**
     * 职位创建。
     *
     * @param accountJobReq 职位请求
     * @return 职位 ID
     */
    @PostMapping("save")
    public ScmResult<Long> save(@Validated @RequestBody AccountJobReq accountJobReq) {
        return ScmResult.success(accountJobDomain.save(accountJobReq));
    }

    /**
     * 职位删除。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("delete")
    public ScmResult<Void> delete(@Validated @RequestBody IdListCommand idListObj) {
        accountJobDomain.delete(idListObj.getIdList());
        return ScmResult.success();
    }

    /**
     * 职位详情。
     *
     * @param id 职位 ID
     * @return 职位 VO
     */
    @PostMapping("detail")
    public ScmResult<AccountJobVO> detail(@RequestParam("id") Long id) {
        return ScmResult.success(accountJobDomain.detail(id));
    }

    /**
     * 职位分页。
     *
     * @param accountJobQuery 职位查询
     * @return 职位分页
     */
    @PostMapping("accountJobPage")
    public ScmResult<Page<AccountJobRes>> accountJobPage(@RequestBody AccountJobQuery accountJobQuery) {
        return ScmResult.success(accountJobDomain.accountJobPageVO(accountJobQuery));
    }
}
