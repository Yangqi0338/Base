package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.model.support.OperatorConfigVO;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.enums.identity.OperatorEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.domain.repository.DealerRepository;
import com.newzkl.platform.base.biz.account.domain.repository.OperatorRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SelectorRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.req.web.DealerProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.SelectorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.OperatorAssembler;
// TODO[cross-domain relation]: import relation.req.ConditionReq;
// TODO[cross-domain relation]: import relation.req.TeamUserCountReq;
// TODO[cross-domain relation]: import relation.vo.TeamUserCountRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@RequiredArgsConstructor
public class OperatorClientDomainImpl implements OperatorClientDomain {

    private final OperatorRepository operatorRepository;
    private final DealerRepository dealerRepository;
    private final SelectorRepository selectorRepository;
    private final SupplierRepository supplierRepository;
    private final OperatorAssembler operatorAssembler;

    @Override
    public OperatorVO operatorEdit(OperatorReq operatorEditReq) {
        OperatorVO operatorVO = operatorAssembler.req2VO(operatorEditReq);
        doOperatorCheck(operatorVO);
        operatorRepository.operatorEdit(operatorVO);
        return operatorVO;
    }

    @Override
    public int operatorDelete(List<Long> operatorIdList) {
        return operatorRepository.operatorDelete(operatorIdList);
    }

    @Override
    public void operatorEdit(List<EditColumnVO> editColumnList, Long id) {
        operatorRepository.operatorEdit(editColumnList, id);
    }

    @Override
    public OperatorVO operator(Long operatorId) {
        OperatorVO operator = operatorRepository.operator(operatorId);
        return operator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperatorVO operatorProxySave(OperatorProxySaveReq operatorProxySaveReq, Long accountId) {
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }

        OperatorVO operatorVO = new OperatorVO();
//        operator.init(operatorProxySaveReq, accountId);
        doOperatorCheck(operatorVO);
        operatorRepository.operatorSave(operatorVO);
        return operatorVO;
    }

    @Override
    public OperatorVO operatorCustomSave(OperatorCustomSaveReq operatorEditReq, Long accountId) {
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }

