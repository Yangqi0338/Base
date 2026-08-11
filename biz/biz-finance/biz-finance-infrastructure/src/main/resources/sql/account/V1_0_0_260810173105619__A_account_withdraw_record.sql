ALTER TABLE
  `account_withdraw_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
ADD
  COLUMN `goods_points` int NULL COMMENT '提货积分' AFTER `amount`,
ADD
  COLUMN `config` json NULL COMMENT '配置' AFTER `goods_points`,
  MODIFY COLUMN `finish_time` varchar(255) NULL COMMENT '提现时间',
  MODIFY COLUMN `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
  MODIFY COLUMN `state` int NULL COMMENT '状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_account_withdraw_record_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_account_withdraw_record_finish_time`(`finish_time`) COMMENT '提现时间',
ADD
  INDEX `auto_idx_account_withdraw_record_state`(`state`) COMMENT '状态';
