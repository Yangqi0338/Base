package com.newzkl.platform.base.biz.account.domain.service;

import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
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
    void operatorEdit(List<EditColumnDTO> editColumnList, Long id);

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

    void dealerEdit(List<EditColumnDTO> editColumnList, Long id);

    DealerVO dealer(Long dealerId);

    DealerVO dealerProxySave(DealerProxySaveReq dealerRegisterCommand, Long inviteAccountId, Long accountId, boolean isRegisterOnce);

    Long selectorCustomSave(SelectorCustomSaveReq req, boolean isRegisterOnce);

    Long selectorSave(SelectorVO selector);

    int selectorEdit(Long id, SelectorEditReq selectorEditReq);

    int selectorDelete(List<Long> selectorIdList);

    void selectorEdit(List<EditColumnDTO> editColumnList, Long id);

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

    SelectorVO selectorProxySave(SelectorProxySaveReq selectorProxySaveReq, Long inviteAccountId, Long accountId);

    /**
     * 获取运营商信息
     *
     * @param id
     * @return
     */
    OperatorDomainInfo getOperatorDomainInfo(Long id);

}
