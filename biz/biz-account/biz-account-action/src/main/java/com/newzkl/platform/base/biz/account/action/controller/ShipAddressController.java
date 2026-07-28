package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-收货地址控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ShipAddressController}, 路径与 HTTP 方法保持不变。
 * 旧权限点 {@code @Limit(code = FuncCons.address, level = set/get)} 不在本层声明, 鉴权切面归入口 starter。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/shipAddress")
@RequiredArgsConstructor
public class ShipAddressController {

    private final ShipAddressDomain shipAddressDomain;

    /**
     * 收货地址创建。
     *
     * @param shipAddressReq 收货地址入参
     * @return 收货地址 ID
     */
    @PostMapping("shipAddressSave")
    public PlatformResult<Long> shipAddressSave(@Validated @RequestBody ShipAddressReq shipAddressReq) {
        return PlatformResult.success(shipAddressDomain.save(shipAddressReq));
    }

    /**
     * 收货地址修改。
     *
     * @param edit 收货地址入参 (以其 id 为更新目标)
     * @return 成功结果
     */
    @PostMapping("shipAddressUpdate")
    public PlatformResult<Void> shipAddressEdit(@Validated @RequestBody ShipAddressReq edit) {
        shipAddressDomain.edit(edit.getId(), edit);
        return PlatformResult.success();
    }

    /**
     * 收货地址删除。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("shipAddressDelete")
    public PlatformResult<Void> shipAddressDelete(@RequestBody IdListCommand idListObj) {
        shipAddressDomain.delete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 收货地址详情。
     *
     * @param shipAddressId 收货地址 ID
     * @return 收货地址详情
     */
    @GetMapping("shipAddress")
    public PlatformResult<ShipAddressRes> shipAddress(@RequestParam("id") Long shipAddressId) {
        return PlatformResult.success(shipAddressDomain.detail(shipAddressId));
    }

    /**
     * 收货地址分页。
     *
     * @param shipAddressQuery 收货地址查询 (账号 ID 按当前登录态回填)
     * @return 收货地址分页
     */
    @PostMapping("shipAddressPage")
    public PlatformResult<Page<ShipAddressRes>> shipAddressPage(@RequestBody ShipAddressQuery shipAddressQuery) {
        shipAddressQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(shipAddressDomain.pageList(shipAddressQuery));
    }
}
