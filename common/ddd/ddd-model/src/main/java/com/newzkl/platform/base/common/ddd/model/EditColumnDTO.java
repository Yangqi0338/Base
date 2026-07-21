package com.newzkl.platform.base.common.ddd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2917:20
 */
@Data
@AllArgsConstructor
public class EditColumnDTO {
    private String name;
    private Integer count;
}
