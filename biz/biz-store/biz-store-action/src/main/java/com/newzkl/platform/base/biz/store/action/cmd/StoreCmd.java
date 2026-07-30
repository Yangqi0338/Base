package com.newzkl.platform.base.biz.store.action.cmd;

import com.newzkl.platform.base.biz.store.model.store.req.StoreSaveReq;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 门店入参命令集合
 *
 * @author fang
 */
public class StoreCmd {

    /**
     * 门店 ID 命令
     */
    @Data
    public static class ID {
        private Long storeId;
    }

    /**
     * 门店 ID 列表命令
     */
    @Data
    public static class IDList {
        @NotEmpty
        private List<Long> storeIdList;
    }

    /**
     * 门店编辑命令
     */
    @Data
    public static class Edit {
        @NotNull(message = "ID不能为空")
        private Long id;
        private StoreSaveReq storeSaveReq;
        /**
         * 联系人姓名
         */
        private String contactName;
        /**
         * 联系人电话
         */
        private String contactPhone;
    }
}
