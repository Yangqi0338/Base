package com.newzkl.platform.base.biz.account.facade;

import com.newzkl.platform.base.biz.account.facade.model.ChannelOutVO;
import com.newzkl.platform.base.biz.account.facade.model.ChannelRpcQuery;

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
public interface ChannelFacade {


    List<ChannelOutVO> channelList(ChannelRpcQuery query);
}
