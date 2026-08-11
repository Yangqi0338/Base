ALTER TABLE
  `bonus_pool_now` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `activity_id` varchar(255) NULL COMMENT '活动id',
  MODIFY COLUMN `bonus_desc` varchar(255) NULL COMMENT '奖金池活动描述',
  MODIFY COLUMN `strategy_id` bigint NULL COMMENT '策略id',
  MODIFY COLUMN `order_bonus` bigint NULL COMMENT '订单奖金',
  MODIFY COLUMN `custom_bonus` bigint NULL COMMENT '自定义奖金',
  MODIFY COLUMN `settle_bonus` bigint NULL COMMENT '最终结算奖金',
  MODIFY COLUMN `state` int NULL COMMENT '状态(0-进行中, 1-已结算, 2-已作废)',
  MODIFY COLUMN `start_time` datetime NULL COMMENT '开始时间',
  MODIFY COLUMN `end_time` datetime NULL COMMENT '结束时间',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `end_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '本期奖金池';
