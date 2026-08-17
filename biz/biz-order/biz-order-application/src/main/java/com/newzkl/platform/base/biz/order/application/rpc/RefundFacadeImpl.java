package com.newzkl.platform.base.biz.order.application.rpc;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.facade.OrderFacade;
import com.newzkl.platform.base.biz.order.facade.RefundFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.*;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.req.RefundItemCommand;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.support.api.ReceiveAddressOutVO;
import com.newzkl.platform.base.biz.order.model.support.api.SupplierRefundVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.RefundErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * @author fang
 */
@DubboService
@Component
@RequiredArgsConstructor
public class RefundFacadeImpl implements RefundFacade {


    private final RefundDomain refundDomain;
    private final RefundService refundService;
    private final GoodsApi goodsApi;
    private final OrderFacade orderFacade;
    private final SupplierApi supplierApi;
    private final QueryService queryService;
    private final OrderDomain orderDomain;
    private final RefundRepository refundRepository;

    @Override
    public Long apiSubmit(Long accountId, ApiRefundSubmitReq refundSubmitReq) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(refundSubmitReq.getOutOrderNo());
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        if(refundSubmitReq.getSkuList().size() > 1){
            ThrowsException.exception(BaseErrorCode.PARAM, "退款SkuID集合, 目前仅能提交一个SKU");
        }
        Long spuOrderId = orderDomain.spuOrderId(orderIdList.get(0), refundSubmitReq.getSkuList().get(0).getSkuId());
        RefundCommand refundCommand = TransferUtils.transfer(refundSubmitReq, new Function<ApiRefundSubmitReq, RefundCommand>() {
            @Override
            public RefundCommand apply(ApiRefundSubmitReq refundSubmitReq) {
                RefundCommand refundCommand = new RefundCommand();
                refundCommand.setRefundType(refundSubmitReq.getRefundType());
                refundCommand.setReason(refundSubmitReq.getReason());
                refundCommand.setRemark(refundSubmitReq.getRemark());
                refundCommand.setImages(refundSubmitReq.getImages());
                refundCommand.setOrderId(orderIdList.get(0));
                refundCommand.setSpuOrderId(spuOrderId);
                refundCommand.setRole(RoleEnum.CompanyRole.CHANNEL);
                refundCommand.setRefundItemCommandList(TransferUtils.transfers(refundSubmitReq.getSkuList(), new Function<ApiRefundSubmitGoodsReq, RefundItemCommand>() {
                    @Override
                    public RefundItemCommand apply(ApiRefundSubmitGoodsReq apiRefundSubmitGoodsReq) {
                        RefundItemCommand refundItemCommand = new RefundItemCommand();
                        refundItemCommand.setSkuId(apiRefundSubmitGoodsReq.getSkuId());
                        return refundItemCommand;
                    }
                }));
                return refundCommand;
            }
        });
        return refundService.refundCreateApi(refundCommand);
    }

    @Override
    public void apiStop(Long accountId, Long refundId) {
        refundDomain.stopAudit(RoleEnum.CompanyRole.CHANNEL, accountId, refundId);
    }

    @Override
    public void apiPass(Long accountId, Long refundId) {
        refundDomain.agreeAudit(refundId, RoleEnum.CompanyRole.CHANNEL);
    }

    @Override
    public void apiRefuse(Long accountId, Long refundId) {
        refundDomain.refuseAudit(refundId, RoleEnum.CompanyRole.CHANNEL, "");
    }

    @Override
    public void apiSubmitFreight(Long accountId, ApiRefundFreightReq refundFreightReq) {
        RefundFreightVO refundFreightVO = TransferUtils.transfer(refundFreightReq, new Function<ApiRefundFreightReq, RefundFreightVO>() {
            @Override
            public RefundFreightVO apply(ApiRefundFreightReq refundFreightReq) {
                RefundFreightVO refundFreightVO = new RefundFreightVO();
                refundFreightVO.setRefundId(refundFreightReq.getRefundId());
                refundFreightVO.setFreightCompanyName(refundFreightReq.getFreightCompanyName());
                refundFreightVO.setFreightNo(refundFreightReq.getFreightNo());
                return refundFreightVO;
            }
        });
        refundDomain.submitRefundFreight(refundFreightVO);

    }

    @Override
    public ApiRefundFreightAddressVO apiFreightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(refundAddressInfoReq.getOutOrderNo());
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号不存在");
        }
        SpuOrderRelationVO spuOrderRelationVO = orderFacade.spuOrderRelation(orderIdList.get(0), refundAddressInfoReq.getSpuId());
        if(spuOrderRelationVO == null){
            ThrowsException.exception(BaseErrorCode.PARAM, "SPU_ID错误");
        }
        if(spuOrderRelationVO.getSupplierId().intValue() == 1){
            ApiRefundFreightAddressVO outRefundAddress = refundRepository.getOutRefundAddress(spuOrderRelationVO.getId(), refundAddressInfoReq.getSpuId());
            if(outRefundAddress == null){
                ThrowsException.exception(RefundErrorCode.OUT_ADDRESS_NOT_REFUND);
            }
            return outRefundAddress;
        }else {
            SupplierRefundVO supplierRefundVO = supplierApi.supplierRefundVO(spuOrderRelationVO.getSupplierId());
            return TransferUtils.transfer(supplierRefundVO.getReceiveAddressVO(), new Function<ReceiveAddressOutVO, ApiRefundFreightAddressVO>() {
                @Override
                public ApiRefundFreightAddressVO apply(ReceiveAddressOutVO receiveAddressVO) {
                    ApiRefundFreightAddressVO apiRefundFreightAddressVO = new ApiRefundFreightAddressVO();
                    apiRefundFreightAddressVO.setShipName(receiveAddressVO.getShipName());
                    apiRefundFreightAddressVO.setShipPhone(receiveAddressVO.getShipPhone());
                    apiRefundFreightAddressVO.setShipArea(receiveAddressVO.getShipArea());
                    apiRefundFreightAddressVO.setShipAddress(receiveAddressVO.getShipAddress());
                    return apiRefundFreightAddressVO;
                }
            });
        }
    }

    @Override
    public List<ApiRefundStateVO> apiRefundState(Long accountId, List<Long> refundIdList) {
        return refundRepository.accountRefundState(accountId, refundIdList);
    }

    @Override
    public Page<ApiRefundVO> apiList(Long accountId, ApiRefundReq apiRefundReq) {
        RefundQuery refundQuery = TransferUtils.transfer(apiRefundReq, new Function<ApiRefundReq, RefundQuery>() {
            @Override
            public RefundQuery apply(ApiRefundReq apiRefundReq) {
                RefundQuery refundQuery = new RefundQuery();
                refundQuery.setId(apiRefundReq.getId());
                refundQuery.setIdList(apiRefundReq.getIdList());
                refundQuery.setChannelId(accountId);
                refundQuery.setRefundState(apiRefundReq.getRefundState());
                refundQuery.setRefundType(apiRefundReq.getRefundType());
                refundQuery.setCreateStartTime(apiRefundReq.getCreateBeginTime());
                refundQuery.setCreateEndTime(apiRefundReq.getCreateEndTime());
                refundQuery.setPageNo(apiRefundReq.getPageNo());
                refundQuery.setPageSize(apiRefundReq.getPageSize());
                return refundQuery;
            }
        });
        Page<RefundDTO> refundPageInfo = refundRepository.refundPage(refundQuery);
        return TransferUtils.transferPage(refundPageInfo,ApiRefundVO.class);
    }

    @Override
    public ApiRefundAggVO apiDetail(Long accountId, Long refundId) {
        RefundDTO refundDTO = refundRepository.refund(refundId);
        if(refundDTO == null){
            ThrowsException.exception(BaseErrorCode.PARAM, "refundId");
        }
        ApiRefundAggVO apiRefundAggVO = new ApiRefundAggVO();
        apiRefundAggVO.setRefund(TransferUtils.transfer(refundDTO, ApiRefundVO.class));
        List<RefundItemVO> refundItemVOList = refundDTO.getItem();
        apiRefundAggVO.setRefundItem(TransferUtils.transfers(refundItemVOList, ApiRefundItemVO.class));
        return apiRefundAggVO;
    }
}
