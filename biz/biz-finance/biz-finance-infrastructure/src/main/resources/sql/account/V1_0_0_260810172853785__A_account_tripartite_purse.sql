ALTER TABLE
  `account_tripartite_purse` DROP COLUMN `account_level`,
  DROP COLUMN `oid_apply_seq_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `oid_user_no` varchar(255) NULL COMMENT '三方用户id',
  MODIFY COLUMN `oid_apply_seq_no` varchar(255) NULL COMMENT '三方申请id' AFTER `oid_user_no`,
  MODIFY COLUMN `user_status` varchar(255) NULL COMMENT '状态',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '账户姓名',
  MODIFY COLUMN `amount` bigint NULL COMMENT '三方账户余额',
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '备注',
  MODIFY COLUMN `account_type` varchar(255) NULL COMMENT '类型',
  MODIFY COLUMN `bank_name` varchar(255) NULL COMMENT '银行名称（源 account_tripartite_purse.bank_name 列）' AFTER `account_type`,
  MODIFY COLUMN `commit_info` json NULL COMMENT '资料信息' AFTER `bank_no`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `commit_info`,
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
  INDEX `auto_idx_account_tripartite_purse_account_id`(`account_id`) COMMENT '客户id';
