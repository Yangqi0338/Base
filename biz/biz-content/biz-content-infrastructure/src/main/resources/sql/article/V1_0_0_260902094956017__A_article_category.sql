ALTER TABLE
  `article_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `sort` int NULL COMMENT 'sort',
  MODIFY COLUMN `article_count` int NULL COMMENT 'articleCount',
  MODIFY COLUMN `is_enabled` int NULL COMMENT 'isEnabled[1是,0否]',
  MODIFY COLUMN `recommend_groups` varchar(255) NULL COMMENT 'recommendGroups',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'ArticleCategoryDO表';
