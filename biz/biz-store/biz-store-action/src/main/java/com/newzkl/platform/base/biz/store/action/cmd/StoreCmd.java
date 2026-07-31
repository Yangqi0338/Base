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
        /** 门店ID */
        private Long storeId;
    }

    /**
     * 门店 ID 列表命令
     */
    @Data
    public static class IDList {
        /** 门店ID列表 */
        @NotEmpty
        private List<Long> storeIdList;
    }

    /**
     * 门店编辑命令
     */
    @Data
    public static class Edit {
        /** 主键ID */
        @NotNull(message = "ID不能为空")
        private Long id;
        /** 门店保存请求 */
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
