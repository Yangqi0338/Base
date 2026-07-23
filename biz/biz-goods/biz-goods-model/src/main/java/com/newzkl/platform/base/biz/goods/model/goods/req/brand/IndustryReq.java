package com.newzkl.platform.base.biz.goods.model.goods.req.brand;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 行业
 * @author fang
 */
@Data
public class IndustryReq extends BaseRes {
     /**
     * ID
     */
     private Long id;
     /**
     * 用户ID 非参数
     */
     private Long accountId;
     /**
     * 名称 查询
     */
     @NotBlank(message = "行业名称不能为空")
     private String name;
     /**
     * 描述
     */
     private String desc;
     /**
     * 分类ID集合 非参数
     */
     private String categoryIdList;
}