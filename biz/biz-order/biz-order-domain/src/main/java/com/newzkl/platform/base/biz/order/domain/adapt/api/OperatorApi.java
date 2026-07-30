package com.newzkl.platform.base.biz.order.domain.adapt.api;

import java.util.List;

/**
 * 运营商域出站端口
 *
 * <p>对等旧 {@code @DubboReference IOperatorFacade} 在交易域的用法: 运营商查订单列表时,
 * 需先按其运营类型 (机构 / 行业 / 区域) 拿到可见供应商 ID 列表, 再据此收敛订单查询范围。
 * 实现落 biz-order-infrastructure 的 {@code adapt/api}, 由该层依赖
 * {@code biz-account-facade} 完成跨域调用, 领域层只见本端口。</p>
 *
 * @author KC
 */
public interface OperatorApi {

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型 (0 机构 / 1 行业 / 2 区域), 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 运营商不存在时返回空列表
     */
    List<Long> supplierIdListByType(Long accountId, Integer searchType);
}
