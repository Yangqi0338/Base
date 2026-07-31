package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import java.util.List;

/**
 * 3.11 根据品牌ID获取品牌接口响应参数
 */
@Data
public class HuiDingHuoGetBrandsRes extends HuiDingHuoBaseRes<List<HuiDingHuoGetBrandsRes.BrandDetail>> {
    
    /**
     * 品牌详情
     */
    @Data
    public static class BrandDetail {
        /**
         * 品牌ID
         */
        private String id;
        
        /**
         * 品牌名称
         */
        private String name;
        
        /**
         * 品牌描述
         */
        private String desc;
        
        /**
         * 品牌logo URL
         */
        private String logoUrl;
        
        /**
         * 品牌海报URL
         */
        private String posterUrl;
    }
}
