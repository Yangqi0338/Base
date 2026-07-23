package com.newzkl.platform.base.biz.goods.model.goods.vo.brand;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum.ApprovalStatus;
import lombok.Data;

/**
 * 品牌
 * @author fang
 */
@Data
public class BrandVO extends BaseVO {

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