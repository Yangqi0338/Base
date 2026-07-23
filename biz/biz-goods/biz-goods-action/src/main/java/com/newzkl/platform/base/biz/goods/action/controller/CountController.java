package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.IndexCountRes;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品统计控制器。
 *
 * <p>提供商品首页运营统计数据查询。</p>
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/count")
@RequiredArgsConstructor
@Slf4j
public class CountController {

    private final SpuRepository spuRepository;

    /**
     * 首页统计。
     *
     * @param timeQuery 时间范围查询条件
     * @return 首页统计结果
     */
    @PostMapping("indexCount")
    public ScmResult<IndexCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        return ScmResult.success(spuRepository.indexCount(timeQuery));
    }
}
