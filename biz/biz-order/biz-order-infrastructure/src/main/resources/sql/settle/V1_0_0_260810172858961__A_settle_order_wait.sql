ALTER TABLE
  `settle_order_wait` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `supplier_id` bigint NULL COMMENT '供应商ID',
  MODIFY COLUMN `spu_order_id` bigint NULL COMMENT 'SPU_订单ID',
  MODIFY COLUMN `type` int NULL COMMENT '类型(0 商品 1 运费 2 售后)',
  MODIFY COLUMN `order_money` bigint NULL COMMENT '订单结算金额',
  MODIFY COLUMN `settle_time` datetime NULL COMMENT '结算时间',
  MODIFY COLUMN `refund_id` bigint NULL COMMENT '售后单ID' AFTER `settle_time`,
  MODIFY COLUMN `refund_state` int NULL COMMENT '售后状态(对齐源 refund_state 列语义, 迁移补映射)' AFTER `refund_id`,
  MODIFY COLUMN `settle_record_id` bigint NULL COMMENT '结算单ID' AFTER `refund_state`,
ADD
  COLUMN `refund_amount` bigint NULL COMMENT '售后金额 默认0' AFTER `settle_record_id`,
  MODIFY COLUMN `settle_time_node` bigint NULL COMMENT '结算时间节点(时间戳)' AFTER `refund_amount`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `settle_time_node`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_settle_order_wait_supplier_id`(`supplier_id`) COMMENT '供应商ID',
ADD
  INDEX `auto_idx_settle_order_wait_spu_order_id`(`spu_order_id`) COMMENT 'SPU_订单ID',
ADD
  INDEX `auto_idx_settle_order_wait_sku_order_id`(`sku_order_id`) COMMENT 'sku订单ID',
ADD
  INDEX `auto_idx_settle_order_wait_spu_id`(`spu_id`) COMMENT 'SPU_ID',
ADD
  INDEX `auto_idx_settle_order_wait_sku_id`(`sku_id`) COMMENT 'SKU_ID',
ADD
  INDEX `auto_idx_settle_order_wait_refund_id`(`refund_id`) COMMENT '售后单ID',
ADD
  INDEX `auto_idx_settle_order_wait_settle_record_id`(`settle_record_id`) COMMENT '结算单ID';
