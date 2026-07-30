package com.newzkl.platform.base.biz.account.facade;

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
public interface OperatorFacade {

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * <p>运营类型决定可见口径: 机构 (0) 看自己招募的供应商, 行业 (1) 看同行业,
     * 区域 (2) 看同区域。</p>
     *
     * <p>传入的运营类型不在该运营商可用范围内时, 抛出通用层
     * {@code PlatformException(BaseErrorCode.PARAM)}。</p>
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型 (0 机构 / 1 行业 / 2 区域), 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 运营商不存在时返回空列表
     */
    List<Long> supplierIdListByType(Long accountId, Integer searchType);
}
