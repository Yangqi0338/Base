package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CdkRepository;
import com.newzkl.platform.base.biz.account.domain.service.CdkDomain;
import com.newzkl.platform.base.biz.account.model.assembler.CdkAssembler;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkEditReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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
 * 开通码领域服务实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.ICdkDomainImpl}。
 * 旧 {@code ThrowsException.exception(...)} 一律换为 {@link ScmException};
 * 旧 {@code CommonEnum.Switch} 在中台通用层为 {@code CommonEnum.YesOrNo} (码值 0/1 一致)。</p>
 *
 * @author KC
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CdkDomainImpl implements CdkDomain {

    /**
     * 单次随机生成开通码的数量上限 (旧实现硬编码 1000)。
     */
    private static final int MAX_CREATE_COUNT = 1000;

    /**
     * 开通码值长度 (旧实现硬编码 12)。
     */
    private static final int CDK_VALUE_LENGTH = 12;

    private final CdkRepository cdkRepository;
    private final CdkAssembler assembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CdkReq req) {
        CdkVO item = assembler.req2VO(req);
        item.setId(SnowflakeIdAble.getSnowflakeId());
        return cdkRepository.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Long id, CdkReq req) {
        CdkVO item = assembler.req2VO(req);
        item.setId(id);
        return cdkRepository.edit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return cdkRepository.delete(idList);
    }

    @Override
    public CdkVO cdk(Long id) {
        return cdkRepository.detail(id);
    }

    @Override
    public CdkRes detail(Long id) {
        return assembler.vo2Res(cdkRepository.detail(id));
    }

    @Override
    public Page<CdkRes> pageList(CdkQuery query) {
        return TransferUtils.transferPage(cdkRepository.pageList(query), assembler::vo2Res);
    }

    @Override
    public List<Long> idByQuery(CdkQuery query) {
        List<Long> idList = cdkRepository.idByQuery(query);
        return idList == null ? new ArrayList<>() : idList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> randomCreateCdk(Long belowId, Integer number, Integer systemType) {
        if (number == null || number > MAX_CREATE_COUNT) {
            throw new ScmException(BaseErrorCode.PARAM, "一次最多生成" + MAX_CREATE_COUNT + "个");
        }
        Set<String> valueSet = new HashSet<>();
        createCdkValue(valueSet, systemType, number);

        List<String> cdkValueList = new ArrayList<>();
        List<CdkVO> cdkList = new ArrayList<>();
        for (String value : valueSet) {
            CdkVO cdk = new CdkVO();
            cdk.setId(SnowflakeIdAble.getSnowflakeId());
            cdk.setValue(value);
            cdk.setOperatorId(belowId);
            cdk.setBelowRole(RoleEnum.CompanyRole.OPERATOR.getCode());
            cdk.setSystemType(systemType);
            cdkValueList.add(value);
            cdkList.add(cdk);
        }
        cdkRepository.saveBatch(cdkList);
        return cdkValueList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> directCreateCdk(Long belowId, Integer systemType, List<String> valueList) {
        List<Long> cdkIdList = new ArrayList<>();
        List<CdkVO> cdkList = new ArrayList<>();
        if (CollUtil.isEmpty(valueList)) {
            return cdkIdList;
        }
        for (String value : valueList) {
            CdkVO cdk = new CdkVO();
            cdk.setId(SnowflakeIdAble.getSnowflakeId());
            cdk.setValue(value);
            cdk.setOperatorId(belowId);
            cdk.setBelowRole(RoleEnum.CompanyRole.OPERATOR.getCode());
            cdk.setSystemType(systemType);
            cdkList.add(cdk);
            cdkIdList.add(cdk.getId());
        }
        cdkRepository.saveBatch(cdkList);
        return cdkIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int toCdk(ToCdkCommand command) {
        CdkEditReq cdkEdit = new CdkEditReq();
        Long fromRole = command.getFromRole();
        Long toRole = command.getToRole();
        if (RoleEnum.CompanyRole.OPERATOR.getCode().equals(fromRole)) {
            if (RoleEnum.CompanyRole.DEALER.getCode().equals(toRole)) {
                cdkEdit.setBelowRole(RoleEnum.CompanyRole.DEALER.getCode());
                cdkEdit.setToState(1);
                cdkEdit.setDealerId(command.getToUserId());
                cdkEdit.setToDealerTime(LocalDateTime.now());
            } else if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(toRole)) {
                cdkEdit.setBelowRole(RoleEnum.CompanyRole.CHANNEL.getCode());
                cdkEdit.setToState(1);
                cdkEdit.setChannelId(command.getToUserId());
                cdkEdit.setToChannelTime(LocalDateTime.now());
            } else {
                throw new ScmException(BaseErrorCode.PARAM, "被分配人角色");
            }
        } else if (RoleEnum.CompanyRole.DEALER.getCode().equals(fromRole)) {
            if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(toRole)) {
                cdkEdit.setBelowRole(RoleEnum.CompanyRole.CHANNEL.getCode());
                cdkEdit.setToState(2);
                cdkEdit.setChannelId(command.getToUserId());
                cdkEdit.setToChannelTime(LocalDateTime.now());
            } else {
                throw new ScmException(BaseErrorCode.PARAM, "被分配人角色");
            }
        } else {
            throw new ScmException(BaseErrorCode.PARAM, "分配人角色");
        }
        return cdkRepository.editForToCdk(cdkEdit, command.getCdkIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cdkStateEdit(Long id, Integer useState) {
        CdkVO cdk = cdkRepository.detail(id);
        if (cdk == null) {
            throw new ScmException(BaseErrorCode.NODATA, "开通码");
        }
        if (CommonEnum.YesOrNo.NO.getCode().equals(useState)
                && CommonEnum.YesOrNo.NO.getCode().equals(cdk.getUseType())) {
            throw new ScmException(BaseErrorCode.PARAM, "用户已使用的兑换,不能修改为未使用");
        }
        CdkVO cdkEdit = new CdkVO();
        cdkEdit.setId(id);
        cdkEdit.setUseState(useState);
        cdkRepository.edit(cdkEdit);
    }

    /**
     * 递归补足指定数量的、库内不重复的开通码值。
     *
     * <p>保留旧实现语义: 一次生成 (缺口数量) 个随机值, 剔除库内同系统类型已存在的值, 不足则递归再生成。</p>
     *
     * @param valueSet   结果集合 (原地累积)
     * @param systemType 系统类型
     * @param number     目标数量
     */
    private void createCdkValue(Set<String> valueSet, Integer systemType, Integer number) {
        if (valueSet.size() == number) {
            return;
        }
        if (!valueSet.isEmpty()) {
            log.warn("createCdkValue : 发生递归");
        }
        Set<String> candidates = ScmUtil.generateDiffCode(CDK_VALUE_LENGTH, number - valueSet.size());
        Set<String> existValue = cdkRepository.existValue(systemType, candidates);
        if (CollUtil.isNotEmpty(existValue)) {
            candidates.removeAll(existValue);
        }
        valueSet.addAll(candidates);
        createCdkValue(valueSet, systemType, number);
    }
}
