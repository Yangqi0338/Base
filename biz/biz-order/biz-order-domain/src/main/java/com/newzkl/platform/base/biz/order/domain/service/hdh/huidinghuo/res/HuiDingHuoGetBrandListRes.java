package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import java.util.List;

/**
 * 3.10 品牌列表接口响应参数
 */
@Data
public class HuiDingHuoGetBrandListRes extends HuiDingHuoBaseRes<HuiDingHuoGetBrandListRes.BrandListData> {
    
    /**
     * 品牌列表数据
     */
    @Data
    public static class BrandListData {
        /**
         * 品牌列表
         */
        private List<BrandItem> dataList;
        
        /**
         * 下一页
         */
        private Integer nextPage;
        
        /**
         * 总数
         */
        private Long total;
        
        /**
         * 总页数
         */
        private Long totalPage;
    }
    
    /**
     * 品牌项
     */
    @Data
    public static class BrandItem {
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
