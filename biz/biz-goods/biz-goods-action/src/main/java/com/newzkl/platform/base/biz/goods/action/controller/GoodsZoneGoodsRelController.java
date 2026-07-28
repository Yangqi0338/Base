package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.GoodsZoneGoodsRelService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelDelReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分组-商品关联控制器。
 *
 * @author sijiwang
 */
@RestController
@RequestMapping("/goods/zone/rel")
@RequiredArgsConstructor
public class GoodsZoneGoodsRelController {

    private final GoodsZoneGoodsRelService goodsZoneGoodsRelService;

    /**
     * 批量添加商品到分组。
     *
     * @param addReq 新增请求
     * @return 是否成功
     */
    @PostMapping("/batchAdd")
    public PlatformResult<Boolean> batchAdd(@Validated @RequestBody GoodsZoneGoodsRelAddReq addReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.batchAdd(addReq));
    }

    /**
     * 批量删除分组下的商品。
     *
     * @param delReq 删除请求
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody GoodsZoneGoodsRelDelReq delReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.batchDelete(delReq));
    }

    /**
     * 根据分组 ID 查询关联商品。
     *
     * @param groupId 分组 ID
     * @return 商品列表
     */
    @GetMapping("/listByGroupId/{groupId}")
    public PlatformResult<List<GoodsZoneGoodsRelRes>> listByGroupId(@PathVariable Long groupId) {
        return PlatformResult.success(goodsZoneGoodsRelService.listByGroupId(groupId));
    }

    /**
     * 分页查询关联关系。
     *
     * @param queryReq 分页条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<Page<GoodsZoneGoodsRelRes>> pageQuery(@RequestBody GoodsZoneGoodsRelPageReq queryReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.pageQuery(queryReq));
    }

}
