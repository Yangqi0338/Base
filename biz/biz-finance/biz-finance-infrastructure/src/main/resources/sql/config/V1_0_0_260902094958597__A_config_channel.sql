ALTER TABLE
  `config_channel` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT 'channelId',
  MODIFY COLUMN `platform_config` json NULL COMMENT 'platformConfig',
  MODIFY COLUMN `operator_config` json NULL COMMENT 'operatorConfig',
  MODIFY COLUMN `platform_now_value` double(6, 2) NULL COMMENT 'platformNowValue',
  MODIFY COLUMN `operator_now_value` double(6, 2) NULL COMMENT 'operatorNowValue',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'ConfigChannelDO表';
