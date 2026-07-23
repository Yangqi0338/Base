package com.newzkl.platform.base.biz.account.application.auth.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.account.model.req.CodeUpdateUsernameReq;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.account.model.auth.req.*;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.biz.account.model.auth.res.TokenAndExpireRes;

import java.util.List;

/**
 * 账号登录领域服务接口
 */
public interface AccountLoginService {

    /**
     * 账号因素登录
     *
     * @param loginReq 密码登录请求参数
     * @return 登录结果
     */
    LoginRes accountLogin(LoginReq loginReq);

    /**
     * 子账号密码登录
     *
     * @param mainUsername 主账号用户名
     * @param loginReq     密码登录请求参数
     * @return 登录结果
     */
    LoginRes subPasswordLogin(String mainUsername, LoginReq loginReq);

    /**
     * 刷新 Token
     *
     * @param accountId 账号ID
     * @return 登录结果
     */
    LoginRes refreshToken(Long accountId);

    /**
     * 切换客户端
     *
     * @param toggleClientReq 切换客户端请求参数
     * @return 登录结果
     */
    LoginRes toggleClient(ToggleClientReq toggleClientReq);

    /**
     * 登录记录分页
     *
     * @param accountLoginLogQuery
     * @return
     */
    Page<AccountLoginLogRes> accountLoginLogPage(AccountLoginLogQuery accountLoginLogQuery);

    /**
     * 登录注册 集成
     *
     * @param codeLoginRegisterReq
     * @return
     */
    LoginRes loginRegister(CodeLoginRegisterReq codeLoginRegisterReq);

    /**
     * 通过账号ID获取Token及剩余有效期
     *
     * @param accountId 账号ID（即account.getId()）
     * @param client
     * @return TokenAndExpireVO（无有效登录时抛出异常）
     */
    TokenAndExpireRes getTokenAndExpireById(Long accountId, CommonEnum.Client client);

    /**
     * 批量注册
     */
    void accountBatchRegister(List<CustomSaveBatchReq> customSaveBatchReqList);

    /**
     * 批量注册
     */
    Long customeRegister(IdentitySaveReq customSaveReq);

    /**
     * 供应商通过运营商生成的注册链接进行注册
     */
    Long inviteSupplierRegister(IdentitySaveReq customSaveReq, String host);

    /**
     * 修改账号名称
     *
     * @param codeUpdateUsernameReq 修改用户名命令
     */
    void editUsername(CodeUpdateUsernameReq codeUpdateUsernameReq);

    /**
     * 修改密码
     *
     * @param codeUpdateUsernameReq 修改密码命令
     */
    void editPassword(CodeUpdatePasswordReq codeUpdateUsernameReq);
}
