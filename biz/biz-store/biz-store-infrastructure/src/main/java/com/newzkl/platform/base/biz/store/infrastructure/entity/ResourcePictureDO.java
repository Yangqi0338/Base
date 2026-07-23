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
public class ResourcePictureDO implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String pictureName;

    private String resourceLink;

    private Long accountId;

    private Long menuId;

    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}