package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PackOrderRpcVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;

import java.util.List;

/**
 * @author niu
 * @description: 消费
 * @date 2023/12/20 17:19
 */
public interface ConsumeEarningDataRepository {

    /**
     * 保存分润记录
     *
     * @param earningInfos
     */
    void saveEarningRecord(List<EarningRecordVO> earningInfos);

    /**
     * 查询分润记录分页
     *
     * @param query 分润记录查询
     * @return 分润记录分页
     */
    Page<EarningRecordVO> queryEarningRecord(EarningRecordQuery query);

    /**
     * 查询 APP 分润记录分页
     *
     * @param query 分润记录查询
     * @return APP 分润记录分页
     */
    Page<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery query);

    /**
     * 查询客户带分润金额
     *
     * @param query
     * @return
     */
    Integer queryAccountWaitEarningAmount(EarningRecordQuery query);

    /**
     * 查询累计分润金额
     *
     * @return
     */
    TotalEarningVO queryTotalEarning();

    /**
     * 查询甄选师礼包订单信息
     *
     * @param orderNo
     * @return
     */
    PackOrderRpcVO queryPickPackOrder(Long orderNo);

    /**
     * 批量查询甄选师礼包订单信息
     *
     * @param orderNo
     * @return
     */
    List<PackOrderRpcVO> queryPickPackOrder(List<Long> orderNo);

    /**
     * 获取角色上级ID
     *
     * @param client
     * @param accountId
     * @return
     */
    UpIdRes upId(CommonEnum.Client client, Long accountId);

    /**
     * 发送mq消息
     *
     * @param skuOrderWaitEarningVO
     */
    void sendMessage(SkuOrderWaitEarningVO skuOrderWaitEarningVO);

    /**
     * 更新分润记录结算状态
     *
     * @param skuOrderId
     * @param state
     */
    void alterEarningRecordState(Long skuOrderId, Integer state);

    /**
     * 修改分润记录
     */
    Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req);

}
