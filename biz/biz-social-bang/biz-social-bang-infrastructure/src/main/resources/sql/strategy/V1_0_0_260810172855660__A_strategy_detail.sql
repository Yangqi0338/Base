ALTER TABLE
  `strategy_detail` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `strategy_id` bigint NULL COMMENT '策略ID',
  MODIFY COLUMN `award_id` bigint NULL COMMENT '奖品ID(0 表示现金, 不限量)',
  MODIFY COLUMN `award_name` varchar(255) NULL COMMENT '奖品名称',
  MODIFY COLUMN `award_count` int NULL COMMENT '奖品库存',
  MODIFY COLUMN `award_surplus_count` int NULL COMMENT '奖品剩余库存',
  MODIFY COLUMN `strategy_content` varchar(255) NULL COMMENT '策略内容',
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
  COMMENT = '策略明细';
