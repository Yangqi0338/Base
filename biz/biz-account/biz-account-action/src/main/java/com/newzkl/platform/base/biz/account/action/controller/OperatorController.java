package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.net.InternetDomainName;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.OperatorCmd;
import com.newzkl.platform.base.biz.account.model.req.OperatorQuery;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户-运营商
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.OperatorController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code operator} / {@code operatorPage})。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/operator")
@RequiredArgsConstructor
public class OperatorController {

    private final UserQueryService userQueryService;
    private final IdentityService identityService;

    /**
     * 运营商修改
     *
     * <p>迁移补充: 旧 {@code IRoleService.operatorEdit(id, command)} 中台化后为
     * {@link IdentityService#operatorEdit}, 账号ID由 {@code OperatorReq.id} 承载。</p>
     *
     * @param operatorReq 运营商修改请求
     * @return 空结果
     */
    @PostMapping("operatorEdit")
    public PlatformResult<Void> operatorEdit(@Validated @RequestBody OperatorReq operatorReq) {
        identityService.operatorEdit(operatorReq);
        return PlatformResult.success();
    }

    /**
     * 运营商注册 for 平台
     *
     * <p>迁移补充: 旧 {@code AbsRolePolicyFactory.getPolicy(OPERATOR).proxyRegister(JSON)} 中台化后为
     * {@link AbsIdentityPolicySupport#getPolicy} 取策略再 {@code proxyRegister},
     * 入参由 String JSON 改为强类型 {@code IdentityProxySaveReq}; 为不改前端契约,
     * 入参仍收旧 {@code OperatorProxySaveReq}, action 内按同名属性转换。
     * 旧接口声明 {@code ScmResult<Long>} 但实际回空, 此处逐字保留旧行为 (不回传账号ID)。</p>
     *
     * @param operatorProxySaveReq 运营商代理注册请求
     * @return 空结果
     */
    @PostMapping("operatorRegisterForAdmin")
    public PlatformResult<Long> operatorRegister(@Validated @RequestBody OperatorProxySaveReq operatorProxySaveReq) {
        IdentityProxySaveReq proxySaveReq = TransferUtils.transfer(operatorProxySaveReq, IdentityProxySaveReq::new);
        AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.OPERATOR).proxyRegister(proxySaveReq);
        return PlatformResult.success();
    }

    /**
     * 运营商详情
     *
     * @param operatorId 运营商账号ID, 不传取当前登录账号
     * @return 运营商视图
     */
    @GetMapping("operator")
    public PlatformResult<OperatorVO> operator(@RequestParam(value = "id", required = false) Long operatorId) {
        Long id = operatorId == null ? SecurityUtils.getAccountId() : operatorId;
        return PlatformResult.success(userQueryService.operatorVO(id));
    }

    /**
     * 生成供应商注册邀请链接
     *
     * @param request HTTP 请求, 取 {@code HOST} 头定位运营商
     * @return 邀请链接
     */
    @GetMapping("supplierRegistrationInvitationLink")
    public PlatformResult<String> supplierRegistrationInvitationLink(HttpServletRequest request) {
        String headHost = request.getHeader("HOST");
        if (headHost == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "Header缺少HOST");
        }
        return PlatformResult.success(userQueryService.supplierRegistrationInvitationLink(headHost));
    }

    /**
     * 按注册域名获取运营商信息
     *
     * <p>保留旧语义: 去协议头后取首段前缀, 与一级域名拼成 {@code 前缀.y.一级域名};
     * 解析异常时回退为原始入参域名。旧代码中的域名格式校验本就整段注释, 未补齐。</p>
     *
     * @param registerDomain 注册域名
     * @return 含 {@code host} / {@code yqm} / {@code info} 的映射
     */
    @GetMapping("getInfoByDomain")
    public PlatformResult<Map<String, Object>> getInfoByDomain(String registerDomain) {
        log.info("registerDomain:{}", registerDomain);
        String fHost = registerDomain.replace("http://", "").replace("https://", "");
        String firstPointPrefix = getPrefix(fHost);
        String yHost;
        try {
            yHost = firstPointPrefix + "." + "y" + "." + toApex(fHost);
        } catch (Exception e) {
            log.info("域名错误", e);
            yHost = registerDomain;
        }
        OperatorVO operatorByDomain = userQueryService.getOperatorByDomain(yHost);
        if (ObjectUtil.isEmpty(operatorByDomain)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "域名错误，运营商不存在");
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("host", yHost);
        objectMap.put("yqm", operatorByDomain.getYqm());
        String info = operatorByDomain.getInfo();
        if (info != null) {
            try {
                objectMap.put("info", JSONObject.parseObject(info));
            } catch (Exception e) {
                log.error("info解析异常", e);
            }
        }
        return PlatformResult.success(objectMap);
    }

    /**
     * 取域名首段前缀
     *
     * @param domain 域名
     * @return 首段前缀, 入参为空时返回 null
     */
    public String getPrefix(String domain) {
        if (domain == null || domain.isEmpty()) {
            return null;
        }
        String[] parts = domain.split("\\.");
        return parts.length > 0 ? parts[0] : null;
    }

    /**
     * 取一级域名 (注册域, eTLD+1)
     *
     * <p>如 {@code a.b.example.co.uk} 返回 {@code example.co.uk}。</p>
     *
     * @param host 任意层级域名
     * @return 一级域名
     */
    public String toApex(String host) {
        return InternetDomainName.from(host).topPrivateDomain().toString();
    }

    /**
     * 运营商分页
     *
     * @param operatorQuery 运营商查询
     * @return 运营商分页
     */
    @PostMapping("operatorPage")
    public PlatformResult<Page<OperatorVO>> operatorPage(@RequestBody OperatorQuery operatorQuery) {
        return PlatformResult.success(userQueryService.operatorPage(operatorQuery));
    }

    /**
     * 服务费修改
     *
     * <p>迁移补充: 中台 {@code OperatorCmd.ServiceFeeConfigEdit} 的 {@code serviceFeeConfigVO}
     * 字段随资金域解耦已注释, 当前只能传账号ID, 见迁移报告「能力缺失」。</p>
     *
     * @param serviceFeeConfigEdit 服务费修改入参
     * @return 空结果
     */
    @PostMapping("serviceFeeConfigEdit")
    public PlatformResult<Void> serviceFeeConfigEdit(@RequestBody OperatorCmd.ServiceFeeConfigEdit serviceFeeConfigEdit) {
        identityService.serviceFeeConfigEdit(serviceFeeConfigEdit.getAccountId(), null);
        return PlatformResult.success();
    }

    /**
     * 获取运营商域名信息
     *
     * @return 运营商域名信息
     */
    @PostMapping("getOperatorDomainInfoBySub")
    public PlatformResult<OperatorDomainInfo> getOperatorDomainInfoBySub() {
        return PlatformResult.success(userQueryService.getOperatorDomainInfoBySub());
    }
}
