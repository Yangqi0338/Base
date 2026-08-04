package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.model.req.DealerCmd;
import com.newzkl.platform.base.biz.account.model.req.DealerEditReq;
import com.newzkl.platform.base.biz.account.model.req.DealerQuery;
import com.newzkl.platform.base.biz.account.model.req.web.DealerProxySaveReq;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-交易师
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.DealerController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code dealer} / {@code dealerPage})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final OperatorClientDomain operatorClientDomain;
    private final UserQueryService userQueryService;

    /**
     * 市场交易师修改
     *
     * <p>保留旧鉴权语义: 平台端必须显式传 ID; 交易师端强制改写为当前登录账号; 其余角色抛参数异常。</p>
     *
     * @param edit 交易师修改请求
     * @return 空结果
     */
    @PostMapping("dealerEdit")
    public PlatformResult<Void> dealerEdit(@Validated @RequestBody DealerEditReq edit) {
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            if (edit.getId() == null) {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        } else if (RoleEnum.CompanyRole.DEALER.getCode().equals(SecurityUtils.getRoleId())) {
            edit.setId(SecurityUtils.getAccountId());
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        operatorClientDomain.dealerEdit(edit.getId(), edit);
        return PlatformResult.success();
    }

    /**
     * 市场交易师分页
     *
     * <p>保留旧语义: 平台端排除已注销。</p>
     *
     * @param dealerQuery 交易师查询
     * @return 交易师分页
     */
    @PostMapping("dealerPage")
    public PlatformResult<Page<DealerVO>> dealerPage(@RequestBody DealerQuery dealerQuery) {
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            dealerQuery.setStateOver(AccountEnum.State.DESTROY.getCode());
        }
        return PlatformResult.success(userQueryService.dealerPage(dealerQuery));
    }

    /**
     * 市场交易师代理注册
     *
     * <p>迁移补充: 旧 {@code AbsRolePolicyFactory.getPolicy(DEALER).proxyRegister(JSON)} 中台化后为
     * {@link AbsIdentityPolicySupport#getPolicy} 取策略再 {@code proxyRegister},
     * 入参由 String JSON 改为强类型 {@code IdentityProxySaveReq}; 为不改前端契约,
     * 入参仍收旧 {@code DealerProxySaveReq}, action 内按同名属性转换。
     * 旧接口声明 {@code ScmResult<Long>} 但实际回空, 此处逐字保留旧行为。</p>
     *
     * @param dealerProxySaveReq 交易师代理注册请求
     * @return 空结果
     */
    @PostMapping("dealerRegister")
    public PlatformResult<Long> dealerProxyRegister(@Validated @RequestBody DealerProxySaveReq dealerProxySaveReq) {
        IdentityProxySaveReq proxySaveReq = TransferUtils.transfer(dealerProxySaveReq, IdentityProxySaveReq::new);
        AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.DEALER).proxyRegister(proxySaveReq);
        return PlatformResult.success();
    }

    /**
     * 服务费修改
     *
     * <p>迁移补充: 旧实现走充血实体 {@code Dealer.serviceFeeConfigEdit(serviceRate)} 只写
     * {@code service_rate} 单列, 中台贫血模型下改为
     * {@link OperatorClientDomain#dealerServiceFeeConfigEdit} 按主键部分列更新。
     * 旧接口无鉴权注解, 平台侧可对任意 {@code accountId} 改费率, 此风险按原样保留</p>
     *
     * @param serviceFeeConfigEdit 服务费率修改入参
     * @return 空结果
     */
    @PostMapping("serviceFeeConfigEdit")
    public PlatformResult<Void> serviceFeeConfigEdit(@Validated @RequestBody DealerCmd.ServiceFeeConfigEdit serviceFeeConfigEdit) {
        operatorClientDomain.dealerServiceFeeConfigEdit(serviceFeeConfigEdit.getAccountId(), serviceFeeConfigEdit.getServiceRate());
        return PlatformResult.success();
    }

    /**
     * 市场交易师详情 for 平台
     *
     * @param idListCommand 交易师ID入参
     * @return 交易师视图
     */
    @PostMapping("dealerForAdmin")
    public PlatformResult<DealerVO> dealer(@RequestBody IdListCommand idListCommand) {
        return PlatformResult.success(userQueryService.dealerVO(CollUtil.getFirst(idListCommand.getIdList())));
    }

    /**
     * 市场交易师详情
     *
     * @return 交易师视图
     */
    @GetMapping("dealer")
    public PlatformResult<DealerVO> dealer() {
        return PlatformResult.success(userQueryService.dealerVO(SecurityUtils.getAccountId()));
    }
}
