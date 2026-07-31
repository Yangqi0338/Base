package com.newzkl.platform.base.biz.goods.model.goods.req.report;

import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报告实体
 *
 * @author kc
 */
@Data
public class ReportReq extends ReportQuery implements Serializable {

    /** 主键ID */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 60, message = "名称长度不能超过60个字符")
    private String name;
    /**
     * 路径
     */
    @NotBlank(message = "文件不能为空")
    private String path;
    /**
     * 分类id列表
     */
    @NotEmpty(message = "关联分类不能为空")
    private List<Long> categoryIdList;
    /**
     * 商品id列表
     */
    @NotEmpty(message = "关联商品不能为空")
    private List<Long> spuIdList;
}