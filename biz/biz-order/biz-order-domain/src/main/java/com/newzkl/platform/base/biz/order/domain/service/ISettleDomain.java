package com.newzkl.platform.base.biz.order.domain.service;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.req.FreightSettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleGoodsCommand;
import com.newzkl.platform.base.biz.order.model.req.SettleGoodsQuery;
import com.newzkl.platform.base.biz.order.model.req.SettleOrderWaitCommand;
import com.newzkl.platform.base.biz.order.model.support.api.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.order.model.vo.ExecuteSettleRes;
import com.newzkl.platform.base.biz.order.model.vo.SettleGoodsVO;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
public interface ISettleDomain {
    /**
     * 结算商品信息表创建
     * @param settleGoodsCommand
     * @return
     */
    Long settleGoodsSave(SettleGoodsCommand settleGoodsCommand);
    /**
     * 执行结算
     * @param supplierId 供应商ID
     * @param settlementConfigRpcVO 结算配置
     * @param settleGoodsVOList 待结算商品
     * @param settleTime 结算时间
     * @return
     */
    ExecuteSettleRes executeSettle(Long supplierId, SettlementConfigOutVO settlementConfigRpcVO, List<SettleGoodsVO> settleGoodsVOList, LocalDateTime settleTime);

    /**
     * 执行结算
     * @param supplierId 供应商ID
     * @param settleOrderWaitVOList 待结算订单
     * @param settleTime 结算时间
     * @return
     */
    ExecuteSettleRes executeSettle2(Long supplierId, List<SettleOrderWaitVO> settleOrderWaitVOList, LocalDateTime settleTime);
    /**
     * 待结算订单记录创建
     * @param settleOrderWaitCommandList
     * @param settleType
     */
    void settleOrderWaitSave(List<SettleOrderWaitCommand> settleOrderWaitCommandList, Integer settleType);
    /**
     * 结算商品信息表-值对象列表
     * @param settleGoodsQuery
     * @return
     */
    Page<SettleGoodsVO> settleGoodsVOList(SettleGoodsQuery settleGoodsQuery);
    /**
     * 待结算运费记录创建
     * @param asList
     */
    void freightSettleOrderWaitSave(List<FreightSettleOrderWaitCommand> asList, RoleEnum.OrderType settleOrderType);

    /**
     * 关闭待结算单
     * @param skuOrderId
     * @return null:待结算记录未生成 0 待结算记录关闭失败 1 待结算记录关闭成功
     */
    Integer closeSettleOrder(Long skuOrderId, Long refundId);
}
