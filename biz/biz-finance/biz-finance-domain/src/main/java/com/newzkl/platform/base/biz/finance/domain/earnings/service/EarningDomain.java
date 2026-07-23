package com.newzkl.platform.base.biz.finance.domain.earnings.service;


import java.util.List;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;

/**
 * @author niu
 * @description: 分润查询接口
 * @date 2023/12/23 11:08
 */
public interface EarningDomain {

    /**
     * 查询分润记录
     *
     * @param query
     * @return
     */
    List<EarningRecordVO> queryEarningRecord(EarningRecordQuery query);

    /**
     * 查询客户商品待分润金额
     *
     * @param query
     * @return
     */
    Integer queryAccountGoodsWaitEarningAmount(EarningRecordQuery query);

    /**
     * 更新分润记录结算状态
     *
     * @param skuOrderId
     * @param state
     */
    void alterEarningRecordState(Long skuOrderId, Integer state);

    /**
     * 查询累计分润金额
     *
     * @return
     */
    TotalEarningVO queryTotalEarning();

    /**
     * app查询分润金额
     *
     * @return
     */
    List<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery req);

    /**
     * 修改分润记录
     *
     * @return
     */
    Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req);
}
