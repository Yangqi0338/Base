package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 选品列表参数
 * @date 2024/1/1718:49
 */
@Data
public class SelectListApiReq implements Serializable {
    /**
     * SPU_ID集合:不传可查询所有选品
     */
    private List<Long> spuIdList;
    /**
     * SPU名称:模糊查询
     */
    private String spuName;
    /**
     * 商品上下架状态 : 2 上架 3 下架
     */
    private String spuState;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 当前页:不传可查所有
     */
    private Integer pageNo = 0;
    /**
     * 每页的数量
     */
    private Integer pageSize = 0;
    /**
     * 排序字段 : 0 销量 , 1 金额
     */
    private List<Integer>  sortField;
    /**
     * 排序方式 : 0 升序 , 1 降序 , 与排序字段按下标一一对应
     */
    private List<Integer> sortMode;
    /**
     * 市场id
     */
    private Long marketId;
}
