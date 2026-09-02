ALTER TABLE
  `store_zone` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT 'zoneCode',
  MODIFY COLUMN `zone_name` varchar(255) NULL COMMENT 'zoneName',
  MODIFY COLUMN `zone_subtitle` varchar(255) NULL COMMENT 'zoneSubtitle',
  MODIFY COLUMN `zone_describe` varchar(255) NULL COMMENT 'zoneDescribe',
  MODIFY COLUMN `state` int NULL COMMENT 'state',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'StoreZoneDO表';
