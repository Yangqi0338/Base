ALTER TABLE
  `bill_order_award` DROP COLUMN `role_name`,
  DROP COLUMN `nickname`,
  DROP COLUMN `realname`,
  DROP COLUMN `create_date`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `role` bigint NULL COMMENT '角色',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号id',
  MODIFY COLUMN `username` varchar(255) NULL COMMENT '用戶名即手机号',
  MODIFY COLUMN `purse_type` int NULL COMMENT '钱包类型',
  MODIFY COLUMN `account_type` int NULL COMMENT '账户类型',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
  MODIFY COLUMN `order_count` int NULL COMMENT '订单数',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `order_count`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_bill_order_award_account_id`(`account_id`) COMMENT '账号id',
ADD
  INDEX `auto_idx_bill_order_award_username`(`username`) COMMENT '用戶名即手机号',
ADD
  INDEX `auto_idx_bill_order_award_purse_type`(`purse_type`) COMMENT '钱包类型',
ADD
  INDEX `auto_idx_bill_order_award_account_type`(`account_type`) COMMENT '账户类型',
  COMMENT = '订单奖励账单 DO';
