ALTER TABLE
  `video_spu_relation` MODIFY COLUMN `video_id` bigint NULL COMMENT 'videoId',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'spuId',
  MODIFY COLUMN `type` int NULL COMMENT 'type',
  COMMENT = 'VideoSpuRelationDO表';
