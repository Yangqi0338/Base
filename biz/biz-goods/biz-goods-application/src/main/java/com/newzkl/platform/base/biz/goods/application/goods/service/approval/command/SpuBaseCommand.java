package com.newzkl.platform.base.biz.goods.application.goods.service.approval.command;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.common.ddd.model.check.DeleteCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.Data;

/**
 * @author muc_fang
 * @Description: SPU基础信息操作
 * @date 2023/8/79:30
 */
public class SpuBaseCommand{

    @Data
    public static class Add implements AddCommand {
    }
    @Data
    public static class Update implements UpdateCommand {
        private SpuDTO spuDTO;
    }
    @Data
    public static class Delete implements DeleteCommand {

    }
    @Data
    public static class Check implements CheckCommand {

    }
}
