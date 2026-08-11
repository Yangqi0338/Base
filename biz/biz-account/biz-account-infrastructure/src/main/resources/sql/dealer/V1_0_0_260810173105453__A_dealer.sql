ALTER TABLE
  `dealer` DROP COLUMN `state`,
  DROP COLUMN `name`,
  DROP COLUMN `head_img`,
  DROP COLUMN `username`,
  DROP COLUMN `role_id`,
  DROP COLUMN `role_name`,
  DROP COLUMN `operator_id`,
  DROP COLUMN `invite_channel_number`,
  DROP COLUMN `service_fee`,
  DROP COLUMN `order_amount`,
  DROP COLUMN `order_total_amount`,
  DROP COLUMN `phone`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `service_rate` double(6, 2) NULL COMMENT '分润比例' AFTER `id`,
  MODIFY COLUMN `market_count` int NULL COMMENT '绑定的二级市场数量' AFTER `service_rate`,
  MODIFY COLUMN `supplier_goods_count` int NULL COMMENT '供应商商品数量' AFTER `market_count`,
  MODIFY COLUMN `goods_points` int NULL COMMENT '提货积分' AFTER `supplier_goods_count`,
  MODIFY COLUMN `level_up_progress` double(6, 2) NULL COMMENT '升级进度' AFTER `goods_points`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `level_up_progress`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
