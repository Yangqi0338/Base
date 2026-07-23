package com.newzkl.platform.base.biz.goods.domain.virtualSpu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.virtualSpu.repository.ICdkRepository;
import com.newzkl.platform.base.biz.goods.domain.virtualSpu.service.ICdkDomain;
import com.newzkl.platform.base.biz.goods.model.goods.dto.virtualSpu.CdkDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu.CdkQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu.BuyCreateCdkReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu.ToCdkCommand;
import com.newzkl.platform.base.biz.goods.model.goods.vo.virtualSpu.CdkVO;
import com.newzkl.platform.base.biz.goods.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.goods.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ICdkDomainImpl implements ICdkDomain {

    private final ICdkRepository cdkRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> randomCreateCdk(Long belowId, Integer number, Integer systemType) {
        List<String> cdkValueList = new ArrayList<>();
        List<CdkDTO> cdkDTOList = new ArrayList<>();
        HashSet<String> valueList = new HashSet<>();
        if (number > 1000) {
            throw new ScmException(BaseErrorCode.PARAM, "一次最多生成1000个");
        }
        //递归获取兑换码
        createCdkValue(valueList, cdkRepository, systemType, 12, number);
        for (String value : valueList) {
            CdkDTO cdkDTO = new CdkDTO();
            cdkDTO.setId(SnowflakeIdAble.getSnowflakeId());
            cdkDTO.setValue(value);
            cdkDTO.setOperatorId(belowId);
            cdkDTO.setBelowRole(RoleEnum.CompanyRole.OPERATOR);
            cdkDTO.setSystemType(systemType);
            cdkValueList.add(value);
            cdkDTOList.add(cdkDTO);
        }
        cdkRepository.cdkSaveBatch(cdkDTOList);
        return cdkValueList;
    }

    private void createCdkValue(Set<String> objects, ICdkRepository cdkRepository, Integer systemType, int length, Integer number) {
        if (objects.size() == number) {
            return;
        }
        if (!objects.isEmpty()) {
            log.warn("createCdkValue : 发生递归");
        }
        Set<String> valueList = ScmUtil.generateDiffCode(length, number - objects.size());
        Set<String> existValue = cdkRepository.existValue(systemType, valueList);
        if (!existValue.isEmpty()) {
            valueList.removeAll(existValue);
        }
        objects.addAll(valueList);
        createCdkValue(objects, cdkRepository, systemType, 12, number);
    }

    @Override
    public void cdkStateEdit(Long id, Integer useState) {
        CdkDTO cdkDTO = cdkRepository.cdk(id);
        if (CommonEnum.YesOrNo.NO.getCode().equals(useState)) {
            if (cdkDTO.getUseType() == 0) {
                throw new ScmException(BaseErrorCode.PARAM, "用户已使用的兑换,不能修改为未使用");
            }
        }
        CdkDTO cdkDTOEdit = new CdkDTO();
        cdkDTOEdit.setId(id);
        cdkDTOEdit.setUseState(useState);
        cdkRepository.cdkEdit(cdkDTOEdit);
    }

    @Override
    public void buyCreateCdk(BuyCreateCdkReq buyCreateCdkReq) {
        List<CdkDTO> cdkList = buildCdk(buyCreateCdkReq);
        cdkRepository.cdkSaveBatch(cdkList);
    }

    @Override
    public Page<CdkVO> cdkPage(CdkQuery cdkQuery) {
        return cdkRepository.cdkPage(cdkQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createCdk() {
        BuyCreateCdkReq buyCreateCdkReq = new BuyCreateCdkReq();
        buyCreateCdkReq.setOrderId(0L);
        buyCreateCdkReq.setGetType(0);
        buyCreateCdkReq.setSystemType(0);
        buyCreateCdkReq.setRole(RoleEnum.CompanyRole.PLATFORM);
        buyCreateCdkReq.setCount(10);
        buyCreateCdkReq.setAccountId(0L);
        buyCreateCdkReq.setToState(0);
        List<CdkDTO> cdkDTOList = buildCdk(buyCreateCdkReq);
        cdkRepository.cdkSaveBatch(cdkDTOList);
    }

    private List<CdkDTO> buildCdk(BuyCreateCdkReq buyCreateCdkReq) {
        List<CdkDTO> cdkDTOList = new ArrayList<>();
        HashSet<String> valueList = new HashSet<>();
        //递归获取兑换码
        createCdkValue(valueList, cdkRepository, buyCreateCdkReq.getSystemType(), 12, buyCreateCdkReq.getCount());
        for (String value : valueList) {
            CdkDTO cdkDTO = new CdkDTO();
            cdkDTO.setId(SnowflakeIdAble.getSnowflakeId());
            cdkDTO.setValue(value);
            cdkDTO.setBelowRole(buyCreateCdkReq.getRole());
            cdkDTO.setSystemType(buyCreateCdkReq.getSystemType());
            cdkDTO.setGetType(buyCreateCdkReq.getGetType());
            cdkDTO.setOrderId(buyCreateCdkReq.getOrderId());
            cdkDTO.setToState(buyCreateCdkReq.getToState());
            if (RoleEnum.CompanyRole.OPERATOR == buyCreateCdkReq.getRole()) {
                cdkDTO.setOperatorId(buyCreateCdkReq.getAccountId());
                cdkDTO.setToOperatorTime(LocalDateTime.now());
            } else if (RoleEnum.CompanyRole.DEALER == buyCreateCdkReq.getRole()) {
                cdkDTO.setDealerId(buyCreateCdkReq.getAccountId());
                cdkDTO.setToDealerTime(LocalDateTime.now());
            } else if (RoleEnum.CompanyRole.CHANNEL == buyCreateCdkReq.getRole()) {
                cdkDTO.setChannelId(buyCreateCdkReq.getAccountId());
                cdkDTO.setToChannelTime(LocalDateTime.now());
            }
            cdkDTOList.add(cdkDTO);
        }
        return cdkDTOList;
    }

    @Override
    public int toCdk(ToCdkCommand toCdkCommand) {
        CdkDTO cdkDTOEdit = new CdkDTO();
        if (RoleEnum.CompanyRole.OPERATOR == toCdkCommand.getFromRole()) {
            if (RoleEnum.CompanyRole.DEALER == toCdkCommand.getToRole()) {
                cdkDTOEdit.setBelowRole(RoleEnum.CompanyRole.DEALER);
                cdkDTOEdit.setToState(1);
                cdkDTOEdit.setDealerId(toCdkCommand.getToUserId());
                cdkDTOEdit.setToDealerTime(LocalDateTime.now());
            } else if (RoleEnum.CompanyRole.CHANNEL == toCdkCommand.getToRole()) {
                cdkDTOEdit.setBelowRole(RoleEnum.CompanyRole.CHANNEL);
                cdkDTOEdit.setToState(1);
                cdkDTOEdit.setChannelId(toCdkCommand.getToUserId());
                cdkDTOEdit.setToChannelTime(LocalDateTime.now());
            }else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        } else if (RoleEnum.CompanyRole.DEALER == toCdkCommand.getFromRole()) {
            if (RoleEnum.CompanyRole.CHANNEL == toCdkCommand.getToRole()) {
                cdkDTOEdit.setBelowRole(RoleEnum.CompanyRole.CHANNEL);
                cdkDTOEdit.setToState(2);
                cdkDTOEdit.setChannelId(toCdkCommand.getToUserId());
                cdkDTOEdit.setToChannelTime(LocalDateTime.now());
            }else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        }else {
            throw new ScmException(BaseErrorCode.PARAM);
        }
        return cdkRepository.cdkEditForToCdk(cdkDTOEdit,toCdkCommand.getCdkIdList());
    }

}
