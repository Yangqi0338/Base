package com.newzkl.platform.base.biz.account.domain.pack.entity;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.common.ddd.model.enums.account.PackEnum;
import com.newzkl.platform.base.common.ddd.model.constant.PackOrderErrorCode;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 入会礼包订单域内实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.model.entity.PackOrder}。</p>
 *
 * <p>迁移说明：{@code SnowflakeIdAble.getSnowflakeId()} 去除（落库时 DO 主键
 * {@code IdType.ASSIGN_ID} 自动雪花）；{@code ThrowsException.exception} 换
 * {@code PlatformException}；{@code SecurityUtils.getAccountId()} 沿用 Base 版；
 * 收货信息由源 {@code ShipAddressOutVO} 改存 biz-user {@code ShipAddressVO} 的 JSON；
 * 状态迁移用本域 {@code PackOrderStateEnum}。</p>
 *
 * @author KC
 */
@Data
public class PackOrder implements Serializable {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 下单账号ID
     */
    private Long accountId;

    /**
     * 收货信息 JSON（结构：ShipAddressVO）
     */
    private String shipVO;

    /**
     * 订单金额（分）
     */
    private Integer amount;

    /**
     * 物流公司
     */
    private String freightCompany;

    /**
     * 物流单号
     */
    private String freightCode;

    /**
     * 订单明细 JSON（结构：List&lt;PackGoodsRes&gt;）
     */
    private String packOrderItemList;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 礼包商品ID
     */
    private Long packId;

    /**
     * 礼包类型（角色 ID）
     */
    private Integer packType;

    /**
     * 礼包等级
     */
    private Integer packLevel;

    /**
     * 礼包名称
     */
    private String levelName;

    /**
     * 上次物流API调用时间
     */
    private LocalDateTime lastFreightApiUseTime;

    /**
     * 初始化订单（组装金额/状态/明细 JSON）
     *
     * @param command       预创建入参
     * @param shipAddressVO 收货信息
     * @param packGoodsList 礼包明细
     */
    public void init(PackOrderCommand command, ShipAddressRes shipAddressVO, List<PackGoodsRes> packGoodsList) {
        for (PackGoodsRes packGoods : packGoodsList) {
            if (Integer.valueOf(0).equals(packGoods.getState())) {
                throw new PlatformException(PackOrderErrorCode.GOODS_DOWN);
            }
        }
        this.accountId = SecurityUtils.getAccountId();
        this.amount = packGoodsList.stream()
                .map(PackGoodsRes::getAmount)
                .filter(a -> a != null && a > 0)
                .mapToInt(Integer::intValue)
                .sum();
        this.shipVO = shipAddressVO == null ? null : JSON.toJSONString(shipAddressVO);
        this.state = PackEnum.PackOrderStateEnum.WAIT_PAY.getCode();
        PackGoodsRes first = packGoodsList.get(0);
        this.packLevel = first.getLevel();
        this.packType = first.getType();
        this.packId = first.getId();
        this.packOrderItemList = JSON.toJSONString(packGoodsList);
        this.levelName = first.getName();
    }

    /**
     * 状态检查
     *
     * @param stateList 允许的状态集合
     * @return 当前状态是否命中
     */
    public boolean checkState(List<Integer> stateList) {
        return ObjectUtil.isNotEmpty(stateList) && stateList.contains(this.state);
    }

    /**
     * 发货（仅待发货可转已完成）
     *
     * @param command 发货入参
     */
    public void packOrderDeliver(PackOrderDeliverCommand command) {
        if (!checkState(Collections.singletonList(PackEnum.PackOrderStateEnum.WAIT_DELIVERY.getCode()))) {
            throw new PlatformException(PackOrderErrorCode.ORDER_TIME_OUT);
        }
        this.freightCode = command.getFreightCode();
        this.freightCompany = command.getFreightCompany();
        this.state = PackEnum.PackOrderStateEnum.SUCCESS.getCode();
    }
}
