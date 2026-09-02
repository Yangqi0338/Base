ALTER TABLE
  `project_video` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `project_id` bigint NULL COMMENT 'projectId',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `url` varchar(255) NULL COMMENT 'url',
  MODIFY COLUMN `index` int NULL COMMENT 'index',
  MODIFY COLUMN `extra` varchar(255) NULL COMMENT 'extra',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'ProjectVideoDO表';
