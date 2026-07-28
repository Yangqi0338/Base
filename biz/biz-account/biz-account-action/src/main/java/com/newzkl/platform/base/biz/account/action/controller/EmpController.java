package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/emp")
@RequiredArgsConstructor
public class EmpController {

    private final AdminClientDomain adminClientDomain;

    /**
     * 员工分页。
     *
     * @param empQuery 员工查询
     * @return 员工分页
     */
    @PostMapping("empPage")
    public PlatformResult<Page<EmpRes>> empPage(@RequestBody EmpQuery empQuery) {
        return PlatformResult.success(adminClientDomain.empPage(empQuery));
    }
}
