ALTER TABLE
  `strategy` DROP COLUMN `strategy_id`,
  DROP COLUMN `creator`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `strategy_mode` int NULL COMMENT '策略方式(0 奖金池贡献值, 1 暂未其他)',
  MODIFY COLUMN `grant_type` int NULL COMMENT '发放奖品方式(1 即时, 2 定时, 3 人工)',
  MODIFY COLUMN `dividend_cycle` varchar(255) NULL COMMENT '分红周期',
  MODIFY COLUMN `settlement_strategy` varchar(255) NULL COMMENT '分红策略',
  MODIFY COLUMN `dividend_method` varchar(255) NULL COMMENT '分红方式',
  MODIFY COLUMN `dividend_role` varchar(255) NULL COMMENT '分红角色',
  MODIFY COLUMN `dividend_user` varchar(255) NULL COMMENT '分红用户',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `dividend_user`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  DROP PRIMARY KEY,
ADD
  PRIMARY KEY (`id`),
  COMMENT = '策略';
