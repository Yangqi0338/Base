package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 渠道商修改。
     *
     * @param channelReq 渠道商请求
     * @return 修改数量
     */
    @PostMapping("channelEdit")
    public PlatformResult<Integer> channelEdit(@Validated @RequestBody ChannelReq channelReq) {
        return PlatformResult.success(channelClientDomain.channelEdit(channelReq));
    }

    /**
     * 渠道商详情。
     *
     * @param channelId 渠道商 ID (为空时取当前账号)
     * @return 渠道商 VO
     */
    @GetMapping("channel")
    public PlatformResult<ChannelVO> channel(@RequestParam(value = "id", required = false) Long channelId) {
        if (channelId == null) {
            channelId = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(channelClientDomain.channel(channelId));
    }
}
