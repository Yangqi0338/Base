package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-收货地址
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ShipAddressController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中不带前导斜杠的写法 (如 {@code shipAddress})。</p>
 *
 * <p>迁移补充: 旧实现的详情与分页直连 {@code IShipAddressRepository}, action 层不得依赖仓储端口,
 * 改走领域服务同名能力, 出入参结构不变。旧 {@code @Limit(code=address)} 未迁移, 见迁移报告「鉴权降级」。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/shipAddress")
@RequiredArgsConstructor
public class ShipAddressController {

    private final ShipAddressDomain shipAddressDomain;

    /**
     * 收货地址创建
     *
     * @param shipAddressReq 收货地址入参
     * @return 收货地址ID
     */
    @PostMapping("shipAddressSave")
    public PlatformResult<Long> shipAddressSave(@Validated @RequestBody ShipAddressReq shipAddressReq) {
        return PlatformResult.success(shipAddressDomain.save(shipAddressReq));
    }

    /**
     * 收货地址修改
     *
     * @param edit 收货地址入参
     * @return 空结果
     */
    @PostMapping("shipAddressUpdate")
    public PlatformResult<Void> shipAddressEdit(@Validated @RequestBody ShipAddressReq edit) {
        shipAddressDomain.edit(edit.getId(), edit);
        return PlatformResult.success();
    }

    /**
     * 收货地址删除
     *
     * @param idListCommand ID 列表
     * @return 空结果
     */
    @PostMapping("shipAddressDelete")
    public PlatformResult<Void> shipAddressDelete(@RequestBody IdCommand idListCommand) {
        shipAddressDomain.delete(idListCommand.getIdList());
        return PlatformResult.success();
    }

    /**
     * 收货地址详情
     *
     * @param shipAddressId 收货地址ID
     * @return 收货地址出参
     */
    @GetMapping("shipAddress")
    public PlatformResult<ShipAddressRes> shipAddress(@RequestParam("id") Long shipAddressId) {
        return PlatformResult.success(shipAddressDomain.detail(shipAddressId));
    }

    /**
     * 收货地址分页
     *
     * <p>保留旧语义: 强制按当前登录账号过滤。</p>
     *
     * @param shipAddressQuery 收货地址查询
     * @return 收货地址分页
     */
    @PostMapping("shipAddressPage")
    public PlatformResult<Page<ShipAddressRes>> shipAddressPage(@RequestBody ShipAddressQuery shipAddressQuery) {
        shipAddressQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(shipAddressDomain.pageList(shipAddressQuery));
    }

    /**
     * 查询默认收货地址
     *
     * <p>保留旧语义: 按当前登录态 (账号 + 角色) 取 isDefault 开启的收货地址, 登录态过滤下沉
     * 领域服务内部。旧 {@code @Limit(code=address)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @return 默认收货地址出参, 无则 null
     */
    @GetMapping("defaultShipAddress")
    public PlatformResult<ShipAddressRes> defaultShipAddress() {
        return PlatformResult.success(shipAddressDomain.defaultShipAddress());
    }
}
