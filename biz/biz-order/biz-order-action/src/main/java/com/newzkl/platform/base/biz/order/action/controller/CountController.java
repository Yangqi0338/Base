package com.newzkl.platform.base.biz.order.action.controller;

import com.newzkl.platform.base.biz.order.domain.service.OrderCountDomain;
import com.newzkl.platform.base.biz.order.model.order.res.IndexCountRes;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易-统计控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.CountController},
 * 类级路径 {@code /order/count} 与 {@code indexCount} 端点 (POST, 无前导斜杠) 逐字保持不变。
 * 偏离说明:</p>
 * <ul>
 *   <li>旧 {@code IQueryService.indexCount} 落在 application 层直连 {@code SpuOrderDAO},
 *       新版改注入。</li>
 *   <li>出参 {@code ScmResult} 改 {@code PlatformResult};
 *       {@code IndexCountRes} 字段名与前端契约一致。</li>
 *   <li>旧库金额字段 {@code total_amount} 在中台拆分, 订单金额取 {@code order_payable_amount}。</li>
 * </ul>
 *
 * @author KC
 */
@RestController("orderCountController")
@RequestMapping("/order/count")
@RequiredArgsConstructor
@Slf4j
public class CountController {

    private final OrderCountDomain orderCountDomain;

    /**
     * 分组统计
     *
     * @param timeQuery 时间分组查询
     * @return 首页统计结果
     */
    @PostMapping("indexCount")
    public PlatformResult<IndexCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        return PlatformResult.success(orderCountDomain.indexCount(timeQuery));
    }
}
