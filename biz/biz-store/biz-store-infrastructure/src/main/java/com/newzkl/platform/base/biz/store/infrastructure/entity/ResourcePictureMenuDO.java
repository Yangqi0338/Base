package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 
 */
@Data
public class ResourcePictureMenuDO implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String menuName;

    private Long accountId;

    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}