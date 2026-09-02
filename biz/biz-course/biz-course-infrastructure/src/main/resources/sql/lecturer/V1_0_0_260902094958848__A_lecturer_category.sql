ALTER TABLE
  `lecturer_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `category_name` varchar(255) NULL COMMENT 'categoryName',
  MODIFY COLUMN `icon_url` varchar(255) NULL COMMENT 'iconUrl',
  MODIFY COLUMN `is_enabled` int NULL COMMENT 'isEnabled[1是,0否]',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'LecturerCategoryDO表';
