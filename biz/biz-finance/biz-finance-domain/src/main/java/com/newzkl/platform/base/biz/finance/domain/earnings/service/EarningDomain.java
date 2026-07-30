package com.newzkl.platform.base.biz.finance.domain.earnings.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 查询分润记录分页
     *
     * @param query 分润记录查询
     * @return 分润记录分页
     */
    Page<EarningRecordVO> queryEarningRecord(EarningRecordQuery query);

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
     * app查询分润记录分页
     *
     * @param req 分润记录查询
     * @return APP 分润记录分页
     */
    Page<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery req);

    /**
     * 修改分润记录
     *
     * @return
     */
    Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req);
}
