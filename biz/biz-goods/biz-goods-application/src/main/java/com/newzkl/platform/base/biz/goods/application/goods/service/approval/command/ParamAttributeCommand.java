package com.newzkl.platform.base.biz.goods.application.goods.service.approval.command;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.common.ddd.model.check.DeleteCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 参数属性操作
 * @date 2023/8/79:30
 */
public class ParamAttributeCommand {
    @Data
    public static class Add implements AddCommand {
        /**
         * SpuId
         */
        private Long spuId;
        List<SpuAttributeVO> attributeVOList;
    }
    @Data
    public static class Update implements UpdateCommand {
        /**
         * SpuId
         */
        private Long spuId;
        List<SpuAttributeVO> attributeVOList;
    }
    @Data
    public static class Delete implements DeleteCommand {
        /**
         * SpuId
         */
        private Long spuId;
        List<Long> attributeIdList;
    }
    @Data
    public static class Check implements CheckCommand {

    }
}
