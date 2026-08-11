ALTER TABLE
  `account_purse` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型',
  MODIFY COLUMN `account_type` int NULL COMMENT '客户类型',
  MODIFY COLUMN `earnings` bigint NULL COMMENT '收益',
  MODIFY COLUMN `total_earnings` bigint NULL COMMENT '总收益',
  MODIFY COLUMN `tripartite_amount` bigint NULL COMMENT '三方余额',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `tripartite_amount`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_account_purse_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_account_purse_purse_type`(`purse_type`) COMMENT '账户类型',
ADD
  INDEX `auto_idx_account_purse_account_type`(`account_type`) COMMENT '客户类型';