        OperatorProxySaveReq operatorProxySaveReq = TransferUtils.transfer(operatorEditReq, OperatorProxySaveReq::new);
        operatorProxySaveReq.setType(OperatorEnum.Type.ORGANIZE);
        operatorProxySaveReq.setTypeForeignId(null);
        operatorProxySaveReq.setTypeForeignName("");
        OperatorVO operator = new OperatorVO();
        operator.init(operatorProxySaveReq, accountId);
        doOperatorCheck(operator);
        operatorRepository.operatorSave(operator);
        return operator;
    }

    public boolean doOperatorCheck(OperatorVO operator) {
        OperatorEnum.Type type = operator.getType();
        if (type == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "类型错误");
        }

        Long operatorId = operator.getId();
        OperatorVO oldVO = new OperatorVO();
        // 若是更新
        if (operatorId != null) {
            oldVO = operatorRepository.operator(operatorId);

            // 若现数据库的类型为非机构, 且修改类型不为自身, 则拒绝
            OperatorEnum.Type dbType = oldVO.getType();
            if (OperatorEnum.Type.ORGANIZE != dbType && type != dbType) {
                throw new PlatformException(BaseErrorCode.EXECUTE, "新增或更新失败: 区域|行业|品牌不允许修改为其他类型");
            }
        }

        // 若有类型外键id,则与数据库的不能重复
        String typeForeignId = operator.getTypeForeignId();
        String typeForeignName = operator.getTypeForeignName();
        boolean isTypeIdCheck = StrUtil.isNotBlank(typeForeignId) && !StrUtil.equals(oldVO.getTypeForeignId(), typeForeignId);
        boolean isTypeNameCheck = StrUtil.isNotBlank(typeForeignName) && !StrUtil.equals(oldVO.getTypeForeignName(), typeForeignName);
        if (isTypeIdCheck || isTypeNameCheck) {
            OperatorQuery operatorQuery = new OperatorQuery();
            if (isTypeIdCheck) {
                operatorQuery.setTypeForeignId(typeForeignId);
            } else {
                operatorQuery.setTypeForeignNameEq(typeForeignName);
            }
            Long operatorCountByQuery = operatorRepository.count(operatorQuery);
            if (operatorCountByQuery > 0) {
                throw new PlatformException(BaseErrorCode.PARAM, "该" + type.getValue() + "已存在运营商");
            }
        }

        // 修正domain数据
        String domain = operator.getDomain();
        if (ObjectUtil.isNotEmpty(domain)) {
            domain = domain.toLowerCase();
            OperatorConfigVO operatorConfigVO = operatorRepository.getOperatorConfig();
            if (operatorConfigVO == null) {
                throw new PlatformException(BaseErrorCode.SERVER, "运营商域名配置异常");
            }
            String baseDomain = operatorConfigVO.getBaseDomain();
            if (!StrUtil.endWith(domain, baseDomain)) {
                domain = domain + "." + baseDomain;
                operator.setDomain(domain);
            }

            // 若和数据库的相等则不进入以下判断
            if (!StrUtil.equals(oldVO.getDomain(), domain)) {
                OperatorQuery operatorQuery = new OperatorQuery();
                operatorQuery.setDomain(domain);
                Long operatorCountByQuery = operatorRepository.count(operatorQuery);
                if (operatorCountByQuery > 0) {
                    throw new PlatformException(BaseErrorCode.PARAM, "域名错误，运营商已存在");
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long dealerCustomSave(DealerCustomSaveReq dealerEditReq, boolean isRegisterOnce) {
        if (dealerEditReq.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        if (isRegisterOnce) {
            dealerRepository.dealerDelete(Collections.singletonList(dealerEditReq.getId()));
        }
        DealerVO item = TransferUtils.transfer(dealerEditReq, DealerVO::new, (c, v) -> {
        });
//        item.init();
        item.setState(RoleEnum.State.IN.getCode());
        item.setServiceRate(0D);
        Long aLong = dealerRepository.dealerSave(item);
        return aLong;
    }

    @Override
    public int dealerEdit(Long id, DealerEditReq dealerEditReq) {
        DealerVO item = TransferUtils.transfer(dealerEditReq, DealerVO::new, (c, v) -> {
            v.setId(id);
        });
        return dealerRepository.dealerEdit(item);
    }

    @Override
    public int dealerServiceFeeConfigEdit(Long id, Double serviceRate) {
        DealerVO item = new DealerVO();
        item.setId(id);
        item.setServiceRate(serviceRate);
        return dealerRepository.dealerEdit(item);
    }

    @Override
    public int dealerDelete(List<Long> dealerIdList) {
        return dealerRepository.dealerDelete(dealerIdList);
    }

    @Override
    public void dealerEdit(List<EditColumnVO> editColumnList, Long id) {
        dealerRepository.dealerEdit(editColumnList, id);
    }

    @Override
    public DealerVO dealer(Long dealerId) {
        DealerVO dealer = dealerRepository.dealer(dealerId);
        return dealer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DealerVO dealerProxySave(DealerProxySaveReq dealerRegisterCommand, Long inviteAccountId, Long accountId, boolean isRegisterOnce) {
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        DealerVO item = TransferUtils.transfer(dealerRegisterCommand, DealerVO::new, (c, v) -> {
            v.setId(accountId);
            v.setOperatorId(inviteAccountId);
        });
//        item.init();
        item.setState(RoleEnum.State.IN.getCode());
        dealerRepository.dealerSave(item);
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long selectorCustomSave(SelectorCustomSaveReq req, boolean isRegisterOnce) {
        if (req.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        if (isRegisterOnce) {
            selectorRepository.selectorDelete(Collections.singletonList(req.getId()));
        }
        SelectorVO item = TransferUtils.transfer(req, SelectorVO::new);

//        item.init();
        item.setName(StrUtil.isEmpty(req.getName()) ? req.getUsername() : req.getName());
        item.setUsername(req.getUsername());

        return selectorRepository.selectorSave(item);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long selectorSave(SelectorVO selector) {
        if (selector.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        selector.setState(RoleEnum.State.IN);
        selector.setName(StrUtil.isEmpty(selector.getName()) ? selector.getUsername() : selector.getName());
        selector.setLevel(1);
        return selectorRepository.selectorSave(selector);
    }

    @Override
    public int selectorEdit(Long id, SelectorEditReq selectorEditReq) {
        SelectorVO item = TransferUtils.transfer(selectorEditReq, SelectorVO::new, (c, v) -> {
            v.setId(id);
        });
        return selectorRepository.selectorEdit(item);
    }

    @Override
    public int selectorLevelEdit(Long id, Integer level) {
        SelectorVO item = new SelectorVO();
        item.setId(id);
        item.setLevel(level);
        return selectorRepository.selectorEdit(item);
    }

    @Override
    public int selectorDelete(List<Long> selectorIdList) {
        return selectorRepository.selectorDelete(selectorIdList);
    }

    @Override
    public void selectorEdit(List<EditColumnVO> editColumnList, Long id) {
        selectorRepository.selectorEdit(editColumnList, id);
    }

    @Override
    public SelectorVO selector(Long selectorId) {
        SelectorVO selector = selectorRepository.selector(selectorId);
        return selector;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inviteSuccess(Long accountId, RoleEnum.CompanyRole inviteAccountRole, Long inviteAccountId) {
        if (accountId == null) {
            return;
        }
        //增加邀请数量
        List<EditColumnVO> columnList = new ArrayList<>();
        columnList.add(new EditColumnVO(OperatorEnum.TEAM_COUNT, 1));
        columnList.add(new EditColumnVO(OperatorEnum.TODAY_INVITE, 1));
        columnList.add(new EditColumnVO(OperatorEnum.TO_MONTH_INVITE, 1));
        if (RoleEnum.CompanyRole.SUPPLIER == inviteAccountRole) {
            columnList.add(new EditColumnVO(OperatorEnum.TEAM_SUPPLIER_COUNT, 1));
        } else if (RoleEnum.CompanyRole.SELECTOR == inviteAccountRole) {
            columnList.add(new EditColumnVO(OperatorEnum.TEAM_SELECTOR_COUNT, 1));
        }
        selectorRepository.selectorEdit(columnList, accountId);
    }

    @Override
    public void selectorLevelUp(Long accountId, Integer level) {
        SelectorVO selector = selectorRepository.selector(accountId);
        if (level != null) {
            if (selector.getLevel() == null || selector.getLevel() < level) {
                selector.setLevel(level);
            } else {
                return;
            }
            //持久化
            SelectorVO selectorEdit = new SelectorVO();
            selectorEdit.setId(accountId);
            selectorEdit.setLevel(level);
            selectorRepository.selectorEdit(selectorEdit);
            //触发上级甄选师等级计算和升级
            Long inviteId = selector.getInviteId();
            // TODO[cross-domain relation]: 原调用 selectorRepository.countLevelNumber(inviteId) 组装 relation.TeamUserCountReq 触发上级等级计算, relation 迁 biz-user 后恢复
        }
    }

    @Override
    public SelectorVO selectorProxySave(SelectorProxySaveReq selectorProxySaveReq, Long inviteAccountId, Long accountId) {
        if (accountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        SelectorVO item = TransferUtils.transfer(selectorProxySaveReq, SelectorVO::new, (c, v) -> {
            v.setId(accountId);
            v.setInviteId(inviteAccountId);
        });
//        item.init();

        item.setName(Opt.ofBlankAble(selectorProxySaveReq.getName()).orElse(selectorProxySaveReq.getUsername()));
        item.setUsername(selectorProxySaveReq.getUsername());
        selectorRepository.selectorSave(item);
        return item;
    }

    @Override
    public OperatorDomainInfo getOperatorDomainInfo(Long operatorId) {
        return operatorRepository.getOperatorDomainInfo(operatorId);
    }

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * <p>迁自旧 {@code IOperatorDomainImpl#supplierIdListByType}, 三个分支口径逐字保持。
     * 偏离: 旧 {@code OperatorVO.type} 是 {@code Integer}, Base 已换成
     * {@code OperatorEnum.Type} 枚举, 故比较落在枚举 code 上。</p>
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型, 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 运营商不存在时返回空列表
     */
    @Override
    public List<Long> supplierIdListByType(Long accountId, Integer searchType) {
        OperatorVO operator = operatorRepository.operator(accountId);
        if (operator == null) {
            return Collections.emptyList();
        }
        Integer operatorType = operator.getType() == null ? null : operator.getType().getCode();
        List<Integer> allowTypeList = new ArrayList<>();
        allowTypeList.add(OperatorEnum.Type.ORGANIZE.getCode());
        allowTypeList.add(operatorType);

        Integer type = Opt.ofNullable(searchType).orElse(operatorType);
        OperatorEnum.Type dataType = type == null ? null : OperatorEnum.Type.findByCode(type);
        if (!allowTypeList.contains(type) || dataType == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "不可用的运营类型");
        }

        String typeForeignId = operator.getTypeForeignId();
        SupplierQuery query = new SupplierQuery();
        switch (dataType) {
            case ORGANIZE:
            case BRAND:
                // Q3 越权收敛: 品牌(BRAND)并入机构(ORGANIZE)分支, 口径一致 = 看自己招募的所有供应商
                // (Base 新增 BRAND 类型, new-scm DataType 无此枚举; 品牌运营商可见范围收敛为自招募, 不放大)
                query.setInviteId(accountId);
                break;
            case INDUSTRY:
                // 行业: 看同行业的供应商
                query.setIndustryId(NumberUtil.parseLong(typeForeignId, null));
                break;
            case AREA:
                // 区域: typeForeignId 为省市区逗号串, 取最后一段作为公司区域码
                query.setCompanyAreaCode(NumberUtil.parseLong(CollUtil.getLast(StrUtil.split(typeForeignId, ',')), null));
                break;
            default:
                throw new PlatformException(BaseErrorCode.PARAM, "不可用的运营类型");
        }
        return supplierRepository.idByQuery(query);
    }
}
