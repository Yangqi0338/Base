package com.newzkl.platform.base.biz.store.model.store.res;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店客户
 */
@Data
public class StoreAccountExportRes implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty("客户ID")
    private Long accountId;

    @ExcelProperty("客户昵称")
    private String nickname;

    @ExcelProperty("头像")
    private String headImg;

    @ExcelProperty("账号")
    private String username;



    @ExcelProperty("绑定时间")
    private LocalDateTime createTime;

    /**
     * 0:未拉黑，1已拉黑
     */
    @ExcelProperty("关系状态")
    private String relationType;

}