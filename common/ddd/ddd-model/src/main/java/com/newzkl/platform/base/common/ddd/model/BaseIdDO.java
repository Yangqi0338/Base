package com.newzkl.platform.base.common.ddd.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础数据库Id对象
 */
@NoArgsConstructor
@Data
public class BaseIdDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
     * 主键ID
     * */
    @TableId(type = IdType.ASSIGN_ID)
    protected Long id;
}
