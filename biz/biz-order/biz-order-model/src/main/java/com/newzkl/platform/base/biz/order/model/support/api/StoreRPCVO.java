package com.newzkl.platform.base.biz.order.model.support.api;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 门店。
 *
 * <p>迁移: 跨域 goods 结构 {@code com.zkl.scm.goods.rpc.model.store.StoreRPCVO}
 * 降级为 order 本地 DTO。</p>
 *
 * @author KC
 */
@Data
public class StoreRPCVO extends BaseVO {
    /** 门店名称。 */
    private String name;
    /** logo。 */
    private String logo;
    /** 地址。 */
    private String address;
    /** 经度。 */
    private Double longitude;
    /** 纬度。 */
    private Double latitude;
    /** 渠道商ID。 */
    private Long channelId;
    /** 管理员ID。 */
    private Long managerId;
    /** 样板店ID。 */
    private Long modelShopId;
    /** 是否是样板店。 */
    private Integer isModelShop;
    /** 售后地址。 */
    private String refundAddress;
    /** 选品数量。 */
    private Integer selectionNumber;
    /** 自营商品数量。 */
    private Integer customNumber;
    /** 成交笔数。 */
    private Integer dealerNumber;
    /** 成交金额。 */
    private Integer dealerAmount;
    /** 总客户数。 */
    private Integer customCount;
    /** 样式code。 */
    private String styleCode;
}
