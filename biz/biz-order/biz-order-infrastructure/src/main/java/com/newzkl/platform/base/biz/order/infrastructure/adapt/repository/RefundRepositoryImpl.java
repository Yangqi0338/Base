package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.lang.Opt;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundErrorCode;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.RefundDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SkuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.SkuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.order.vo.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class RefundRepositoryImpl extends ServiceImpl<RefundDAO, RefundDO> implements RefundRepository {

    private final SkuOrderDAO skuOrderDAO;
    private final SpuOrderDAO spuOrderDAO;

    @Override
    public Long refundSave(Refund refund) {
        RefundDO refundDO = TransferUtils.transfer(refund, RefundDO::new);
        this.save(refundDO);
        Integer number = 0;
        for (RefundItemVO refundItem : refund.getItem()) {
            int count = skuOrderDAO.updateSkuRefundingQuantity(refund.getSpuOrderNo(), refundItem.getSkuId(), refundItem.getCount());
            if (count != 1){
                throw new ScmException(RefundErrorCode.SKU_REFUNDING_COUNT);
            }
            number = number + refundItem.getCount();
        }
        spuOrderDAO.updateSkuRefundingCount(refund.getSpuOrderNo(), number);
        return null;
    }

    @Override
    public void refundUpdate(Refund refund) {
        RefundDO refundDO = TransferUtils.transfer(refund, RefundDO::new);
        this.updateById(refundDO);
    }

    @Override
    public Refund refund(Long refundId) {
        RefundDO refundDO = this.getById(refundId);
        return TransferUtils.transfer(refundDO, Refund::new);
    }

    @Override
    public Refund refundBySpuOrderId(String spuOrderNo) {
        LambdaQueryWrapper<RefundDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundDO::getSpuOrderNo, spuOrderNo);
        RefundDO refundDO = this.getOne(wrapper);
        return TransferUtils.transfer(refundDO, Refund::new);
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        RefundDO refundDO = this.getById(refundId);
        return TransferUtils.transfer(refundDO, RefundVO::new);
    }

    @Override
    public RefundVO refundVoBySpuOrderId(String spuOrderNo) {
        LambdaQueryWrapper<RefundDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundDO::getSpuOrderNo, spuOrderNo);
        RefundDO refundDO = this.getOne(wrapper);
        return TransferUtils.transfer(refundDO, RefundVO::new);
    }

    @Override
    public Page<RefundVO> refundVOList(RefundPageReq refundQuery) {
        // 创建分页对象
        Page<RefundDO> doPage = new Page<>(refundQuery.getCurrent(), refundQuery.getSize());

        // 构建查询条件
        LambdaQueryWrapper<RefundDO> wrapper = buildRefundQueryWrapper(refundQuery);

        // 执行查询
        Page<RefundDO> resultPage = baseMapper.selectPage(doPage, wrapper);

        // 转换返回结果
        Page<RefundVO> voPage = TransferUtils.transferPage(resultPage, RefundVO::new);

        // 对返回的数据进行额外处理，比如设置枚举转换等
        List<RefundVO> records = voPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records = records.stream()
                    .filter(Objects::nonNull)
                    .map(this::processRefundVO)
                    .collect(Collectors.toList());
            voPage.setRecords(records);
        }

        return voPage;
    }

    /**
     * 构建售后查询条件包装器
     *
     * @param req 查询请求参数
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<RefundDO> buildRefundQueryWrapper(RefundPageReq req) {
        LambdaQueryWrapper<RefundDO> wrapper = new LambdaQueryWrapper<>();

        // 精确匹配查询条件
        Optional.ofNullable(req.getId())
                .ifPresent(id -> wrapper.eq(RefundDO::getId, id));

        Optional.ofNullable(req.getSpuOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(spuOrderNo -> wrapper.eq(RefundDO::getSpuOrderNo, spuOrderNo));

        Optional.ofNullable(req.getMerchantId())
                .ifPresent(merchantId -> wrapper.eq(RefundDO::getMerchantId, merchantId));

        Optional.ofNullable(req.getChannelId())
                .ifPresent(channelId -> wrapper.eq(RefundDO::getChannelId, channelId));

        Optional.ofNullable(req.getSupplierId())
                .ifPresent(supplierId -> wrapper.eq(RefundDO::getSupplierId, supplierId));

        Optional.ofNullable(req.getRefundState())
                .ifPresent(refundState -> wrapper.eq(RefundDO::getRefundState, refundState));

        Optional.ofNullable(req.getRefundType())
                .ifPresent(refundType -> wrapper.eq(RefundDO::getRefundType, refundType));

        Optional.ofNullable(req.getOrderType())
                .ifPresent(orderType -> wrapper.eq(RefundDO::getOrderType, orderType));

        Optional.ofNullable(req.getMemberId())
                .ifPresent(memberId -> wrapper.eq(RefundDO::getMemberId, memberId));

        // ID集合查询
        Optional.ofNullable(req.getIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(idList -> wrapper.in(RefundDO::getId, idList));

        Optional.ofNullable(req.getRefundStateList())
                .filter(list -> !list.isEmpty())
                .ifPresent(refundStateList -> wrapper.in(RefundDO::getRefundState, refundStateList));

        // 不可见来源订单状态排除查询
        Optional.ofNullable(req.getFromOrderStateNot())
                .filter(list -> !list.isEmpty())
                .ifPresent(fromOrderStateNot -> wrapper.notIn(RefundDO::getFromOrderState, fromOrderStateNot));

        // 时间范围查询
        Optional.ofNullable(req.getCreateBeginTime())
                .ifPresent(createBeginTime -> wrapper.ge(RefundDO::getCreateTime, new java.sql.Timestamp(createBeginTime)));

        Optional.ofNullable(req.getCreateEndTime())
                .ifPresent(createEndTime -> wrapper.le(RefundDO::getCreateTime, new java.sql.Timestamp(createEndTime)));

        // 状态变化时间查询
        Optional.ofNullable(req.getStateTimeLess())
                .ifPresent(stateTimeLess -> wrapper.lt(RefundDO::getStateTime, stateTimeLess));

        // 用户名和昵称模糊查询
        Optional.ofNullable(req.getMemberIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(idList -> wrapper.in(RefundDO::getMemberId, idList));


        // 按创建时间降序排列
        wrapper.orderByDesc(RefundDO::getCreateTime);

        return wrapper;
    }

    /**
     * 处理RefundVO，进行枚举转换等操作
     *
     * @param refundVO 待处理的RefundVO对象
     * @return 处理后的RefundVO对象
     */
    private RefundVO processRefundVO(RefundVO refundVO) {
        // 设置状态时间戳
        if (refundVO.getStateTime() != null) {
            refundVO.setStateTimeTimestamp(refundVO.getStateTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        // 处理退货信息拓展字段
        if (StringUtils.isNotBlank(refundVO.getFreightExt())) {
            try {
                FreightExtVO freightExt = JSONObject.parseObject(refundVO.getFreightExt(), FreightExtVO.class);
                refundVO.setFreightExtDto(freightExt);
            } catch (Exception e) {
                // 解析失败时忽略或记录日志
            }
        }

        // 处理售后明细
        if (refundVO.getItemStr() != null) {
            refundVO.getItem(); // 触发item的懒加载
        }

        return refundVO;
    }

    @Override
    public List<SkuRefundRes> skuRefundResList(String orderNo, List<Long> skuIds) {

        return skuOrderDAO.skuRefundResList(orderNo, skuIds);
    }

    @Override
    public void updateState(Refund refundEdit, Long refundId, OrderEnum.OrderType orderType, RefundEnum.State sourceState, RefundEnum.State toState,
                            Long channelId) {
        RefundDO refundDO = new RefundDO();
        if(refundEdit != null){
            refundDO = TransferUtils.transfer(refundEdit, RefundDO::new);
        }
        refundDO.setStateTime(LocalDateTime.now());
        baseMapper.updateState(refundDO, refundId, sourceState, toState);
    }

    @Override
    public int updateStateWithFrom(OrderEnum.OrderType orderType, Long refundId, RefundEnum.State sourceState, RefundEnum.State toState,
                                   Long channelId) {

        return baseMapper.updateStateWithFrom(refundId, sourceState, toState);
    }

    @Override
    public List<ApiRefundStateVO> accountRefundState(Long accountId, List<Long> refundIdList) {
        return baseMapper.accountRefundState(accountId, refundIdList);
    }

    @Override
    public List<SpuRefundRes> spuRefundResList(String orderNo, List<Long> spuIds) {
        return spuOrderDAO.spuRefundResList(orderNo, spuIds);
    }

    @Override
    public void skuOrderEditForRefundClose(String spuOrderNo, List<RefundItemVO> item) {
        List<String> skuOrderIdList = item.stream().map(RefundItemVO::getSkuOrderNo).collect(Collectors.toList());
        spuOrderDAO.cutSkuOrderRefundingNumber(spuOrderNo, skuOrderIdList);
        skuOrderDAO.skuOrderEditForRefundClose(skuOrderIdList);
    }

    @Override
    public void skuOrderEditForRefundPass(String spuOrderNo, List<RefundItemVO> item) {
        List<String> skuOrderIdList = item.stream().map(RefundItemVO::getSkuOrderNo).collect(Collectors.toList());
        spuOrderDAO.cutSkuOrderRefundingNumber(spuOrderNo, skuOrderIdList);
        skuOrderDAO.skuOrderEditForRefundPass(skuOrderIdList);
    }

    @Override
    public Long refundIdByOutRefundId(String returnSn) {
        return baseMapper.refundIdByOutRefundId(returnSn);
    }

    @Override
    public List<Refund> refundVOListForAutoAgree(RefundPageReq refundQuery) {
        List<RefundDO> refundDOS = baseMapper.refundVOListForAutoAgree(refundQuery);
        if (CollectionUtils.isEmpty(refundDOS)){
            return Collections.emptyList();
        }
        return TransferUtils.transfers(refundDOS, Refund::new);
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId) {
        String outRefundAddress = baseMapper.getOutRefundAddress(spuOrderNo, spuId);
        if(StringUtils.isEmpty(outRefundAddress)){
            return null;
        }
        return JSONObject.parseObject(outRefundAddress, ApiRefundFreightAddressVO.class);
    }

    @Override
    public Integer countTotalRefundingByMemberId(Long memberId) {
        return baseMapper.countTotalRefundingByMemberId(memberId);
    }

    @Override
    public Integer countTotalRefundingByStoreId(Long storeId) {
        return baseMapper.countTotalRefundingByStoreId(storeId);
    }

    @Override
    public List<RefundExcelVO> exportRefund(RefundPageReq refundQuery) {
        List<RefundVO> list = refundVOList(refundQuery).getRecords();
        return TransferUtils.transfers(list, (vo) -> {
            RefundExcelVO refundExcelVO = TransferUtils.transfer(vo, RefundExcelVO::new);

            OrderEnum.OrderType orderType = OrderEnum.OrderType.getByCode(Integer.valueOf(refundExcelVO.getOrderType()));
            refundExcelVO.setOrderType(Opt.ofNullable(orderType).map(OrderEnum.OrderType::getInfo).orElse(""));
            SpuEnum.ChannelType channelType = SpuEnum.ChannelType.getByCode(Integer.valueOf(refundExcelVO.getSpuChannelType()));
            refundExcelVO.setSpuChannelType(Opt.ofNullable(channelType).map(SpuEnum.ChannelType::getValue).orElse(""));
            RefundEnum.State state = RefundEnum.State.getByCode(Integer.valueOf(refundExcelVO.getRefundState()));
            refundExcelVO.setRefundState(Opt.ofNullable(state).map(RefundEnum.State::getInfo).orElse(""));
            RefundEnum.RefundType refundType = RefundEnum.RefundType.getByCode(Integer.valueOf(refundExcelVO.getRefundType()));
            refundExcelVO.setRefundType(Opt.ofNullable(refundType).map(RefundEnum.RefundType::getInfo).orElse(""));

            return refundExcelVO;
        });
    }
}
