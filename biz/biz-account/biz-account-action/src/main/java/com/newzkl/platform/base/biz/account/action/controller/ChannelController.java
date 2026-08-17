package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.ServiceFeeConfigEdit;
import com.newzkl.platform.base.biz.account.action.cmd.ChannelCmd;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelUpdateReq;
import com.newzkl.platform.base.biz.account.model.res.ChannelPageRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-渠道商
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ChannelController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code channel} / {@code pageList})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelClientDomain channelClientDomain;
    private final UserQueryService userQueryService;
    private final IdentityService identityService;

    /**
     * 渠道商修改
     *
     * <p>保留旧鉴权语义: 平台端必须显式传 ID; 渠道商端强制改写为当前登录账号; 其余角色抛参数异常。</p>
     *
     * @param req 渠道商修改请求
     * @return 空结果
     */
    @PostMapping("/channelEdit")
    public PlatformResult<Void> channelEdit(@Validated @RequestBody ChannelReq req) {
        if (RoleEnum.CompanyRole.PLATFORM == SecurityUtils.getRole()) {
            if (req.getId() == null) {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        } else if (RoleEnum.CompanyRole.CHANNEL == SecurityUtils.getRole()) {
            req.setId(SecurityUtils.getAccountId());
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        channelClientDomain.channelEdit(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商详情
     *
     * @param id 渠道商账号ID, 不传取当前登录账号
     * @return 渠道商视图
     */
    @GetMapping("channel")
    public PlatformResult<ChannelVO> channel(@RequestParam(value = "id", required = false) Long id) {
        Long channelId = id == null ? SecurityUtils.getAccountId() : id;
        return PlatformResult.success(channelClientDomain.channel(channelId));
    }

    /**
     * 渠道商分页
     *
     * <p>保留旧角色分流校验: 平台端排除已注销; 交易师 / 运营商 / 运营商游客按本人归属过滤; 其余角色抛参数异常。
     * 迁移补充: 中台 {@code ChannelQuery} 无 {@code upOperatorId} 筛选字段, 归属过滤未能表达,
     * 见迁移报告「数据隔离降级」。</p>
     *
     * @param channelQuery 渠道商查询
     * @return 渠道商分页
     */
    @PostMapping("channelListVO")
    public PlatformResult<Page<ChannelVO>> channelPage(@RequestBody ChannelQuery channelQuery) {
        RoleEnum.CompanyRole role = SecurityUtils.getRole();
        CommonEnum.Client client = SecurityUtils.getClient();
        if (RoleEnum.CompanyRole.PLATFORM == role) {
            channelQuery.setStateOver(ChannelEnum.State.DESTORY);
        } else if (CommonEnum.Client.PARTNER != client) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(userQueryService.channelPage(channelQuery));
    }

    /**
     * 用于APP端市场渠道分页
     *
     * <p>保留旧语义: 未传 {@code idList} 时直接回空分页。</p>
     *
     * @param channelQuery 渠道商查询
     * @return 渠道商分页
     */
    @PostMapping("pageList")
    public PlatformResult<Page<ChannelVO>> pageList(@RequestBody ChannelQuery channelQuery) {
        if (channelQuery.getIdList() == null || channelQuery.getIdList().isEmpty()) {
            return PlatformResult.success(new Page<>());
        }
        return PlatformResult.success(userQueryService.channelPage(channelQuery));
    }

    /**
     * 渠道商上级交易师修改
     *
     * <p>旧 {@code @Limit(code=1023, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param command 上级交易师修改入参
     * @return 空结果
     */
    @PostMapping("channelUpEdit")
    public PlatformResult<Void> channelUpEdit(@Validated @RequestBody ChannelCmd.ChannelUpEdit command) {
        identityService.channelUpEdit(command.getChannelId(), command.getDealerId());
        return PlatformResult.success();
    }

    /**
     * 渠道商服务费修改
     *
     * <p>旧 {@code @Limit(code=1023, level=set)} 未迁移, 见迁移报告「鉴权降级」。
     * 迁移补充: 中台 {@code OperatorCmd.ServiceFeeConfigEdit} 的 {@code serviceFeeConfigVO}
     * 字段随资金域解耦已注释, 当前只能传账号ID, 见迁移报告「能力缺失」。</p>
     *
     * @param serviceFeeConfigEdit 服务费修改入参
     * @return 空结果
     */
    @PostMapping("serviceFeeConfigEdit")
    public PlatformResult<Void> serviceFeeConfigEdit(@RequestBody ServiceFeeConfigEdit serviceFeeConfigEdit) {
        identityService.serviceFeeConfigEdit(serviceFeeConfigEdit.getAccountId(), null);
        return PlatformResult.success();
    }

    /**
     * 查询渠道商服务费
     *
     * @param idListCommand 渠道商ID入参
     * @return 服务费配置
     */
    @PostMapping("/queryServiceFeeConfig")
    public PlatformResult<ServiceFeeConfigVO> queryServiceFeeConfig(@RequestBody @Valid IdCommand idListCommand) {
        return PlatformResult.success(identityService.queryServiceFeeConfig(CollUtil.getFirst(idListCommand.getIdList())));
    }

    /**
     * 渠道商分页-数字门店
     *
     * @param channelQuery 渠道商查询
     * @return 渠道商门店分页
     */
    @PostMapping("queryChannelPage")
    public PlatformResult<Page<ChannelPageRes>> queryChannelPage(@RequestBody ChannelQuery channelQuery) {
        return PlatformResult.success(userQueryService.queryChannelPage(channelQuery));
    }

    /**
     * 修改渠道商-数字门店
     *
     * @param req 渠道商修改请求
     * @return 空结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> update(@Validated @RequestBody ChannelUpdateReq req) {
        channelClientDomain.updateChannel(req);
        return PlatformResult.success();
    }
}
