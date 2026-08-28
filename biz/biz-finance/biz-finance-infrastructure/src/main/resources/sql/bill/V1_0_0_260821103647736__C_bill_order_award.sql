CREATE TABLE `bill_order_award` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `identity` bigint NULL COMMENT '角色',
  `account_id` bigint NULL COMMENT '账号id',
  `username` varchar(255) NULL COMMENT '用戶名即手机号',
  `purse_type` int NULL COMMENT '钱包类型',
  `account_type` int NULL COMMENT '账户类型',
  `amount` bigint NULL COMMENT '金额',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_bill_order_award_account_id`(`account_id`) COMMENT '账号id',
  INDEX `auto_idx_bill_order_award_account_type`(`account_type`) COMMENT '账户类型',
  INDEX `auto_idx_bill_order_award_purse_type`(`purse_type`) COMMENT '钱包类型',
  INDEX `auto_idx_bill_order_award_username`(`username`) COMMENT '用戶名即手机号'
) COMMENT = '订单奖励账单 DO';
