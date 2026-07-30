package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.relation.dto.ShipAddressDTO;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressPageReq;

import java.util.List;
import java.util.Optional;

/**
 * 收货地址仓储接口
 *
 * <p>定义领域层数据访问契约，基础设施层实现具体逻辑。</p>
 *
 * <p>迁移说明：源 pageQuery 返回 MyBatis-Plus Page，领域层不依赖 MP 分页，
 * 降级为 List；分页元数据在 infra 内构造。TODO[page-meta] total 等元数据跨层丢失。</p>
 *
 * @author sijiwang
 */
public interface ShipAddressRepository {

    /**
     * 保存收货地址
     *
     * @param dto 领域模型
     * @return 保存后的领域模型（含主键ID）
     */
    ShipAddressDTO save(ShipAddressDTO dto);

    /**
     * 根据ID更新收货地址
     *
     * @param dto 领域模型（含ID）
     * @return 是否更新成功
     */
    boolean updateById(ShipAddressDTO dto);

    /**
     * 根据ID查询收货地址
     *
     * @param id 地址ID
     * @return 领域模型（空则返回Optional.empty()）
     */
    Optional<ShipAddressDTO> findById(Long id);

    /**
     * 查询账号的默认收货地址
     *
     * @param accountId 账号ID
     * @return 默认地址（无则返回Optional.empty()）
     */
    Optional<ShipAddressDTO> findDefaultByAccountId(Long accountId);

    /**
     * 查询账号下所有未删除的收货地址
     *
     * @param accountId 账号ID
     * @return 地址列表（按默认地址+更新时间倒序）
     */
    List<ShipAddressDTO> findByAccountId(Long accountId);

    /**
     * 分页查询收货地址
     *
     * @param req 分页查询参数
     * @return 当前页地址列表
     */
    List<ShipAddressDTO> pageQuery(ShipAddressPageReq req);

    /**
     * 逻辑删除收货地址
     *
     * @param id       地址ID
     * @param operator 操作人
     * @return 是否删除成功
     */
    boolean logicDeleteById(Long id, String operator);

    /**
     * 设置默认地址（自动取消同账号其他默认地址）
     *
     * @param id        目标地址ID
     * @param accountId 账号ID
     * @param operator  操作人
     * @return 是否设置成功
     */
    boolean setDefault(Long id, Long accountId, String operator);
}
