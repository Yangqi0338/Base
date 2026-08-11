ALTER TABLE
  `bonus_pool_partake` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id' AFTER `id`,
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账户id' AFTER `channel_id`,
  MODIFY COLUMN `serial_id` varchar(255) NULL COMMENT '流水号id',
  MODIFY COLUMN `activity_id` varchar(255) NULL COMMENT '活动id',
  MODIFY COLUMN `activity_name` varchar(255) NULL COMMENT '活动名称',
  MODIFY COLUMN `settlement_id` varchar(255) NULL COMMENT '结算id',
  MODIFY COLUMN `nick_name` varchar(255) NULL COMMENT '昵称',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '手机号 phone',
  MODIFY COLUMN `role` bigint NULL COMMENT '角色',
  MODIFY COLUMN `role_name` varchar(255) NULL COMMENT '角色名称',
  MODIFY COLUMN `dividend_amount` bigint NULL COMMENT '个人分红金额',
  MODIFY COLUMN `dividend_cycle` varchar(255) NULL COMMENT '分红周期',
  MODIFY COLUMN `order_sn` varchar(255) NULL COMMENT '交易单号',
  MODIFY COLUMN `bonus` bigint NULL COMMENT '录入奖金',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `bonus`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '奖金池参与记录';
