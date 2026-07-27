package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.OperatorCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
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
     * 运营商按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("operatorEditColumn")
    public PlatformResult<Void> operatorEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        operatorClientDomain.operatorEdit(cmd.getEditColumnList(), cmd.getId());
        return PlatformResult.success();
    }

    /**
     * 运营商删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("operatorDelete")
    public PlatformResult<Integer> operatorDelete(@Validated @RequestBody IdListCommand idListObj) {
        return PlatformResult.success(operatorClientDomain.operatorDelete(idListObj.getIdList()));
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

    /**
     * 运营商自定义注册。
     *
     * @param operatorEditReq 自定义注册请求
     * @return 运营商 VO
     */
    @PostMapping("operatorCustomSave")
    public PlatformResult<OperatorVO> operatorCustomSave(@Validated @RequestBody OperatorCustomSaveReq operatorEditReq) {
        return PlatformResult.success(operatorClientDomain.operatorCustomSave(operatorEditReq, SecurityUtils.getAccountId()));
    }

    /**
     * 运营商代理注册。
     *
     * @param operatorProxySaveReq 代理注册请求
     * @return 运营商 VO
     */
    @PostMapping("operatorProxySave")
    public PlatformResult<OperatorVO> operatorProxySave(@Validated @RequestBody OperatorProxySaveReq operatorProxySaveReq) {
        return PlatformResult.success(operatorClientDomain.operatorProxySave(operatorProxySaveReq, SecurityUtils.getAccountId()));
    }

    /**
     * 获取运营商信息。
     *
     * @param id 运营商 ID
     * @return 运营商域信息
     */
    @PostMapping("getOperatorDomainInfo")
    public PlatformResult<OperatorDomainInfo> getOperatorDomainInfo(@RequestParam("id") Long id) {
        return PlatformResult.success(operatorClientDomain.getOperatorDomainInfo(id));
    }
}
