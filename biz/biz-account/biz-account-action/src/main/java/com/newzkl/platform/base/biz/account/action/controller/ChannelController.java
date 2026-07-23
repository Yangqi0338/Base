package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelUpdateReq;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
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
 * 渠道商控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelClientDomain channelClientDomain;

    /**
     * 渠道商自定义注册。
     *
     * @param customSaveReq 自定义注册请求
     * @return 渠道商 ID
     */
    @PostMapping("channelCustomSave")
    public ScmResult<Long> channelCustomSave(@Validated @RequestBody ChannelCustomSaveReq customSaveReq) {
        return ScmResult.success(channelClientDomain.channelCustomSave(customSaveReq));
    }

    /**
     * 渠道商修改。
     *
     * @param channelReq 渠道商请求
     * @return 修改数量
     */
    @PostMapping("channelEdit")
    public ScmResult<Integer> channelEdit(@Validated @RequestBody ChannelReq channelReq) {
        return ScmResult.success(channelClientDomain.channelEdit(channelReq));
    }

    /**
     * 渠道商按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("channelEditColumn")
    public ScmResult<Void> channelEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        channelClientDomain.channelEdit(cmd.getEditColumnList(), cmd.getId());
        return ScmResult.success();
    }

    /**
     * 渠道商删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("channelDelete")
    public ScmResult<Integer> channelDelete(@Validated @RequestBody IdListCommand idListObj) {
        return ScmResult.success(channelClientDomain.channelDelete(idListObj.getIdList()));
    }

    /**
     * 渠道商详情。
     *
     * @param channelId 渠道商 ID (为空时取当前账号)
     * @return 渠道商 VO
     */
    @PostMapping("channel")
    public ScmResult<ChannelVO> channel(@RequestParam(value = "id", required = false) Long channelId) {
        if (channelId == null) {
            channelId = SecurityUtils.getAccountId();
        }
        return ScmResult.success(channelClientDomain.channel(channelId));
    }

    /**
     * 修改渠道商。
     *
     * @param req 渠道商更新请求
     * @return 修改数量
     */
    @PostMapping("updateChannel")
    public ScmResult<Integer> updateChannel(@Validated @RequestBody ChannelUpdateReq req) {
        return ScmResult.success(channelClientDomain.updateChannel(req));
    }

    /**
     * 渠道商分页。
     *
     * @param channelQuery 渠道商查询
     * @return 渠道商分页
     */
    @PostMapping("channelPageList")
    public ScmResult<Page<ChannelVO>> channelPageList(@RequestBody ChannelQuery channelQuery) {
        return ScmResult.success(channelClientDomain.channelPageList(channelQuery));
    }
}
