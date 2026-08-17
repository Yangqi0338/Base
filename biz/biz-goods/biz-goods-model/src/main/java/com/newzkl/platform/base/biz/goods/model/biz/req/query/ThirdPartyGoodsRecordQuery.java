package com.newzkl.platform.base.biz.goods.model.biz.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 第三方商品同步记录查询条件
 */
@Data
@Builder
public class ThirdPartyGoodsRecordQuery {

    /**
     * 外部SPU ID
     */
    private String outSpuId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 平台类型
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;

    /**
     * 请求状态
     */
    private CommonEnum.RequestStatusEnum requestStatus;
}
