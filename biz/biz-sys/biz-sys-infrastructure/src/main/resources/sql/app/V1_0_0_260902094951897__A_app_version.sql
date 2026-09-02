ALTER TABLE
  `app_version` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `app_code` varchar(255) NULL COMMENT 'appCode',
  MODIFY COLUMN `app_name` varchar(255) NULL COMMENT 'appName',
  MODIFY COLUMN `app_version` varchar(255) NULL COMMENT 'appVersion',
  MODIFY COLUMN `update_config` int NULL COMMENT 'updateConfig',
  MODIFY COLUMN `resource_url` varchar(255) NULL COMMENT 'resourceUrl',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'AppVersionDO表';
