ALTER TABLE
  `earning_record` DROP COLUMN `consume_type`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `earning_type` varchar(255) NULL COMMENT '消费类型' AFTER `id`,
  MODIFY COLUMN `amount` bigint NULL COMMENT '分润金额',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `contribute_id` bigint NULL COMMENT '贡献对象id',
ADD
  COLUMN `contribute_name` varchar(255) NULL COMMENT '贡献对象id' AFTER `contribute_id`,
  MODIFY COLUMN `join_order_no` bigint NULL COMMENT '关联订单' AFTER `contribute_name`,
  MODIFY COLUMN `join_trade_no` bigint NULL COMMENT '关联交易单号' AFTER `join_order_no`,
  MODIFY COLUMN `role` bigint NULL COMMENT '分润角色 id',
  MODIFY COLUMN `goods_info` json NULL COMMENT '商品信息',
  MODIFY COLUMN `earning_time` datetime NULL COMMENT '分润时间',
  MODIFY COLUMN `state` int NULL COMMENT '状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息',
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id',
ADD
  COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_earning_record_earning_type`(`earning_type`) COMMENT '消费类型',
ADD
  INDEX `auto_idx_earning_record_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_earning_record_contribute_id`(`contribute_id`) COMMENT '贡献对象id',
ADD
  INDEX `auto_idx_earning_record_join_order_no`(`join_order_no`) COMMENT '关联订单',
ADD
  INDEX `auto_idx_earning_record_earning_time`(`earning_time`) COMMENT '分润时间';
