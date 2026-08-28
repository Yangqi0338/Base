CREATE TABLE `account_purse` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `account_id` bigint NULL COMMENT '客户id',
  `account_name` varchar(255) NULL COMMENT '客户名称',
  `purse_type` int NULL COMMENT '账户类型',
  `account_type` int NULL COMMENT '客户类型',
  `earnings` bigint NULL COMMENT '收益',
  `total_earnings` int NULL COMMENT '总收益',
  `tripartite_amount` bigint NULL COMMENT '三方余额',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_account_purse_account_id`(`account_id`) COMMENT '客户id',
  INDEX `auto_idx_account_purse_account_type`(`account_type`) COMMENT '客户类型',
  INDEX `auto_idx_account_purse_purse_type`(`purse_type`) COMMENT '账户类型'
);
