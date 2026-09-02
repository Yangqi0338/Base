ALTER TABLE
  `video` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `category_id` bigint NULL COMMENT 'categoryId',
  MODIFY COLUMN `is_visible` int NULL COMMENT 'isVisible[1是,0否]',
  MODIFY COLUMN `video_url` varchar(255) NULL COMMENT 'videoUrl',
  MODIFY COLUMN `video_cover_url` varchar(255) NULL COMMENT 'videoCoverUrl',
  MODIFY COLUMN `issuer_id` bigint NULL COMMENT 'issuerId',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'VideoDO表';
