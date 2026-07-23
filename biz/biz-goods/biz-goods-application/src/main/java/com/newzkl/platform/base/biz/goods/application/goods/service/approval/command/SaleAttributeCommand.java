package com.newzkl.platform.base.biz.goods.application.goods.service.approval.command;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.common.ddd.model.check.DeleteCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 销售属性操作
 * @date 2023/8/79:31
 */
public class SaleAttributeCommand {
    @Data
    public static class Add implements AddCommand {
        /**
         * SpuId
         */
        private Long spuId;
        /**
         * sku信息
         */
        private List<SkuDTO> skuList;
        /**
         * 最新商品规格值
         */
        private List<SpuAttributeVO> attributeVOList;
    }
    @Data
    public static class Update implements UpdateCommand {

    }
    @Data
    public static class Delete implements DeleteCommand {
        Long spuId;
        /**
         * 最新商品规格值
         */
        private List<SpuAttributeVO> attributeVOList;
    }
    @Data
    public static class Check implements CheckCommand {
        /**
         * SpuId
         */
        private Long spuId;
        /**
         * 旧spu信息
         */
        SpuVO oldSpu;
        /**
         * 新增的sku信息
         */
        private List<SkuDTO> skuList;
        /**
         * 最新商品规格值
         */
        private List<SpuAttributeVO> attributeVOList;
    }
}
