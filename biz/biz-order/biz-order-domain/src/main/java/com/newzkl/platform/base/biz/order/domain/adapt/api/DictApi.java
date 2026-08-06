package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.OrderConfigVO;

import java.util.List;

/**
 * 运营商域出站端口
 *
 * <p>对等旧 {@code @DubboReference IOperatorFacade} 在交易域的用法: 运营商查订单列表时,
 * 需先按其运营类型 (机构 / 行业 / 区域) 拿到可见供应商 ID 列表, 再据此收敛订单查询范围。
 <dependency>
 <groupId>com.newzkl.platform.Base</groupId>
 <artifactId>biz-finance-facade</artifactId>
 </dependency> * 实现落 biz-order-infrastructure 的 {@code adapt/api}, 由该层依赖
 * {@code biz-account-facade} 完成跨域调用, 领域层只见本端口。</p>
 *
 * @author KC
 */
public interface DictApi {

    OrderConfigVO get(Long id);

    void set(Long id, String value);
}
