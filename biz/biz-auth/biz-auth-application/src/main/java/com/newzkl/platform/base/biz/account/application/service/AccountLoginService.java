package com.newzkl.platform.base.biz.account.application.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

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

    Long customeRegister(IdentityCustomSaveReq customSaveReq);

    /**
     * 修改密码
     *
     * @param codeUpdatePasswordCommand 修改密码命令
     */
    void editPassword(CodeUpdatePasswordReq codeUpdatePasswordCommand);

    /**
     * 修改账号名称
     *
     * @param codeUpdateUsernameReq 修改用户名命令
     */
    void editUsername(CodeUpdateUsernameReq codeUpdateUsernameReq);

    /**
     * 查询一个用户有几个角色
     *
     * @param client
     * @param accountId
     */
    List<RoleVO> accountRoleList(Long accountId, CommonEnum.Client client);
}
