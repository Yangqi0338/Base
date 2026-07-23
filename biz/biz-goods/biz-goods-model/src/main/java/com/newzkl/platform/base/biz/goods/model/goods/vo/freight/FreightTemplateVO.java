package com.newzkl.platform.base.biz.goods.model.goods.vo.freight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 运费模板
 * @author fang
 */
@Data
public class FreightTemplateVO extends BaseRes {
     /**
     * ID
     */
     private Long id;
     /**
      * 账号ID
      */
     private Long accountId;
     /**
     * 名称 查询
     */
     private String name;
     /**
     * 是否包邮 0：不包邮  1：包邮
     */
     private Integer freePost;
     /**
     * 计价方式 1:按件数 2:按重量 3:按体积
     */
     private Integer pricingManner;
     /**
     * 是否指定条件包邮 0:否 1:是
     */
     private Integer isFreePostCondition;
     /**
     * 是否默认模板
     */
     private Integer isDefault;
     /**
     * 包邮条件
     */
     @JsonIgnore
     private String freePostCondition;
     /**
     * 地区运费规则
     */
     private String regionSpec;
}