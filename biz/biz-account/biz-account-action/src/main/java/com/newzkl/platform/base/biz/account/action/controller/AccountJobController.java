package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.AccountJobDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountJobReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountJobVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
    public PlatformResult<Long> save(@Validated @RequestBody AccountJobReq accountJobReq) {
        return PlatformResult.success(accountJobDomain.save(accountJobReq));
    }

    /**
     * 职位删除。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("delete")
    public PlatformResult<Void> delete(@Validated @RequestBody IdListCommand idListObj) {
        accountJobDomain.delete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 职位详情。
     *
     * @param id 职位 ID
     * @return 职位 VO
     */
    @PostMapping("detail")
    public PlatformResult<AccountJobVO> detail(@RequestParam("id") Long id) {
        return PlatformResult.success(accountJobDomain.detail(id));
    }
}
