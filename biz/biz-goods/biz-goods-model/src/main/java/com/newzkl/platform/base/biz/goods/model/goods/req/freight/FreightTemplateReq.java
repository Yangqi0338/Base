package com.newzkl.platform.base.biz.goods.model.goods.req.freight;

import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreePostConditionVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.RegionVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 运费模板
 * @author fang
 */
@Data
public class FreightTemplateReq {
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
     @NotBlank(message = "模板名称不能为空")
     private String name;
     /**
     * 是否包邮 0：不包邮  1：包邮
     */
     private Integer freePost;
     /**
     * 计价方式 1:按件数 2:按重量 3:按体积
     */
     @NotNull(message = "计费方式不能为空")
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
     private FreePostConditionVO freePostCondition;
     /**
      * 地区运费规则
      */
     @NotEmpty(message = "地区运费规则不能为空")
     private List<RegionVO> regionSpec;
}