ALTER TABLE
  `order_state_record` DROP COLUMN `before_state_desc`,
  DROP COLUMN `after_state_desc`,
  DROP COLUMN `operator_id`,
  DROP COLUMN `role_desc`,
  DROP COLUMN `operate_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `order_id` bigint NULL COMMENT '订单主键ID',
  MODIFY COLUMN `spu_order_id` bigint NULL COMMENT 'SPU订单ID',
  MODIFY COLUMN `sku_order_id` bigint NULL COMMENT 'SKU订单ID',
  MODIFY COLUMN `orderer_id` bigint NULL COMMENT '下单人ID(匿名订单可为 null)',
  CHANGE COLUMN operator_role_id `operator_role` bigint NULL COMMENT '操作人角色' AFTER `orderer_id`,
  MODIFY COLUMN `ext` json NULL COMMENT '拓展字段',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `ext`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_order_state_record_order_id`(`order_id`) COMMENT '订单主键ID',
ADD
  INDEX `auto_idx_order_state_record_spu_order_id`(`spu_order_id`) COMMENT 'SPU订单ID',
ADD
  INDEX `auto_idx_order_state_record_sku_order_id`(`sku_order_id`) COMMENT 'SKU订单ID',
ADD
  INDEX `auto_idx_order_state_record_orderer_id`(`orderer_id`) COMMENT '下单人ID',
  COMMENT = '订单状态记录表 DO';
