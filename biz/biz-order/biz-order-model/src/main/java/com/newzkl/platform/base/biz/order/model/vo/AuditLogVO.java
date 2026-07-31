package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1217:46
 */
@Data
public class AuditLogVO {

    private List<Item> itemList;

    @Data
    public static class Item{
        private String roleName;
        private String remark;
    }
}
