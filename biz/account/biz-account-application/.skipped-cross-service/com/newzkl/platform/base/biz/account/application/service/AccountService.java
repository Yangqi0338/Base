package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountFinanceVO;
import com.newzkl.platform.base.biz.account.model.res.AppHomePageDataVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountAwardUserVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.NameAuthVO;
import com.newzkl.platform.base.biz.account.model.auth.req.CustomSaveBatchReq;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
public interface AccountService {

    AppHomePageDataVO appHomePageData(Long accountId);

    void submitNameAuthInfo(NameAuthVO nameAuthVO);

    void batchCreateEmp(Long pid, List<SubProxySaveReq> subProxySaveReqList);

    /**
     * 批量进行个人注册
     *
     * @param customSaveBatchReqList
     */
    void customSaveBatch(List<CustomSaveBatchReq> customSaveBatchReqList);

    /**
     * 用户收益
     *
     * @param accountId
     */
    AccountFinanceVO accountFinanceVO(CommonEnum.Client client, Long accountId);

    Page<AccountAwardUserVO> listOperatorUser(AccountAwardUserQuery req);


    /**
     * 分页查询会员账号
     *
     * @param query
     * @return
     */
    Page<MemberAccountVO> pageAccount(AccountQuery query);

    /**
     * 禁用或者启用会员
     *
     * @param req
     */
    void disableAccount(AdminDisableAccountReq req);

    /**
     * 身份创建
     *
     * @param accountReq 账号创建请求
     */
    Long identityCreate(AdminRegisterIdentityReq accountReq);

    /**
     * 账号删除
     *
     * @param idList 删除id
     */
    void accountDelete(List<Long> idList);
}
