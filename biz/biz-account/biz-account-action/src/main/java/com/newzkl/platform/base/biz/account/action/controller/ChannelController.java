package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.dto.ChannelDTO;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelRegisterReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.res.ChannelRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-渠道商
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ChannelController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code channel})。</p>
 *
 * <p>迁出端点: {@code serviceFeeConfigEdit} / {@code queryServiceFeeConfig} 已归位资金域
 * {@code finance.action.controller.ConfigController} ({@code /config/*}), 服务费配置属资金域数据。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/channel")
@RequiredArgsConstructor
@FuncPermission("用户-渠道商")
public class ChannelController {

    private final ChannelClientDomain channelClientDomain;
    private final AccountService accountService;

    /**
     * 渠道商新增
     *
     * <p>收渠道商专用入参 {@link ChannelRegisterReq}, 端内转 {@link AdminRegisterIdentityReq}:
     * identity 固定 CHANNEL, 角色由 roleIdList 指定并在注册链绑定。渠道商业务字段 (公司信息/联系人等)
     * 由注册链按昵称兜底, 其余走 {@link #channelEdit} 补充。</p>
     *
     * @param req 渠道商注册请求
     * @return 新建账号 ID
     */
    @RoleLimit(client = AccountEnum.Client.ADMIN)
    @PostMapping("/create")
    @FuncPermission("新建渠道商")
    public PlatformResult<Long> create(@RequestBody @Validated ChannelRegisterReq req) {
        log.info("新增渠道商");
        AdminRegisterIdentityReq registerReq = TransferUtils.transfer(req, AdminRegisterIdentityReq.class);
        registerReq.setIdentity(AccountEnum.Identity.CHANNEL);
        return PlatformResult.success(accountService.identityCreate(registerReq));
    }

    /**
     * 渠道商修改
     *
     * <p>保留旧鉴权语义: 平台端必须显式传 ID; 渠道商端强制改写为当前登录账号; 其余角色抛参数异常。</p>
     *
     * @param req 渠道商修改请求
     * @return 空结果
     */
    @RoleLimit({AccountEnum.Identity.PLATFORM, AccountEnum.Identity.PLATFORM, AccountEnum.Identity.CHANNEL})
    @PostMapping("/channelEdit")
    @FuncPermission("渠道商修改")
    public PlatformResult<Void> channelEdit(@Validated @RequestBody ChannelReq req) {
        if (AccountEnum.Client.ADMIN == SecurityUtils.getClient()) {
            if (req.getId() == null) {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        } else if (AccountEnum.Identity.CHANNEL == SecurityUtils.getIdentity()) {
            req.setId(SecurityUtils.getAccountId());
        }
        channelClientDomain.channelEdit(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商纯净详情
     *
     * @param id 渠道商账号ID, 不传取当前登录账号
     * @return 渠道商纯净视图
     * @ext 主数据 channel (无副数据)
     */
    @GetMapping("/base")
    public PlatformResult<ChannelDTO> channelBase(@RequestParam(value = "id", required = false) Long id) {
        Long channelId = id == null ? SecurityUtils.getAccountId() : id;
        return PlatformResult.success(channelClientDomain.channelBase(channelId));
    }

    /**
     * 渠道商详情
     *
     * @param id 渠道商账号ID, 不传取当前登录账号
     * @return 渠道商聚合视图
     * @ext 主数据 channel, 副数据 account(单副, 副数据不再向下关联)。方向与
     *      {@code AccountController.identityDetail}(主 account / 副身份) 相反, 两者不可互相替代
     */
    @GetMapping("channel")
    public PlatformResult<ChannelRes> channel(@RequestParam(value = "id", required = false) Long id) {
        Long channelId = id == null ? SecurityUtils.getAccountId() : id;
        return PlatformResult.success(channelClientDomain.channel(channelId));
    }
}
