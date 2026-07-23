package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 门店专区领域对象
 */
@Data
public class StoreZoneCreateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 专区名称
     */
    @NotNull(message = "专区名称不能为空")
    @Size(max = 20, message = "专区名称长度不能超过20")
    private String zoneName;

    /**
     * 专区副标题
     */
    @NotNull(message = "专区副标题不能为空")
    @Size(max = 10, message = "专区副标题长度不能超过10")
    private String zoneSubtitle;

    /**
     * 描述
     */
    @Size(max = 100, message = "专区描述长度不能超过100")
    private String zoneDescribe;

    /**
     * 专区背景图
     */
    @NotEmpty
    private List<String> backgroundImageList;

    /**
     * 专区商品集合
     */
    @NotEmpty
    private List<StoreZoneGoods> storeZoneGoodsList;

    /**
     * 专区商品
     */
    @Data
    public static class StoreZoneGoods {
        /**
         * 商品id
         */
        private Long goodsId;

        /**
         * 门店id
         */
        private Long storeId;

        /**
         * 门店名称
         */
        private String storeName;
    }

}