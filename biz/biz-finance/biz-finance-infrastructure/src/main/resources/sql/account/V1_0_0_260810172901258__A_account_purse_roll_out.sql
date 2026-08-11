ALTER TABLE
  `account_purse_roll_out` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `account_type` int NULL COMMENT '客户类型',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型',
  MODIFY COLUMN `apply_amount` bigint NULL COMMENT '申请金额',
  MODIFY COLUMN `audit_state` int NULL COMMENT '审核状态',
  MODIFY COLUMN `tripartite_trade_state` int NULL COMMENT '三方交易状态',
  MODIFY COLUMN `tripartite_account_id` varchar(255) NULL COMMENT '三方用户id' AFTER `tripartite_trade_state`,
  MODIFY COLUMN `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
  MODIFY COLUMN `audit_time` datetime NULL COMMENT '审核时间' AFTER `audit_remark`,
  MODIFY COLUMN `handling_fee` bigint NULL COMMENT '手续费' AFTER `audit_time`,
  MODIFY COLUMN `apply_time` datetime NULL COMMENT '申请时间',
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
  INDEX `auto_idx_account_purse_roll_out_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_account_purse_roll_out_account_type`(`account_type`) COMMENT '客户类型',
ADD
  INDEX `auto_idx_account_purse_roll_out_purse_type`(`purse_type`) COMMENT '账户类型',
ADD
  INDEX `auto_idx_account_purse_roll_out_audit_time`(`audit_time`) COMMENT '审核时间';
