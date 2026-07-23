package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * spu属性
 * @author fang
 */
@Data
public class SpuAttributeVO extends BaseVO {
     /**
     * ID (查询)
     */
     private Long id;
     /**
     * spuId (查询)
     */
     private Long spuId;
     /**
     * 类型 0:销售属性 1:参数属性 (查询)
     */
     private Integer type;
     /**
     * 名称
     */
     @NotEmpty
     private String name;
     /**
     * 手动添加规格或参数的值，参数单值
      * 参数属性示例: ["黑色","白色"]
     */
     @NotEmpty
     private String value;
}