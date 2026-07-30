package com.newzkl.platform.base.biz.user.application.relation.service;

import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressAddReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressPageReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressUpdateReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressVO;

import java.util.List;

/**
 * 收货地址应用服务接口
 *
 * <p>负责跨领域/跨模块的业务编排，调用领域服务完成核心逻辑。</p>
 *
 * <p>迁移说明：源 pageQuery 返回 MyBatis-Plus Page，降级为 List(TODO[page-meta])。</p>
 *
 * @author sijiwang
 */
public interface ShipAddressService {

    /**
     * 新增收货地址
     *
     * @param req 新增参数
     * @return 新增后的视图对象
     */
    ShipAddressVO add(ShipAddressAddReq req);

    /**
     * 更新收货地址
     *
     * @param req 更新参数
     * @return 更新后的视图对象
     */
    ShipAddressVO update(ShipAddressUpdateReq req);

    /**
     * 根据ID查询收货地址详情
     *
     * @param id 地址ID
     * @return 视图对象
     */
    ShipAddressVO getById(Long id);

    /**
     * 查询账号的默认收货地址
     *
     * @param accountId 账号ID
     * @return 默认地址视图对象
     */
    ShipAddressVO getDefaultByAccountId(Long accountId);

    /**
     * 查询账号下所有未删除的收货地址
     *
     * @param accountId 账号ID
     * @return 地址列表
     */
    List<ShipAddressVO> listByAccountId(Long accountId);

    /**
     * 分页查询收货地址
     *
     * @param req 分页查询参数
     * @return 当前页地址列表
     */
    List<ShipAddressVO> pageQuery(ShipAddressPageReq req);

    /**
     * 逻辑删除收货地址
     *
     * @param id       地址ID
     * @param operator 操作人
     * @return 是否删除成功
     */
    boolean delete(Long id, String operator);

    /**
     * 设置默认地址
     *
     * @param id       地址ID
     * @param operator 操作人
     * @return 是否设置成功
     */
    boolean setDefault(Long id, String operator);
}
