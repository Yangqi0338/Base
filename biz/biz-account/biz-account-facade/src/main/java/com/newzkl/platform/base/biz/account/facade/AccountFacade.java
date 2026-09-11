package com.newzkl.platform.base.biz.account.facade;

import com.newzkl.platform.base.biz.account.facade.model.AccountRpcQuery;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.List;

/**
 * 运营商域对外契约 (inbound provider)
 *
 * <p>对等旧 {@code com.zkl.scm.user.rpc.facade.IOperatorFacade} 中被跨域调用的那部分能力。
 * 供其他域 (biz-order / biz-goods 的列表查询) 按运营类型收敛数据可见范围, 避免调用方直连
 * biz-account 内部 domain / model。</p>
 *
 * <p>本接口只使用 JDK 类型作出入参, 物理上不引用 biz-account-model
 * (biz-account-facade 的 pom 未声明该依赖), 与 {@code AuthApi} 同口径。</p>
 *
 * @author KC
 */
public interface AccountFacade {

    List<AccountGroupVO> listAccountByIds(List<Long> accountIdList);

    List<Long> queryMember(String nickname);

    AccountGroupVO selectByUserAccount(String userAccount);

    /**
     * 获取账号信息
     *
     * @param id
     * @return
     */
    AccountGroupVO accountInfo(AccountEnum.Client client, Long id);

    boolean registerChannel(ChannelRegisterReq req);

    AccountRpcVO accountInfo(AccountRpcQuery query);

    /**
     * 按凭证查全部端账号
     *
     * <p>client 为空时不限端, 同一 username 可能存在多端账号, 全部返回供登录侧按端优先级择主</p>
     *
     * @param query 查询条件 (username | client)
     * @return 账号列表
     */
    List<AccountRpcVO> accountInfoList(AccountRpcQuery query);

    AccountRpcVO register(List<IdentityRegisterRpcReq> registerRpcReq);

    boolean accountEdit(AccountRpcVO rpcVO);

    /**
     * 微信绑定回写: 按身份把 openId/unionId 落到身份表
     *
     * <p>登录场景传 wxCode 换码后调用, 保持小程序登录与自主注册口径一致。
     * 只覆盖 member/channel 两种小程序身份, 其余身份返回 false 不回写。</p>
     *
     * @param identity 身份
     * @param accountId 账号ID (与身份表主键同值)
     * @param openId   微信 openId
     * @param unionId  微信 unionId
     * @return 是否回写成功
     */
    boolean bindWx(AccountEnum.Identity identity, Long accountId, String openId, String unionId);

    /**
     * 按账号查微信 openId
     *
     * <p>供支付等三方场景回填 openId。member 优先、channel 兜底, 均无则 null。</p>
     *
     * @param accountId 账号ID (与身份表主键同值)
     * @return openId, 无则 null
     */
    String queryWxOpenId(Long accountId, AccountEnum.Identity identity);
}
