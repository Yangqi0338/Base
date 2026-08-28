CREATE TABLE `account_withdraw_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `account_id` bigint NULL COMMENT '客户id',
  `amount` bigint NULL COMMENT '金额',
  `goods_points` int NULL COMMENT '提货积分',
  `config` json NULL COMMENT '配置',
  `finish_time` varchar(255) NULL COMMENT '提现时间',
  `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
  `state` int NULL COMMENT '状态',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_account_withdraw_record_account_id`(`account_id`) COMMENT '客户id',
  INDEX `auto_idx_account_withdraw_record_finish_time`(`finish_time`) COMMENT '提现时间',
  INDEX `auto_idx_account_withdraw_record_state`(`state`) COMMENT '状态'
);
