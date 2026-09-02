ALTER TABLE
  `report` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `path` varchar(255) NULL COMMENT 'path',
  MODIFY COLUMN `category_id_list` varchar(255) NULL COMMENT 'categoryIdList',
  MODIFY COLUMN `spu_id_list` varchar(255) NULL COMMENT 'spuIdList',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'ReportDO表';
