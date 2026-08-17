package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.vo.ShipAddressVO;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;

import java.util.List;

/**
 * 收货地址仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.repository.IShipAddressRepository}。
 * 旧接口的 {@code shipAddressEdit(List<EditColumnDTO>, Long)} 列自增能力无调用方, 未迁移。</p>
 *
 * @author KC
 */
public interface ShipAddressRepository {

    /**
     * 保存收货地址
     *
     * @param shipAddress 收货地址领域视图
     * @return 主键 ID
     */
    Long save(ShipAddressVO shipAddress);

    /**
     * 按主键更新收货地址
     *
     * @param shipAddress 收货地址领域视图 (需带 id)
     * @return 影响行数
     */
    int edit(ShipAddressVO shipAddress);

    /**
     * 按 ID 列表删除收货地址
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 收货地址详情
     *
     * @param id 主键 ID
     * @return 收货地址领域视图, 无则 null
     */
    ShipAddressVO detail(Long id);

    /**
     * 按查询条件取单条收货地址
     *
     * @param query 查询条件
     * @return 收货地址领域视图, 无则 null
     */
    ShipAddressVO findByQuery(ShipAddressQuery query);

    /**
     * 收货地址分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<ShipAddressVO> pageList(ShipAddressQuery query);

    /**
     * 将同一 (角色, 账号) 下除指定地址外的其他地址置为非默认
     *
     * @param roleId        角色类型 ID
     * @param accountId     账号 ID
     * @param shipAddressId 保留为默认的地址 ID
     */
    void setOtherNotDefault(RoleEnum.CompanyRole role, Long accountId, Long shipAddressId);
}
