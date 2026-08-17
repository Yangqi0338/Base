package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.PackGoodsDomain;
import com.newzkl.platform.base.biz.account.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.model.pack.req.PackGoodsCommand;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-入会礼包商品
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.PackGoodsController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>旧 {@code packGoodsDelete}/{@code packGoodsUpdate} 直调
 *       {@code IPackGoodsRepository}（越层），本仓统一收敛到 {@link PackGoodsDomain}。</li>
 *   <li>旧 {@code packGoodsDelete} 收 {@code IdListObj}，本仓换通用 {@code IdListCommand}。</li>
 *   <li>{@code packGoodsPage} 旧回 {@code PageInfo<PackGoodsVO>}，本仓保留 {@code Page} 分页壳直返。
 *       <b>前端契约变</b>（分页入参 current/size → pageNo/pageSize；出参 list → records）。</li>
 *   <li>{@code TODO[auth-defer]}：旧 {@code @Limit(FuncCons.Admin.pack_goods)} 鉴权注解暂缺，
 *       待入口 starter 鉴权基建接入。</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/packGoods")
@Validated
@RequiredArgsConstructor
public class PackGoodsController {

    private final PackGoodsDomain packGoodsDomain;

    /**
     * 礼包商品创建
     *
     * @param command 写入入参
     * @return 礼包主键ID
     */
    @PostMapping("packGoodsSave")
    public PlatformResult<Long> packGoodsSave(@Valid @RequestBody PackGoodsCommand command) {
        return PlatformResult.success(packGoodsDomain.packGoodsSave(command));
    }

    /**
     * 礼包商品删除
     *
     * @param idListCommand ID 列表入参
     * @return 空结果
     */
    @PostMapping("packGoodsDelete")
    public PlatformResult<Void> packGoodsDelete(@Valid @RequestBody IdCommand idListCommand) {
        packGoodsDomain.packGoodsDelete(idListCommand.getIdList());
        return PlatformResult.success();
    }

    /**
     * 礼包商品修改
     *
     * @param command 写入入参
     * @return 空结果
     */
    @PostMapping("packGoodsUpdate")
    public PlatformResult<Void> packGoodsUpdate(@RequestBody PackGoodsCommand command) {
        packGoodsDomain.packGoodsSave(command);
        return PlatformResult.success();
    }

    /**
     * 礼包商品详情
     *
     * @param id 主键ID
     * @return 礼包出参
     */
    @GetMapping("packGoods")
    public PlatformResult<PackGoodsRes> packGoods(@RequestParam("id") Long id) {
        return PlatformResult.success(packGoodsDomain.packGoodsVO(id));
    }

    /**
     * 礼包商品分页
     *
     * @param query 分页查询
     * @return 礼包分页
     */
    @PostMapping("packGoodsPage")
    public PlatformResult<Page<PackGoodsRes>> packGoodsPage(@RequestBody PackGoodsQuery query) {
        return PlatformResult.success(packGoodsDomain.packGoodsVOList(query));
    }
}
