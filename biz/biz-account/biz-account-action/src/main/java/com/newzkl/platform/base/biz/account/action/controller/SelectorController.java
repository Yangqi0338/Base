package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.model.req.SelectorCmd;
import com.newzkl.platform.base.biz.account.model.req.SelectorEditReq;
import com.newzkl.platform.base.biz.account.model.req.SelectorQuery;
import com.newzkl.platform.base.biz.account.model.req.web.SelectorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.IndexCountRes;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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
 * 用户-甄选师
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.SelectorController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中同一控制器内混用前导斜杠的写法
 * (如 {@code /indexCount} 与 {@code selectorPage})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/selector")
@RequiredArgsConstructor
public class SelectorController {

    private final OperatorClientDomain operatorClientDomain;
    private final UserQueryService userQueryService;

    /**
     * 首页统计数据
     *
     * <p>迁移补充: 旧实现另经 Dubbo 取字典 1004 的历史误差值累加到周/月订单数与渠道商数,
     * 并经 Dubbo 取甄选师等级权限回填 {@code selectorPermissionVO}; 中台无字典与等级出站端口,
     * 两项均未迁移, 当前只回中台自算统计, 见迁移报告「能力缺失」。</p>
     *
     * @return 首页统计
     */
    @PostMapping("/indexCount")
    public PlatformResult<IndexCountRes> indexCount() {
        return PlatformResult.success(userQueryService.indexCount());
    }

    /**
     * 甄选师基本信息修改
     *
     * <p>保留旧语义: 强制改写为当前登录账号。旧接口声明 {@code ScmResult<PageInfo<SelectorSupplierVO>>}
     * 但实际回空, 迁移后出参收敛为 {@code Void}。
     * 旧 {@code @Limit(code=1024, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param selectorEditReq 甄选师修改请求
     * @return 空结果
     */
    @PostMapping("selectorBaseVOEdit")
    public PlatformResult<Void> selectorEdit(@RequestBody SelectorEditReq selectorEditReq) {
        selectorEditReq.setId(SecurityUtils.getAccountId());
        operatorClientDomain.selectorEdit(selectorEditReq.getId(), selectorEditReq);
        return PlatformResult.success();
    }

    /**
     * 甄选师修改上级甄选师
     *
     * <p>迁移补充: 旧实现走充血实体 {@code Selector.selectorInviteIdEdit(inviteId)} 只写单列,
     * 中台贫血模型下改为按 {@code SelectorEditReq} 只填 {@code inviteId} 调统一修改。
     * 旧 {@code @Limit(code=1024, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param command 上级甄选师修改入参
     * @return 空结果
     */
    @PostMapping("selectorInviteIdEdit")
    public PlatformResult<Void> selectorInviteIdEdit(@RequestBody SelectorCmd.SelectorInviteIdEdit command) {
        SelectorEditReq selectorEditReq = new SelectorEditReq();
        selectorEditReq.setInviteId(command.getInviteId());
        operatorClientDomain.selectorEdit(command.getId(), selectorEditReq);
        return PlatformResult.success();
    }

    /**
     * 甄选师详情
     *
     * @param selectorId 甄选师ID入参
     * @return 甄选师视图
     */
    @PostMapping("selector")
    public PlatformResult<SelectorVO> selector(@Validated @RequestBody SelectorCmd.ID selectorId) {
        return PlatformResult.success(userQueryService.selectorVO(selectorId.getSelectorId()));
    }

    /**
     * 甄选师分页
     *
     * <p>保留旧语义: 平台端排除已注销。</p>
     *
     * @param selectorQuery 甄选师查询
     * @return 甄选师分页
     */
    @PostMapping("selectorPage")
    public PlatformResult<Page<SelectorVO>> selectorPage(@RequestBody SelectorQuery selectorQuery) {
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            selectorQuery.setStateOver(AccountEnum.State.DESTROY.getCode());
        }
        return PlatformResult.success(userQueryService.selectorPage(selectorQuery));
    }

    /**
     * 甄选师修改等级
     *
     * <p>迁移补充: 旧实现走充血实体 {@code Selector.selectorLevelEdit(level)} 只写 {@code level} 单列,
     * 中台贫血模型下改为  按主键部分列更新。
     * 旧 {@code @Limit(code={1024,select_level}, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param command 等级改写入参
     * @return 空结果
     */
    @PostMapping("selectorLevelEdit")
    public PlatformResult<Void> selectorLevelEdit(@Validated @RequestBody SelectorCmd.SelectorLevelEdit command) {
        operatorClientDomain.selectorLevelEdit(command.getId(), command.getLevel());
        return PlatformResult.success();
    }

    /**
     * 甄选师详情 for 上级
     *
     * <p>保留旧鉴权语义: 目标甄选师的上级不是当前登录账号时抛参数异常。</p>
     *
     * @param id 甄选师账号ID
     * @return 甄选师视图
     */
    @GetMapping("subSelector")
    public PlatformResult<SelectorVO> subSelector(@RequestParam("id") Long id) {
        SelectorVO selector = userQueryService.selectorVO(id);
        if (!SecurityUtils.getAccountId().equals(selector.getInviteId())) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(selector);
    }

    /**
     * 甄选师详情
     *
     * @param id 甄选师账号ID, 不传取当前登录账号
     * @return 甄选师视图
     */
    @GetMapping("selector")
    public PlatformResult<SelectorVO> selector(@RequestParam(value = "id", required = false) Long id) {
        Long selectorId = id == null ? SecurityUtils.getAccountId() : id;
        return PlatformResult.success(userQueryService.selectorVO(selectorId));
    }

    /**
     * 甄选师分页 for 上级
     *
     * <p>迁移补充: 旧实现在有实名信息时用 {@code realname} 覆盖列表展示名, 中台 {@code SelectorVO}
     * 无 {@code realname} 字段 (实名信息收敛为 {@code nameAuthVO} 字符串), 该覆盖未迁移,
     * 见迁移报告「能力缺失」。</p>
     *
     * @param selectorQuery 甄选师查询
     * @return 甄选师分页
     */
    @PostMapping("subSelectorPage")
    public PlatformResult<Page<SelectorVO>> subSelectorPage(@RequestBody SelectorQuery selectorQuery) {
        selectorQuery.setInviteId(SecurityUtils.getAccountId());
        return PlatformResult.success(userQueryService.selectorPage(selectorQuery));
    }

    /**
     * 甄选师代理注册
     *
     * <p>迁移补充: 旧 {@code AbsRolePolicyFactory.getPolicy(SELECTOR).proxyRegister(JSON)} 中台化后为
     * {@link AbsIdentityPolicySupport#getPolicy} 取策略再 {@code proxyRegister};
     * 为不改前端契约, 入参仍收旧 {@code SelectorProxySaveReq}, action 内按同名属性转换。
     * 旧接口声明 {@code ScmResult<Long>} 但实际回空, 此处逐字保留旧行为。</p>
     *
     * @param selectorProxySaveReq 甄选师代理注册请求
     * @return 空结果
     */
    @PostMapping("selectorRegister")
    public PlatformResult<Long> selectorProxyRegister(@Validated @RequestBody SelectorProxySaveReq selectorProxySaveReq) {
        IdentityProxySaveReq proxySaveReq = TransferUtils.transfer(selectorProxySaveReq, IdentityProxySaveReq::new);
        AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.SELECTOR).proxyRegister(proxySaveReq);
        return PlatformResult.success();
    }
}
