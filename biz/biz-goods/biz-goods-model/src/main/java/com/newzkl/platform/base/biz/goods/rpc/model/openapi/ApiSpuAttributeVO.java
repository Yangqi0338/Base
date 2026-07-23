package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * spu属性
 * @author fang
 */
@Data
public class ApiSpuAttributeVO implements Serializable {
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