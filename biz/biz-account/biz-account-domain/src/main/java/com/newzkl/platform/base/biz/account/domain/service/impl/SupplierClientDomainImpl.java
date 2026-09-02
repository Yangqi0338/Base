package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierAuditRes;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.CompanyInfoVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.SupplierAssembler;
import com.newzkl.platform.base.common.ddd.model.constant.SupplierErrorCode;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@RequiredArgsConstructor
public class SupplierClientDomainImpl implements SupplierClientDomain {

    /**
     * 供应商可经营的最大行业数 (旧 {@code SupplierEnum.maxIndustryNum})
     */
    private static final int MAX_INDUSTRY_NUM = 5;

    private final SupplierRepository supplierRepository;
    private final SupplierAssembler supplierAssembler;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long supplierCustomSave(SupplierCustomSaveReq req) {
        if (req.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        SupplierVO item = TransferUtils.transfer(req, SupplierVO::new);
        item.setState(SupplierEnum.State.INIT);
        item.setAuditState(AuditEnum.State.CUSTOM);
        item.setPromisePayState(CommonEnum.YesOrNo.NO);
        item.setPromisePayAuditState(AuditEnum.State.CUSTOM);
        item.setPeriodSetState(CommonEnum.YesOrNo.NO);
        // 前期固定5000
        item.setShouldPromisePayAmount(Money.of("5000"));
        item.setPromisePayConfig(0);
        return supplierRepository.supplierSave(item);
    }

    @Override
    public int supplierEdit(Long id, SupplierReq supplierEditReq) {
        SupplierVO item = TransferUtils.transfer(supplierEditReq, SupplierVO::new, (c, v) -> {
            v.setId(id);
        });
        return supplierRepository.supplierEdit(item);
    }

    @Override
    public int supplierDelete(List<Long> supplierIdList) {
        return supplierRepository.supplierDelete(supplierIdList);
    }

    @Override
    public void supplierEdit(List<EditColumnVO> editColumnList, Long id) {
        supplierRepository.supplierEdit(editColumnList, id);
    }

    @Override
    public SupplierVO supplier(Long supplierId) {
        SupplierVO supplier = supplierRepository.supplier(supplierId);
        return supplier;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void supplierSubmitAudit(Long accountId, CompanyInfoVO companyInfo) {
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        SupplierVO current = supplierRepository.supplier(accountId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "供应商");
        }
        // 幂等: 仅初始态可提交, 已在审/已入驻不允许重复提交
        // (登录回填的 supplierState 取自 state, 故提交后同步推进 state)
        if (current.getState() != SupplierEnum.State.INIT) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        SupplierVO item = new SupplierVO();
        item.setId(accountId);
        item.setCompanyInfo(companyInfo);
        item.setState(SupplierEnum.State.AUDITING);
        item.setAuditState(AuditEnum.State.AUDITING);
        supplierRepository.supplierEdit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void supplierAuditPass(Long accountId) {
        SupplierVO current = supplierRepository.supplier(accountId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "供应商");
        }
        // 仅审核中的供应商可通过, 防越态/重复通过
        if (current.getState() != SupplierEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        CompanyInfoVO companyInfo = current.getCompanyInfo();
        if (companyInfo == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "companyInfo不能为空");
        }
        //组装数据
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        supplier.setName(companyInfo.getCompanyName());
        supplier.setIndustryIdList(companyInfo.getManageIndustryIdList());
        supplier.setState(SupplierEnum.State.NORMAL);
        supplier.setAuditState(AuditEnum.State.SUCCESS);
        supplier.setCompanyInfo(companyInfo);
        supplier.setPromisePayState(CommonEnum.YesOrNo.NO);
        supplier.setInTime(DateUtil.toLocalDateTime(new Date()));
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditFail(Long accountId, String lastRefuseReason) {
        SupplierVO current = supplierRepository.supplier(accountId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "供应商");
        }
        // 仅审核中的供应商可拒绝
        if (current.getState() != SupplierEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        // 退回初始态以允许重新提交 (submit 判 state==INIT), auditState=FAIL 保留拒绝痕迹
        supplier.setState(SupplierEnum.State.INIT);
        supplier.setAuditState(AuditEnum.State.FAIL);
        supplier.setAuditRefuseReason(lastRefuseReason);
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    public void promisePaySubmitAudit(Long supplierId) {
        SupplierVO supplier = new SupplierVO();
        supplier.setId(supplierId);
        supplier.setPromisePayAuditState(AuditEnum.State.AUDITING);
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void promisePayAuditSuccess(Long accountId, Money promisePayAmount) {
        SupplierVO oldSupplier = supplierRepository.supplier(accountId);
        if (oldSupplier == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "供应商");
        }
        // 幂等: 已缴纳保证金则直接返回, 防重复入账
        if (oldSupplier.getPromisePayState() == CommonEnum.YesOrNo.YES) {
            return;
        }
        // 置已入驻 + 保证金已缴 + 保证金审核通过 + 实缴金额
        // (保证金流水与余额充值由 finance 域负责, 此处只改 account 主数据)
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        supplier.setState(SupplierEnum.State.NORMAL);
        supplier.setPromisePayState(CommonEnum.YesOrNo.YES);
        supplier.setPromisePayAuditState(AuditEnum.State.SUCCESS);
        supplier.setPromisePayAmount(promisePayAmount);
        supplierRepository.supplierEdit(supplier);
        // 短信通知供应商保证金审核通过: 见 rebuild/docs/planning/deferred-issues.md 短信通道
    }

    @Override
    public void promisePayAuditFail(Long accountId, String lastRefuseReason) {
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        supplier.setPromisePayAuditState(AuditEnum.State.FAIL);
        supplier.setAuditRefuseReason(lastRefuseReason);
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    public Money limitAmount(Long accountId) {
        SettlementConfigVO settlementConfig = supplierRepository.getSettlementConfig(accountId);
        return Opt.ofNullable(settlementConfig).map(SettlementConfigVO::getLimitAmount).orElse(Money.nullVal());
    }

    @Override
    public Page<SupplierRes> supplierPage(SupplierQuery supplierQuery) {
        Page<SupplierAccountVO> supplierPageList = supplierRepository.pageListWithAccount(supplierQuery);

        // 查account数据
        return TransferUtils.transferPage(supplierPageList, supplierAssembler::accountVO2Res);
    }

    @Override
    public Page<SupplierAuditRes> supplierAuditPage(SupplierQuery supplierQuery) {
        Page<SupplierAccountVO> supplierPageList = supplierRepository.pageListWithAccount(supplierQuery);
        return TransferUtils.transferPage(supplierPageList, SupplierAuditRes.class);
    }

    @Override
    public SupplierAuditRes supplierAuditDetail(Long supplierId) {
        SupplierVO supplier = supplierRepository.supplier(supplierId);
        if (supplier == null) {
            return null;
        }
        return TransferUtils.transfer(supplier, SupplierAuditRes.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void periodSet(Long id, SettlementConfigVO periodSetConfig) {
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setPeriodSetState(CommonEnum.YesOrNo.YES);
        item.setPeriodSetConfig(periodSetConfig);
        supplierRepository.supplierEdit(item);
        tripInState(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shouldPromisePayAmountSet(Long id, Money shouldPromisePayAmount, Integer promisePayConfig) {
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setShouldPromisePayAmount(shouldPromisePayAmount);
        item.setPromisePayConfig(promisePayConfig);
        supplierRepository.supplierEdit(item);
        tripInState(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addIndustry(Long id, List<Long> industryIdList) {
        SupplierVO current = supplierRepository.supplier(id);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "供应商");
        }
        List<Long> merged = new ArrayList<>(toLongList(current.getIndustryIdList()));
        if (CollUtil.isNotEmpty(industryIdList)) {
            merged.addAll(industryIdList);
        }
        List<Long> distinct = merged.stream().distinct().collect(Collectors.toList());
        // 保留旧限制: 供应商本人操作时受最大行业数约束, 平台角色不限
        if (AccountEnum.Identity.SUPPLIER == SecurityUtils.getIdentity()
                && distinct.size() > MAX_INDUSTRY_NUM) {
            throw new PlatformException(SupplierErrorCode.OVER_INDUSTRY);
        }
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setIndustryIdList(CollUtil.join(distinct, ","));
        supplierRepository.supplierEdit(item);
    }

    /**
     * 触发入驻判定
     *
     * <p>逐字保留旧 {@code Supplier#tripInState}: 账期已设置的前提下, 保证金已缴纳
     * 或缴纳配置为延迟(1)时把供应商状态置为已入驻; 其余情形不动状态。</p>
     *
     * @param id 供应商账号ID
     */
    private void tripInState(Long id) {
        SupplierVO current = supplierRepository.supplier(id);
        if (current == null || current.getPeriodSetState() != CommonEnum.YesOrNo.YES) {
            return;
        }
        boolean promisePaid = current.getPromisePayState() == CommonEnum.YesOrNo.YES;
        boolean delayConfig = Objects.equals(1, current.getPromisePayConfig());
        if (!promisePaid && !delayConfig) {
            return;
        }
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setState(SupplierEnum.State.NORMAL);
        supplierRepository.supplierEdit(item);
    }

    /**
     * 逗号分隔的 ID 串转 ID 列表
     *
     * <p>等价旧 {@code ScmUtil.stringToLongList}: 空串或含 "null" 片段时返回空列表。</p>
     *
     * @param source 逗号分隔的 ID 串
     * @return ID 列表, 无则空列表
     */
    private List<Long> toLongList(String source) {
        if (StrUtil.isEmpty(source) || source.contains("null")) {
            return new ArrayList<>();
        }
        return StrUtil.split(source, ',').stream()
                .map(String::trim)
                .filter(StrUtil::isNotEmpty)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
