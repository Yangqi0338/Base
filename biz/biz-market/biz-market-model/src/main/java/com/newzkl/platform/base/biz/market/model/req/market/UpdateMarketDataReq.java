package com.newzkl.platform.base.biz.market.model.req.market;

import com.newzkl.platform.base.biz.market.model.enums.MarketEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 更新市场数据请求对象
 * @date 2023/12/5 16:28
 */
@Data
public class UpdateMarketDataReq {

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 更新类型
     */
    private MarketEnum.NumType type;

    /**
     * 更新数值
     */
    private Integer alterNum;

    public static UpdateMarketDataReq buildUpdateMarketDataReq(Long marketId, MarketEnum.NumType marketNumType, Integer num) {
        UpdateMarketDataReq updateMarketDataReq = new UpdateMarketDataReq();
        updateMarketDataReq.setMarketId(marketId);
        updateMarketDataReq.setType(marketNumType);
        updateMarketDataReq.setAlterNum(num);
        return updateMarketDataReq;
    }
}
