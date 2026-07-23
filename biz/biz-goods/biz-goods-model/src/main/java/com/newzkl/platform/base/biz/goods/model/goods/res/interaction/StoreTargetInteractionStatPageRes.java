package com.newzkl.platform.base.biz.goods.model.goods.res.interaction;

import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import lombok.Data;

import java.util.List;

/**
 * 门店对象互动统计分页返回结果
 * @author sijiwang
 */
@Data
public class StoreTargetInteractionStatPageRes {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 分页数据列表
     */
    private List<StoreTargetInteractionStat> list;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Long totalPages;

    // 快捷构造方法
    public static StoreTargetInteractionStatPageRes build(Long total, List<StoreTargetInteractionStat> list, Integer pageNum, Integer pageSize) {
        StoreTargetInteractionStatPageRes result = new StoreTargetInteractionStatPageRes();
        result.setTotal(total);
        result.setList(list);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPages(total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        return result;
    }
}