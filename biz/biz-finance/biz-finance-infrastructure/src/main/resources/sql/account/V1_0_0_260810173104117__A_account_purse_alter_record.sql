ALTER TABLE
  `account_purse_alter_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_type` int NULL COMMENT '客户类型',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型',
ADD
  COLUMN `earning_alter_type` int NULL COMMENT '分润修改类型' AFTER `purse_type`,
  MODIFY COLUMN `alter_type` int NULL COMMENT '变动类型',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
  MODIFY COLUMN `join_record_id` bigint NULL COMMENT '关联记录id',
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '备注或记录',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `remark`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_account_purse_alter_record_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_account_purse_alter_record_account_type`(`account_type`) COMMENT '客户类型',
ADD
  INDEX `auto_idx_account_purse_alter_record_purse_type`(`purse_type`) COMMENT '账户类型',
ADD
  INDEX `auto_idx_account_purse_alter_record_alter_type`(`alter_type`) COMMENT '变动类型',
ADD
  INDEX `auto_idx_account_purse_alter_record_join_record_id`(`join_record_id`) COMMENT '关联记录id';
