ALTER TABLE
  `store_zone_background_image` DROP COLUMN `deleted`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT '专区code',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `background_image`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_zone_background_image_zone_code`(`zone_code`) COMMENT '专区code',
  COMMENT = '门店专区背景图领域对象';
