package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.SupplierAssembler;
import com.newzkl.platform.base.biz.account.model.exception.SupplierErrorCode;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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
        SupplierVO item = TransferUtils.transfer(req, SupplierVO::new, (c, v) -> {
            v.setGoodsDealCount(0);
            v.setGoodsNotSaleCount(0);
            v.setGoodsOnSaleCount(0);
            v.setGoodsSaleAmount(Money.ZERO);
            v.setGoodsSaleCount(0);
            v.setGoodsTotalCount(0);
        });
        item.setState(SupplierEnum.State.INIT);
        item.setAuditState(AuditEnum.State.CUSTOM);
        item.setPromisePayState(CommonEnum.YesOrNo.NO);
        item.setPromisePayAuditState(AuditEnum.State.CUSTOM);
        item.setPeriodSetState(CommonEnum.YesOrNo.NO);
        // 前期固定5000
//        item.setShouldPromisePayAmount(500000);
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
    public void auditPass(AccountVO accountVO, String companyInfo) {
        //从companyInfo取出行业ID并赋值
        JSONObject jsonObject = JSONObject.parseObject(companyInfo);
        if (jsonObject == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "companyInfo不能为空");
        }
        String manageIndustryIdListString = jsonObject.getString("manageIndustryIdList");
        List<String> manageIndustryIdList = JSONUtil.toList(manageIndustryIdListString, String.class);
        JSONArray companyAreaCode = jsonObject.getJSONArray("companyAreaCode");
        //组装数据
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountVO.getId());
        supplier.setName(jsonObject.getString("companyName"));
//        supplier.setCompanyAreaCode(StrUtil.toString(CollUtil.getLast(companyAreaCode)));
        supplier.setIndustryIdList(BizUtil.stringListToString(manageIndustryIdList));
        supplier.setState(SupplierEnum.State.NORMAL);
        supplier.setAuditState(AuditEnum.State.SUCCESS);
        supplier.setCompanyInfo(companyInfo);
        supplier.setPromisePayState(CommonEnum.YesOrNo.NO);
        supplier.setInTime(DateUtil.toLocalDateTime(new Date()));
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    public void auditFail(Long accountId, String lastRefuseReason) {
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        supplier.setAuditState(AuditEnum.State.FAIL);
        supplier.setAuditRefuseReason(lastRefuseReason);
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    public void promiseFlowSubmitAuditSuccess(Long supplierId) {
        SupplierVO supplier = new SupplierVO();
        supplier.setId(supplierId);
        supplier.setPromisePayAuditState(AuditEnum.State.AUDITING);
        supplierRepository.supplierEdit(supplier);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void promisePayAuditSuccess(PromiseFlowVO promiseFlowVO) {
//        SupplierRes oldSupplier = supplierRepository.supplier(promiseFlowVO.getAccountId());
//        // 幂等
//        if (oldSupplier.getPromisePayState() == CommonEnum.YesOrNo.YES) {
//            return;
//        }
//        //修改供应商
//        SupplierRes supplier = new SupplierRes();
//        if (promiseFlowVO.getAccountId() == null) {
//            throw new PlatformException(BaseErrorCode.PARAM, "account_id");
//        }
//        supplier.setId(promiseFlowVO.getAccountId());
//        supplier.setState(2);
//        supplier.setPromisePayState(CommonEnum.YesOrNo.YES);
//        supplier.setPromisePayAuditState(AuditEnum.State.SUCCESS.getCode());
//        supplier.setPromisePayAmount(promiseFlowVO.getAmount());
//        supplierRepository.supplierEdit(supplier); // 设置 state=2、promisePayState=ON、promisePayAuditState=SUCCESS、promisePayAmount=审核通过金额。
//        //保存保证金缴纳流水
//        PromiseFlow promiseFlow = SupplierUtil.promiseFlowVO2promiseFlow(promiseFlowVO);
//        promiseFlow.setId(SnowflakeIdAble.getSnowflakeId());
//        supplierRepository.promiseFlowSave(promiseFlow); // 保存保证金缴纳流水
//        //短信通知
//        CodeReq codeReq = new CodeReq();
//        codeReq.setType(SmsEnum.Type.PROMISE_SUCCESS.getCode());
//        codeReq.setPhone(mobile);
//        CodeReq codeReq = SupplierUtil.getPromisePayAuditSuccessNotifyReq(oldSupplier.getUsername()); //发送短信通知供应商
//        supplierRepository.smsNotify(codeReq);
//    }

    @Override
    public void promisePayAuditFail(Long accountId, String lastRefuseReason) {
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountId);
        supplier.setPromisePayAuditState(AuditEnum.State.FAIL);
        supplier.setAuditRefuseReason(lastRefuseReason);
        supplierRepository.supplierEdit(supplier);
    }

    @Override
    public Integer limitAmount(Long accountId) {
        String settlementConfig = supplierRepository.getSettlementConfig(accountId);
        if (StrUtil.isEmpty(settlementConfig)) {
            return 0;
        } else {
            JSONObject jsonObject = JSONObject.parseObject(settlementConfig);
            Integer limitAmount = jsonObject.getInteger("limitAmount");
            return limitAmount == null ? 0 : limitAmount;
        }
    }

    @Override
    public Page<SupplierRes> supplierPage(SupplierQuery supplierQuery) {
        Page<SupplierAccountVO> supplierPageList = supplierRepository.pageListWithAccount(supplierQuery);

        // 查account数据
        return TransferUtils.transferPage(supplierPageList, supplierAssembler::accountVO2Res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void periodSet(Long id, String periodSetConfig) {
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setPeriodSetState(CommonEnum.YesOrNo.YES);
        item.setPeriodSetConfig(periodSetConfig);
        supplierRepository.supplierEdit(item);
        tripInState(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shouldPromisePayAmountSet(Long id, Integer shouldPromisePayAmount, Integer promisePayConfig) {
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setShouldPromisePayAmount(Money.of(shouldPromisePayAmount));
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
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(SecurityUtils.getRoleId())
                && distinct.size() > MAX_INDUSTRY_NUM) {
            throw new PlatformException(SupplierErrorCode.OVER_INDUSTRY);
        }
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setIndustryIdList(CollUtil.join(distinct, ","));
        supplierRepository.supplierEdit(item);
    }

    @Override
    public void supplierInviteIdEdit(Long id, Long inviteId) {
        SupplierVO item = new SupplierVO();
        item.setId(id);
        item.setInviteId(inviteId);
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
