package com.newzkl.platform.base.biz.goods.model.biz.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class CategoryReq extends BaseReq {
    /**
     * 用户ID 非参数
     */
    private Long accountId;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 名称 查询
     */
    private String name;
    /**
     * 图片
     */
    private String img;
    /**
     * 描述
     */
    private String desc;
    /**
     * 子分类
     */
    private List<CategoryReq> children;
}