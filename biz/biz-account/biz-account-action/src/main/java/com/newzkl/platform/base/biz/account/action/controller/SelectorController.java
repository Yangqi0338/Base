package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 甄选师控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/selector")
@RequiredArgsConstructor
public class SelectorController {

    private final OperatorClientDomain operatorClientDomain;

    /**
     * 甄选师详情。
     *
     * @param selectorId 甄选师 ID (为空时取当前账号)
     * @return 甄选师 VO
     */
    @PostMapping("selector")
    public PlatformResult<SelectorVO> selector(@RequestParam(value = "id", required = false) Long selectorId) {
        if (selectorId == null) {
            selectorId = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(operatorClientDomain.selector(selectorId));
    }
}
