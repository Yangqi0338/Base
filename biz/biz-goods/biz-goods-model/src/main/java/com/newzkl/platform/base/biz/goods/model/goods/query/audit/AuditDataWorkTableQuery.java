package com.newzkl.platform.base.biz.goods.model.goods.query.audit;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import lombok.Data;

import java.util.List;

/**
* 工单审核数据
* @author fang
*/
@Data
public class AuditDataWorkTableQuery extends PageQuery {

    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 操作类型  1 修改  2 新增  3 删除
     */
    private Integer operateType;
    /**
     * 操作目标  1 SPU基本信息  2 销售规格  3 属性规格   4 SKU信息 5 SPU状态
     */
    private Integer operateTarget;
    /**
    * ID (查询)
    */
    private Long id;
    /**
    * 商品ID (查询)
    */
    private Long spuId;
    /**
     * 商品名称 (查询)
     */
    private String spuName;
    /**
    * 发起人账号ID (查询)
    */
    private Long accountId;
    /**
     * 审批状态
     */
    private List<String> stateList;
    /**
     * 审批状态
     * @see AuditEnum.State
     */
    private String state;
}
