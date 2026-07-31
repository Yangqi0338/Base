package com.newzkl.platform.base.biz.order.domain.service;


import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountPurseApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountShipAddressApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.HuiFuPurseInfo;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OrderGoodsCheckApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ShipAddressDTO;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderItemCommand;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;
import com.newzkl.platform.base.biz.order.model.support.api.order.StoreDistributionDetailRpcVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * C端用户订单提交领域模型
 * 专注处理 C端 订单提交的完整业务流程
 */
public class CreateMemberPrePayOrderDomain {

    // 依赖注入
    // 迁移: 原 @DubboReference IOrderGoodsFacade/IAccountPurseApi 违 domain 依赖硬线, 改走 adapt/api 出站端口
    private final OrderGoodsCheckApi orderGoodsFacade;
    private final AccountPurseApi accountPurseApi;
    private final IOrderDomainNew orderDomain;

    private final AccountShipAddressApi shipAddressFacade;

    // 订单命令参数
    private final OrderCreateCommand orderCreateCommand;
    private final MemberOrderCreateCommand memberOrderCreateCommand;

    /**
     * 构造函数
     */
    public CreateMemberPrePayOrderDomain(OrderGoodsCheckApi orderGoodsFacade,
                                         AccountPurseApi accountPurseApi,
                                         IOrderDomainNew orderDomain,
                                         AccountShipAddressApi shipAddressFacade,
                                         OrderCreateCommand orderCreateCommand,
                                         MemberOrderCreateCommand memberOrderCreateCommand) {
        // 校验订单类型
        if (OrderEnum.OrderType.MEMBER != orderCreateCommand.getOrderType()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "该领域模型仅支持C端会员订单");
        }
        // 直接赋值，精简换行
        this.orderGoodsFacade = orderGoodsFacade;
        this.accountPurseApi = accountPurseApi;
        this.orderDomain = orderDomain;
        this.shipAddressFacade = shipAddressFacade;
        this.orderCreateCommand = orderCreateCommand;
        this.memberOrderCreateCommand = memberOrderCreateCommand;

    }

    /**
     * 执行C端用户订单提交流程
     */
    public OrderCreateRes execute() {
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        if (CollUtil.isEmpty(orderGoodsList)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "订单商品不能为空");
        }
        List<GoodsVO> goodsList = orderGoodsList.stream()
                .map(item -> {
                    GoodsVO goodsVO = new GoodsVO();
                    goodsVO.setStoreDistributionId(item.getStoreDistributionId());
                    goodsVO.setSkuId(item.getSkuId());
                    goodsVO.setNum(item.getCount());
                    return goodsVO;
                }).collect(Collectors.toList());

        // 可支付校验
        Long channelId = orderCreateCommand.getChannelId();
        HuiFuPurseInfo huiFuPurseInfo = accountPurseApi.queryHuiFuPurse(channelId);
        String huifuId = huiFuPurseInfo.getHuifuId();
        orderCreateCommand.setBenefitTripartiteId(huifuId);
        ShipVO shipVO = orderCreateCommand.getShipVO();
        if (shipVO.getId() != null){
            // 迁移: 原 shipAddressFacade.shipAddress(id)->ShipAddressOutVO 对齐既有出站端口 getAddressDetail(id, accountId)->ShipAddressDTO
            ShipAddressDTO shipAddressDTO = shipAddressFacade.getAddressDetail(shipVO.getId(), SecurityUtils.getAccountId());
            if (Objects.isNull(shipAddressDTO)){
                ThrowsException.exception(BaseErrorCode.PARAM, "收货地址不存在");
            }
            BeanUtils.copyProperties(shipAddressDTO, shipVO);
            shipVO.setShipPhone(shipAddressDTO.getShipPhone());
        }
        // 商品校验

        OrderGoodsCheckReq checkReq = new OrderGoodsCheckReq();
        checkReq.setChannelId(orderCreateCommand.getChannelId())
                .setStoreId(memberOrderCreateCommand.getStoreId())
                .setShipProvinceCode(shipVO.getShipProvinceCode())
                .setShipCityCode(shipVO.getShipCityCode())
                .setShipAreaCode(shipVO.getShipAreaCode())
                .setShipArea(shipVO.getShipArea());

        PlatformResult<OrderGoodsCheckV2Res> checkResult = orderGoodsFacade.orderGoodsCheckV2(checkReq, goodsList);
        if (!checkResult.isSuccess()) {
            ThrowsException.exception(BaseErrorCode.PARAM, checkResult.getMessage());
        }

        // 生成订单
        OrderGoodsCheckV2Res checkData = checkResult.getData();
        StoreDistributionDetailRpcVO storeDistributionDetailRpcVO = checkData.getGoodsInfo().get(0);
        memberOrderCreateCommand.setStoreId(storeDistributionDetailRpcVO.getStoreId());
        memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
        orderCreateCommand.setChannelId(storeDistributionDetailRpcVO.getChannelId());
        //  创建订单 + 保存预支付订单
        OrderCreateRes order = orderDomain.createOrder(checkData, orderCreateCommand);
        orderDomain.savePrePayOrder(order, memberOrderCreateCommand);

        return order;
    }
}