package com.newzkl.platform.base.biz.order.action.controller;

import com.newzkl.platform.base.biz.order.application.service.IQueryService;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.IndexCountRes;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单-统计
 * @author fang
 */
@RestController
@RequestMapping("/order/count")
@Slf4j
public class CountController {

    @Autowired
    private IOrderDomain orderDomain;
    /**
     * 分组统计
     * @return
     */
    @PostMapping("indexCount")
    public PlatformResult<IndexCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        return PlatformResult.success(orderDomain.indexCount(timeQuery));
    }
}
