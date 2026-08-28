package com.newzkl.platform.base.biz.account.application.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;


/**
 * 账号登录领域服务接口
 */
public interface AccountLoginService {

    /**
     * 账号因素登录
     *
     * <p>纯登录: 账号不存在直接抛 {@code NO_EXIST}, 不回退注册。</p>
     *
     * @param client
     * @param loginReq      密码登录请求参数
     * @return 登录结果
     */
    LoginRes accountLogin(AccountEnum.Client client, LoginReq loginReq);

    /**
     * 登录并注册
     *
     * <p>先尝试登录, 仅当账号不存在({@code NO_EXIST})时自动注册并再次登录; 其余异常原样抛出。
     * 是否走注册回退由该端口固定, 不由前端开关控制; 注册后是否自动登录同样后端写死, 不接收前端入参。</p>
     *
     * @param client
     * @param loginReq      登录请求参数(注册所需字段复用登录入参)
     * @return 登录结果
     */
    LoginRes loginRegister(AccountEnum.Client client, LoginReq loginReq);

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
     * 注册(默认注册后自动登录)
     * @param registerReq 注册请求
     * @return 登录结果(login=false 时 token 为空)
     */
    LoginRes register(AccountEnum.Client client, RegisterReq registerReq);

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

}
