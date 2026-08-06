package com.newzkl.platform.base.biz.goods.model.biz.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 第三方商品同步记录查询条件
 */
@Data
@Builder
public class ThirdPartyGoodsRecordQuery {

    private String outSpuId;

    private String interfaceName;

    private PlatformTypeEnum platformType;

    private CommonEnum.RequestStatusEnum requestStatus;
}
