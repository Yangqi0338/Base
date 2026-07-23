package com.newzkl.platform.base.biz.goods.application.goods.service.approval.command;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.common.ddd.model.check.DeleteCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: SKU基础信息操作
 * @date 2023/8/79:30
 */
public class SkuBaseCommand{
    @Data
    public static class Add implements AddCommand {

    }
    @Data
    public static class Update implements UpdateCommand {
        private Long spuId;
        private List<SkuDTO> skuVOList;
        /**
         * 销售价审核人账号 : 工单审核时填写
         */
        private String adminUserName;
    }
    @Data
    public static class Delete implements DeleteCommand {

    }
    @Data
    public static class Check implements CheckCommand {

    }
}
