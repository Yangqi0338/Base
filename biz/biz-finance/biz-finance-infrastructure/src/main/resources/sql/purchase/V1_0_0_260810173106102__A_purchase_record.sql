ALTER TABLE
  `purchase_record` DROP COLUMN `creator`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `purchase_no` varchar(255) NULL COMMENT '购买单号',
  MODIFY COLUMN `type` int NULL COMMENT '购买类型',
  MODIFY COLUMN `trade_no` bigint NULL COMMENT '交易单号',
  MODIFY COLUMN `order_no` bigint NULL COMMENT '订单号',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `pay_amount` bigint NULL COMMENT '支付金额',
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '商品金额',
  MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式',
  MODIFY COLUMN `pay_state` int NULL COMMENT '支付状态',
  MODIFY COLUMN `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `tripartite_trade_no`,
  MODIFY COLUMN `order_info` json NULL COMMENT '订单信息',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `order_info`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_purchase_record_purchase_no`(`purchase_no`) COMMENT '购买单号',
ADD
  INDEX `auto_idx_purchase_record_trade_no`(`trade_no`) COMMENT '交易单号',
ADD
  INDEX `auto_idx_purchase_record_order_no`(`order_no`) COMMENT '订单号',
ADD
  INDEX `auto_idx_purchase_record_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_purchase_record_pay_state`(`pay_state`) COMMENT '支付状态',
ADD
  INDEX `auto_idx_purchase_record_foreign_id`(`foreign_id`) COMMENT '外键id';
