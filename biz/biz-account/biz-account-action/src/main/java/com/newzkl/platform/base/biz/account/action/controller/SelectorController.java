package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SelectorCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SelectorEditReq;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
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
     * 甄选师自定义注册。
     *
     * @param req 自定义注册请求
     * @return 甄选师 ID
     */
    @PostMapping("selectorCustomSave")
    public PlatformResult<Long> selectorCustomSave(@Validated @RequestBody SelectorCustomSaveReq req) {
        return PlatformResult.success(operatorClientDomain.selectorCustomSave(req, false));
    }

    /**
     * 甄选师修改。
     *
     * @param selectorEditReq 甄选师编辑请求
     * @return 修改数量
     */
    @PostMapping("selectorEdit")
    public PlatformResult<Integer> selectorEdit(@Validated @RequestBody SelectorEditReq selectorEditReq) {
        return PlatformResult.success(operatorClientDomain.selectorEdit(selectorEditReq.getId(), selectorEditReq));
    }

    /**
     * 甄选师按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("selectorEditColumn")
    public PlatformResult<Void> selectorEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        operatorClientDomain.selectorEdit(cmd.getEditColumnList(), cmd.getId());
        return PlatformResult.success();
    }

    /**
     * 甄选师删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("selectorDelete")
    public PlatformResult<Integer> selectorDelete(@Validated @RequestBody IdListCommand idListObj) {
        return PlatformResult.success(operatorClientDomain.selectorDelete(idListObj.getIdList()));
    }

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

    /**
     * 甄选师提升等级。
     *
     * @param level 目标等级
     * @return 成功结果
     */
    @PostMapping("selectorLevelUp")
    public PlatformResult<Void> selectorLevelUp(@RequestParam("level") Integer level) {
        operatorClientDomain.selectorLevelUp(SecurityUtils.getAccountId(), level);
        return PlatformResult.success();
    }
}
