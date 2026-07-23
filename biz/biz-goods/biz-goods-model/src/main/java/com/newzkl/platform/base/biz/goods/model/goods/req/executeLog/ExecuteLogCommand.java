package com.newzkl.platform.base.biz.goods.model.goods.req.executeLog;

import lombok.Data;

/**
* 操作日志
* @author fang
*/
@Data
public class ExecuteLogCommand {
    /**
     * ID
     */
    private Long id;
    /**
     * 操作类型
     */
    private Integer type;
    /**
     * 操作主键
     */
    private Long targetId;
    /**
     * 操作人名称
     */
    private String executeUserName;
    /**
     * 原数据
     */
    private String oldData;
    /**
     * 改动数据
     */
    private String updateData;
}
