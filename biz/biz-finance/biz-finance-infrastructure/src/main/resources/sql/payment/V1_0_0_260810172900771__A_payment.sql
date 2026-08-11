ALTER TABLE
  `payment` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `trade_no` bigint NULL COMMENT '交易单号',
  MODIFY COLUMN `order_no` bigint NULL COMMENT '订单号',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `pay_amount` bigint NULL COMMENT '支付金额',
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '商品金额',
  MODIFY COLUMN `consume_type` int NULL COMMENT '消费类型',
  MODIFY COLUMN `pay_state` int NULL COMMENT '支付状态',
  MODIFY COLUMN `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
  MODIFY COLUMN `order_info` json NULL COMMENT '订单信息',
  MODIFY COLUMN `payee_info` json NULL COMMENT '收款方信息',
  MODIFY COLUMN `pay_time` datetime NULL COMMENT '支付时间',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `pay_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  DROP PRIMARY KEY,
ADD
  PRIMARY KEY (`id`),
ADD
  INDEX `auto_idx_payment_trade_no`(`trade_no`) COMMENT '交易单号',
ADD
  INDEX `auto_idx_payment_order_no`(`order_no`) COMMENT '订单号',
ADD
  INDEX `auto_idx_payment_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_payment_consume_type`(`consume_type`) COMMENT '消费类型',
ADD
  INDEX `auto_idx_payment_pay_time`(`pay_time`) COMMENT '支付时间';
