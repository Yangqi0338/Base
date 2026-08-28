CREATE TABLE `store_zone_background_image` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `zone_code` varchar(255) NULL COMMENT '专区code',
  `background_image` varchar(255) NULL COMMENT '图片链接',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_store_zone_background_image_zone_code`(`zone_code`) COMMENT '专区code'
) COMMENT = '门店专区背景图领域对象';
