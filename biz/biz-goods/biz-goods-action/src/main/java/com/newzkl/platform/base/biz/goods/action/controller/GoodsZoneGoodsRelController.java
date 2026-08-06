package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.GoodsZoneGoodsRelService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelDelReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
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
 * 商品分组-商品关联控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/zone/rel")
@RequiredArgsConstructor
public class GoodsZoneGoodsRelController {

    private final GoodsZoneGoodsRelService goodsZoneGoodsRelService;

    /**
     * 批量添加商品到分组
     *
     * @param addReq 关联新增请求
     * @return 是否成功
     */
    @PostMapping("/batchAdd")
    public PlatformResult<Boolean> batchAdd(@Validated @RequestBody GoodsZoneGoodsRelAddReq addReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.batchAdd(addReq));
    }

    /**
     * 批量删除分组下的商品
     *
     * @param delReq 关联删除请求
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody GoodsZoneGoodsRelDelReq delReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.batchDelete(delReq));
    }

    /**
     * 按分组主键查询关联商品
     *
     * @param groupId 分组主键
     * @return 关联商品列表
     */
    @GetMapping("/listByGroupId/{groupId}")
    public PlatformResult<List<GoodsZoneGoodsRelRes>> listByGroupId(@PathVariable Long groupId) {
        return PlatformResult.success(goodsZoneGoodsRelService.listByGroupId(groupId));
    }

    /**
     * 关联关系分页
     *
     * @param queryReq 关联分页查询条件
     * @return 关联分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<GoodsZoneGoodsRelRes>> pageQuery(@RequestBody GoodsZoneGoodsRelPageReq queryReq) {
        return PlatformResult.success(goodsZoneGoodsRelService.pageQuery(queryReq));
    }

    // 源 checkExists (GET /checkExists) 标注 @Deprecated, 按规则不迁 (Base 侧应用服务亦无对等方法)。
}
