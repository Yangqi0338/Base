package com.newzkl.platform.base.biz.goods.model.goods.vo.brand;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum.ApprovalStatus;
import lombok.Data;

/**
 * 品牌
 * @author fang
 */
@Data
public class BrandVO extends BaseRes {

     /**
     * 添加人
     */
     private Long accountId;
     /**
     * 名称
     */
     private String name;
     /**
     * LOGO
     */
     private String logo;
    /**
     * 状态
     */
    private ApprovalStatus state;
    /**
     * 类目ID
     */
    private String categoryIdList;
}