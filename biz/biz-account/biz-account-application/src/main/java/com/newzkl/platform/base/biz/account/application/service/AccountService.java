package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountFinanceVO;
import com.newzkl.platform.base.biz.account.model.res.AppHomePageDataVO;
import com.newzkl.platform.base.biz.account.model.res.UserHomePageRes;
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

    /**
     * 脉脉通-用户主页信息
     *
     * <p>迁移自旧 {@code IAccountService#getUserHomePage}。跨域数据(关注关系/铺货商品/获赞数)
     * 一律经出站端口取, 端口默认兜底时对应字段为默认值,
     * 见各 {@code *ApiDefaultImpl} 的 infra-gap 说明。</p>
     *
     * @param userId        被查看用户ID
     * @param currentUserId 当前登录用户ID, 可为 null(未登录)
     * @return 用户主页信息
     */
    UserHomePageRes getUserHomePage(Long userId, Long currentUserId);
}
