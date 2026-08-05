package com.newzkl.platform.base.biz.goods.model.goods.query.audit;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 商品上传审核数据
* @author fang
*/
@Data
public class AuditDataSpuQuery extends PageQuery implements Serializable {

    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * SPU集合
     */
    private List<Long> spuIdList;
    /**
    * ID (查询)
    */
    private Long id;
    /**
    * 发起人账号ID (查询)
    */
    private Long accountId;
    /**
     * 发起人企业角色ID (查询)
     */
    private RoleEnum.CompanyRole role;
    /**
     * 审批状态
     */
    private List<String> stateList;
    /**
     * 审批状态
     * @see AuditEnum.State
     */
    private String state;
    /** 是否新增 */
    private Integer isNew;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 供货价起始
     */
    private Integer supplyPriceStart;
    /**
     * 供货价结束
     */
    private Integer supplyPriceEnd;
    /**
     * 商品分类
     */
    private Long categoryId;
}
