package com.newzkl.platform.base.biz.account.domain.service;

import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.req.web.DealerProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.SelectorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;

import java.util.List;

/**
 * 运营商端服务
 *
 * @author fang
 */
public interface OperatorClientDomain {
    /**
     * 运营商修改
     *
     * @param operatorEditReq
     * @return
     */
    OperatorVO operatorEdit(OperatorReq operatorEditReq);

    /**
     * 运营商删除
     *
     * @param operatorIdList
     * @return
     */
    int operatorDelete(List<Long> operatorIdList);

    /**
     * 运营商修改
     *
     * @param editColumnList
     * @param id
     */
    void operatorEdit(List<EditColumnVO> editColumnList, Long id);

    /**
     * 查询实体
     *
     * @param operatorId
     * @return
     */
    OperatorVO operator(Long operatorId);

    /**
     * 运营商代理注册
     *
     * @param operatorProxySaveReq
     * @param accountId
     * @return
     */
    OperatorVO operatorProxySave(OperatorProxySaveReq operatorProxySaveReq, Long accountId);

    OperatorVO operatorCustomSave(OperatorCustomSaveReq operatorEditReq, Long accountId);

    Long dealerCustomSave(DealerCustomSaveReq dealerEditReq, boolean isRegisterOnce);

    int dealerEdit(Long id, DealerEditReq dealerEditReq);

    int dealerDelete(List<Long> dealerIdList);

    void dealerEdit(List<EditColumnVO> editColumnList, Long id);

    DealerVO dealer(Long dealerId);

    DealerVO dealerProxySave(DealerProxySaveReq dealerRegisterCommand, Long inviteAccountId, Long accountId, boolean isRegisterOnce);

    /**
     * 交易师服务费率修改
     *
     * <p>迁自旧充血实体 {@code Dealer.serviceFeeConfigEdit(Double)}: 只写 {@code service_rate} 单列,
     * 其余列不动。中台贫血模型下按主键做部分列更新, 语义与旧一致</p>
     *
     * @param id          交易师账号 ID
     * @param serviceRate 服务费率
     * @return 影响行数
     */
    int dealerServiceFeeConfigEdit(Long id, Double serviceRate);

    Long selectorCustomSave(SelectorCustomSaveReq req, boolean isRegisterOnce);

    Long selectorSave(SelectorVO selector);

    int selectorEdit(Long id, SelectorEditReq selectorEditReq);

    int selectorDelete(List<Long> selectorIdList);

    void selectorEdit(List<EditColumnVO> editColumnList, Long id);

    SelectorVO selector(Long selectorId);

    /**
     * 邀请成功处理
     *
     * @param accountId         邀请人账号ID
     * @param inviteAccountRole 被邀请人角色
     * @param inviteAccountId   被邀请人账号ID
     */
    void inviteSuccess(Long accountId, RoleEnum.CompanyRole inviteAccountRole, Long inviteAccountId);

    /**
     * 甄选师提升等级
     *
     * @param accountId
     * @param level
     */
    void selectorLevelUp(Long accountId, Integer level);

    /**
     * 甄选师等级直接改写
     *
     * <p>迁自旧充血实体 {@code Selector.selectorLevelEdit(Integer)}: 只写 {@code level} 单列,
     * 不做「新等级须高于旧等级」判断, 也不触发上级等级重算 —— 与
     * {@link OperatorClientDomain#selectorLevelUp} 的升级语义不同, 是平台侧的人工改写</p>
     *
     * @param id    甄选师账号 ID
     * @param level 等级
     * @return 影响行数
     */
    int selectorLevelEdit(Long id, Integer level);

    SelectorVO selectorProxySave(SelectorProxySaveReq selectorProxySaveReq, Long inviteAccountId, Long accountId);

    /**
     * 获取运营商信息
     *
     * @param id
     * @return
     */
    OperatorDomainInfo getOperatorDomainInfo(Long id);

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * <p>迁自旧 {@code IOperatorDomainImpl#supplierIdListByType}。运营类型决定可见口径:
     * 机构=看自己招募的供应商 (inviteId), 行业=看同行业 (industryId),
     * 区域=看同区域 (companyAreaCode, 取 typeForeignId 逗号串的最后一段)。</p>
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型, 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 运营商不存在时返回空列表
     * @throws com.newzkl.platform.base.common.core.model.exception.PlatformException
     *         传入的运营类型不在该运营商可用范围内时抛出
     */
    List<Long> supplierIdListByType(Long accountId, Integer searchType);

}
