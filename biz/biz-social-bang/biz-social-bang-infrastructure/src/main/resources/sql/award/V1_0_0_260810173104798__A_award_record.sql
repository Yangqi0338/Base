ALTER TABLE
  `award_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `activity_id` int NULL COMMENT '活动id',
  MODIFY COLUMN `strategy_id` bigint NULL COMMENT '策略id',
  MODIFY COLUMN `channel_activity_id` bigint NULL COMMENT '渠道商活动id',
  MODIFY COLUMN `award_state` int NULL COMMENT '奖品状态(0-待发放, 1-已发放)',
  MODIFY COLUMN `grant_type` int NULL COMMENT '发放奖品方式(1-即时, 2-定时, 3-人工)',
  MODIFY COLUMN `member_id` bigint NULL COMMENT '会员id',
  MODIFY COLUMN `award_id` bigint NULL COMMENT '奖品id',
  MODIFY COLUMN `award_type` int NULL COMMENT '奖品类型',
  MODIFY COLUMN `grant_time` datetime NULL COMMENT '发放时间' AFTER `ext_info`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `grant_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
