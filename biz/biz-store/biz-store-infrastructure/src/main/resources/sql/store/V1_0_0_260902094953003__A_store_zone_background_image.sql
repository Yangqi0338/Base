ALTER TABLE
  `store_zone_background_image` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT 'zoneCode',
  MODIFY COLUMN `background_image` varchar(255) NULL COMMENT 'backgroundImage',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'StoreZoneBackgroundImageDO表';
