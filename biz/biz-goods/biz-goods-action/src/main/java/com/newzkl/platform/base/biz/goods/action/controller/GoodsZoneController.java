package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.GoodsZoneService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
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
 * 商品分组控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/zone")
@RequiredArgsConstructor
public class GoodsZoneController {

    /**
     * 默认操作人标识, 与 new-scm 保持一致
     */
    private static final String SYSTEM_OPERATOR = "system";

    private final GoodsZoneService goodsZoneService;

    /**
     * 新增商品分组
     *
     * @param addReq 分组新增请求
     * @return 分组详情
     */
    @PostMapping("/add")
    public PlatformResult<GoodsZoneRes> add(@Validated @RequestBody GoodsZoneAddReq addReq) {
        addReq.setOperator(SecurityUtils.getNickName());
        addReq.setCreateId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsZoneService.add(addReq));
    }

    /**
     * 编辑商品分组
     *
     * @param addReq 分组编辑请求
     * @return 分组详情
     */
    @PostMapping("/edit")
    public PlatformResult<GoodsZoneRes> edit(@Validated @RequestBody GoodsZoneAddReq addReq) {
        addReq.setOperator(SecurityUtils.getNickName());
        addReq.setCreateId(SecurityUtils.getAccountId());
        return PlatformResult.success(goodsZoneService.edit(addReq));
    }

    /**
     * 启用商品分组
     *
     * @param id 分组主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    public PlatformResult<Boolean> enable(@PathVariable Long id) {
        return PlatformResult.success(goodsZoneService.enable(id, SYSTEM_OPERATOR));
    }

    /**
     * 禁用商品分组
     *
     * @param id 分组主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    public PlatformResult<Boolean> disable(@PathVariable Long id) {
        return PlatformResult.success(goodsZoneService.disable(id, SYSTEM_OPERATOR));
    }

    /**
     * 商品分组详情
     *
     * @param id 分组主键
     * @return 分组详情
     */
    @GetMapping("/get/{id}")
    public PlatformResult<GoodsZoneRes> getById(@PathVariable Long id) {
        return PlatformResult.success(goodsZoneService.getById(id));
    }

    /**
     * 商品分组分页
     *
     * @param queryReq 分组分页查询条件
     * @return 分组分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<GoodsZoneRes>> pageQuery(@RequestBody GoodsZonePageReq queryReq) {
        return PlatformResult.success(goodsZoneService.pageQuery(queryReq));
    }

    /**
     * 查询全部启用分组
     *
     * @return 启用分组列表
     */
    @GetMapping("/listEnabled")
    public PlatformResult<List<GoodsZoneRes>> listAllEnabled() {
        return PlatformResult.success(goodsZoneService.listAllEnabled());
    }

    /**
     * 删除商品分组
     *
     * @param id 分组主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    public PlatformResult<Boolean> deleteById(@PathVariable Long id) {
        return PlatformResult.success(goodsZoneService.deleteById(id));
    }
}
