package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.req.query.ShipAddressQuery;
import com.newzkl.platform.base.biz.order.model.req.ShipAddressReq;
import com.newzkl.platform.base.biz.order.model.res.ShipAddressRes;

import java.util.List;

/**
 * 收货地址领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.service.IShipAddressDomain}。
 * 原权限点 {@code @Limit(code = FuncCons.address)} 不在本层迁移, 鉴权切面归入口 starter。</p>
 *
 * @author KC
 */
public interface ShipAddressDomain {

    /**
     * 新建收货地址
     *
     * <p>账号 ID / 角色 ID 按当前登录态回填; 若为默认地址, 同 (角色, 账号) 下其他地址置为非默认。</p>
     *
     * @param req 收货地址入参
     * @return 主键 ID
     */
    Long save(ShipAddressReq req);

    /**
     * 删除收货地址
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 收货地址详情
     *
     * @param id 收货地址 ID
     * @return 收货地址出参, 无则 null
     */
    ShipAddressRes detail(Long id);

    /**
     * 查询当前登录态下的默认收货地址
     *
     * @return 默认收货地址出参, 无则 null
     */
    ShipAddressRes defaultShipAddress();

    /**
     * 收货地址分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<ShipAddressRes> pageList(ShipAddressQuery query);
}
