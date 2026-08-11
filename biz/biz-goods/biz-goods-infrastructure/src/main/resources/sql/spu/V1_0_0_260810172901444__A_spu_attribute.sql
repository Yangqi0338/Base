ALTER TABLE
  `spu_attribute` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'spuId(查询)',
  MODIFY COLUMN `type` int NULL COMMENT '类型(查询)',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `value`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_spu_attribute_spu_id`(`spu_id`) COMMENT 'spuId';
