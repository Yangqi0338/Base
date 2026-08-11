ALTER TABLE
  `settle_record_item` DROP COLUMN `type`,
  DROP COLUMN `spu_order_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `sku_settle_detail` varchar(255) NULL COMMENT 'sku订单结算信息',
  MODIFY COLUMN `settle_money` bigint NULL COMMENT '结算金额',
  MODIFY COLUMN `spu_freight` bigint NULL COMMENT '结算运费' AFTER `settle_goods_num`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `spu_freight`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_settle_record_item_settle_record_id`(`settle_record_id`) COMMENT '结算记录ID',
ADD
  INDEX `auto_idx_settle_record_item_spu_id`(`spu_id`) COMMENT 'SPU_ID';
