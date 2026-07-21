package com.newzkl.platform.base.common.ddd.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础视图实体类
 *
 * @author god
 */
@Setter
@Getter
public class BaseIdVO implements Serializable {

    /*** 主键*/
    protected Long id;

}
