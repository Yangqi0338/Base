ALTER TABLE
  `activity` DROP COLUMN `creator`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `activity_id` varchar(255) NULL COMMENT '活动id',
ADD
  COLUMN `client_type` int NULL COMMENT '客户端类型(1 平台, 2 渠道商)' AFTER `channel_id`,
  MODIFY COLUMN `activity_name` varchar(255) NULL COMMENT '活动名称',
  MODIFY COLUMN `strategy_id` bigint NULL COMMENT '策略id',
  MODIFY COLUMN `condition_type` int NULL COMMENT '门槛类型(0 贡献值, 1 会员等级, 2 会员卡等级)',
  MODIFY COLUMN `repeat_type` int NULL COMMENT '复类型(0 单次, 1 周期)',
  MODIFY COLUMN `state` varchar(255) NULL COMMENT '状态(0 关闭, 1 开启)',
  MODIFY COLUMN `other_config` bigint NULL COMMENT '其他配置',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `other_config`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '活动';
