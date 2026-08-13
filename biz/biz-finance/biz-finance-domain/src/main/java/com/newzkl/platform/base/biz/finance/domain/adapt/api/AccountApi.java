package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.facade.PermissionRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

/**
 * 账户域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IAccountFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 查询账户基础信息 (仅头像 + 昵称)
     *
     * @param client    端类型
     * @param accountId 账户ID
     * @return 账户基础信息, 无则 null
     */
    AccountGroupVO account(CommonEnum.Client client, Long accountId);

    /**
     * 获取账户多级上级链路
     *
     * @param client    端类型
     * @param accountId 账户ID
     * @return 上级链路结果
     */
    UpIdRes upId(CommonEnum.Client client, Long accountId);

    /**
     * 增加账户提货积分
     *
     * <p>提现到账回调后按提现记录的 {@code goodsPoints} 回补积分,
     * 迁移自旧 {@code IAccountFacade#addGoodsPoints}。</p>
     *
     * @param accountId   账户ID
     * @param goodsPoints 提货积分增量
     */
    void addGoodsPoints(Long accountId, Integer goodsPoints);

    /**
     * 保存运营商杠杆配置
     *
     * @param accountId 账户ID
     * @param radio     杠杆比例
     */

    /**
     * 查询运营商杠杆
     *
     * @param operatorId 运营商ID
     * @return 杠杆值
     */

    /**
     * 查询供应商提现限额
     *
     * @param accountId 供应商账户 id
     * @return 限额 (分), 无限制返回 0
     */
    Integer limitAmount(Long accountId);

    /**
     * 创建渠道商
     */
    void registerChannel(ChannelRegisterReq req);

    /**
     * 权益配置值对象
     *
     * @param level 甄选师等级
     * @return
     */
    PermissionRpcVO levelPermissionVO(RoleEnum.CompanyRole role, Integer level);
}
