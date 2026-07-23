package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.SupplierEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
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
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@RequiredArgsConstructor
public class SupplierClientDomainImpl implements SupplierClientDomain {

    private final SupplierRepository supplierRepository;
    private final SupplierAssembler supplierAssembler;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long supplierCustomSave(SupplierCustomSaveReq req) {
        if (req.getId() == null) {
            throw new ScmException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        SupplierVO item = TransferUtils.transfer(req, SupplierVO::new, (c, v) -> {
            v.setGoodsDealCount(0);
            v.setGoodsNotSaleCount(0);
            v.setGoodsOnSaleCount(0);
            v.setGoodsSaleAmount(0);
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
            throw new ScmException(BaseErrorCode.PARAM, "companyInfo不能为空");
        }
        String manageIndustryIdListString = jsonObject.getString("manageIndustryIdList");
        List<String> manageIndustryIdList = JSONUtil.toList(manageIndustryIdListString, String.class);
        JSONArray companyAreaCode = jsonObject.getJSONArray("companyAreaCode");
        //组装数据
        SupplierVO supplier = new SupplierVO();
        supplier.setId(accountVO.getId());
        supplier.setName(jsonObject.getString("companyName"));
//        supplier.setCompanyAreaCode(StrUtil.toString(CollUtil.getLast(companyAreaCode)));
        supplier.setIndustryIdList(ScmUtil.stringListToString(manageIndustryIdList));
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
//            throw new ScmException(BaseErrorCode.PARAM, "account_id");
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
}
