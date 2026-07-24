package com.newzkl.platform.base.biz.store.model.template.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 样板店分页查询
 *
 * @author fang
 */
@Data
public class ModelShopStorePageQuery extends BizPageQuery {

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 样板店id
     */
    @NotNull(message = "样板店id不能为空")
    private Long modeShopId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 使用时间左
     */
    private String useTimeL;

    /**
     * 使用时间右
     */
    private String useTimeR;

    /**
     * 是否正在使用
     */
    private Boolean inUse;

    /**
     * 门店ID集合
     */
    private List<Long> storeIdList;

}