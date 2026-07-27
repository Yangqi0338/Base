package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.DealerCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.DealerEditReq;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
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
     * 交易师自定义注册。
     *
     * @param dealerEditReq 自定义注册请求
     * @return 交易师 ID
     */
    @PostMapping("dealerCustomSave")
    public PlatformResult<Long> dealerCustomSave(@Validated @RequestBody DealerCustomSaveReq dealerEditReq) {
        return PlatformResult.success(operatorClientDomain.dealerCustomSave(dealerEditReq, false));
    }

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

    /**
     * 交易师按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("dealerEditColumn")
    public PlatformResult<Void> dealerEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        operatorClientDomain.dealerEdit(cmd.getEditColumnList(), cmd.getId());
        return PlatformResult.success();
    }

    /**
     * 交易师删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("dealerDelete")
    public PlatformResult<Integer> dealerDelete(@Validated @RequestBody IdListCommand idListObj) {
        return PlatformResult.success(operatorClientDomain.dealerDelete(idListObj.getIdList()));
    }

    /**
     * 交易师详情。
     *
     * @param dealerId 交易师 ID (为空时取当前账号)
     * @return 交易师 VO
     */
    @PostMapping("dealer")
    public PlatformResult<DealerVO> dealer(@RequestParam(value = "id", required = false) Long dealerId) {
        if (dealerId == null) {
            dealerId = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(operatorClientDomain.dealer(dealerId));
    }
}
