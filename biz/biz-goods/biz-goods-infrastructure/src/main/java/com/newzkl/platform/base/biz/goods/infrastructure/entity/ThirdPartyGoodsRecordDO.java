package com.newzkl.platform.base.biz.goods.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 第三方商品同步记录数据对象 (DO)
 */
@Data
@TableName
public class ThirdPartyGoodsRecordDO extends BaseDO {

    /**
     * 平台类型
     */
    private ThirdPartyOrderEnum.PlatformTypeEnum platformType;

    /**
     * 外部商品(SPU)ID
     */
    private String outSpuId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 请求参数
     */
    @JsonSerializable
    private String requestJson;

    /**
     * 响应结果
     */
    @JsonSerializable
    private String responseJson;

    /**
     * 请求状态
     */
    private Integer requestStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;
}
