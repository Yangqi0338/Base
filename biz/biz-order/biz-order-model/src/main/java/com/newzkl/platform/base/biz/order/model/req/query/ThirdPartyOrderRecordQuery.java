package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 三方订单记录查询请求对象
 *
 * @author fang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyOrderRecordQuery extends PageQuery {

    /**
     * 业务订单号
     */
    private String bizOrderNo;

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

    /**
     * 下次重试时间上限(lt)
     * @ext 用于扫描超时未重试的记录
     */
    private LocalDateTime nextRetryTimeBefore;
}