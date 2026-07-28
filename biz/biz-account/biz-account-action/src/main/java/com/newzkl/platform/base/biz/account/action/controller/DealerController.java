package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.DealerEditReq;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易师控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final OperatorClientDomain operatorClientDomain;

    /**
     * 交易师修改。
     *
     * @param dealerEditReq 交易师编辑请求
     * @return 修改数量
     */
    @PostMapping("dealerEdit")
    public PlatformResult<Integer> dealerEdit(@Validated @RequestBody DealerEditReq dealerEditReq) {
        return PlatformResult.success(operatorClientDomain.dealerEdit(dealerEditReq.getId(), dealerEditReq));
    }
}
