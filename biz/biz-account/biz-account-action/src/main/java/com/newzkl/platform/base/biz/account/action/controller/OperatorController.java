package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 运营商控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/operator")
@RequiredArgsConstructor
public class OperatorController {

    private final OperatorClientDomain operatorClientDomain;

    /**
     * 运营商修改。
     *
     * @param operatorEditReq 运营商请求
     * @return 运营商 VO
     */
    @PostMapping("operatorEdit")
    public PlatformResult<OperatorVO> operatorEdit(@Validated @RequestBody OperatorReq operatorEditReq) {
        return PlatformResult.success(operatorClientDomain.operatorEdit(operatorEditReq));
    }

    /**
     * 运营商详情。
     *
     * @param operatorId 运营商 ID (为空时取当前账号)
     * @return 运营商 VO
     */
    @PostMapping("operator")
    public PlatformResult<OperatorVO> operator(@RequestParam(value = "id", required = false) Long operatorId) {
        if (operatorId == null) {
            operatorId = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(operatorClientDomain.operator(operatorId));
    }
}
