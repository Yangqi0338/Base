package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.GoodsZoneService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
 * 商品分组控制器。
 *
 * @author sijiwang
 */
@RestController
@RequestMapping("/goods/zone")
@RequiredArgsConstructor
public class GoodsZoneController {

    private final GoodsZoneService goodsZoneService;

    /**
     * 新增商品分组。
     *
     * @param addReq 新增请求
     * @return 分组结果
     */
    @PostMapping("/add")
    public ScmResult<GoodsZoneRes> add(@Validated @RequestBody GoodsZoneAddReq addReq) {
        addReq.setOperator(SecurityUtils.getNickName());
        addReq.setCreateId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsZoneService.add(addReq));
    }

    /**
     * 编辑商品分组。
     *
     * @param addReq 编辑请求
     * @return 分组结果
     */
    @PostMapping("/edit")
    public ScmResult<GoodsZoneRes> edit(@Validated @RequestBody GoodsZoneAddReq addReq) {
        addReq.setOperator(SecurityUtils.getNickName());
        addReq.setCreateId(SecurityUtils.getAccountId());
        return ScmResult.success(goodsZoneService.edit(addReq));
    }

    /**
     * 启用商品分组。
     *
     * @param id 分组 ID
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    public ScmResult<Boolean> enable(@PathVariable Long id) {
        return ScmResult.success(goodsZoneService.enable(id, SecurityUtils.getNickName()));
    }

    /**
     * 禁用商品分组。
     *
     * @param id 分组 ID
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    public ScmResult<Boolean> disable(@PathVariable Long id) {
        return ScmResult.success(goodsZoneService.disable(id, SecurityUtils.getNickName()));
    }

    /**
     * 根据 ID 查询详情。
     *
     * @param id 主键 ID
     * @return 分组详情
     */
    @GetMapping("/get/{id}")
    public ScmResult<GoodsZoneRes> getById(@PathVariable Long id) {
        return ScmResult.success(goodsZoneService.getById(id));
    }

    /**
     * 分页查询分组列表。
     *
     * @param queryReq 分页条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public ScmResult<Page<GoodsZoneRes>> pageQuery(@RequestBody GoodsZonePageReq queryReq) {
        return ScmResult.success(goodsZoneService.pageQuery(queryReq));
    }

    /**
     * 查询所有启用的分组。
     *
     * @return 分组列表
     */
    @GetMapping("/listEnabled")
    public ScmResult<List<GoodsZoneRes>> listAllEnabled() {
        return ScmResult.success(goodsZoneService.listAllEnabled());
    }

    /**
     * 删除商品分组。
     *
     * @param id 主键 ID
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    public ScmResult<Boolean> deleteById(@PathVariable Long id) {
        return ScmResult.success(goodsZoneService.deleteById(id));
    }
}
