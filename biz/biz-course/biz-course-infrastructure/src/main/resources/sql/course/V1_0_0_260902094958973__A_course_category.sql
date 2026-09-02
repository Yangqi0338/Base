ALTER TABLE
  `course_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `category_code` varchar(255) NULL COMMENT 'categoryCode',
  MODIFY COLUMN `category_name` varchar(255) NULL COMMENT 'categoryName',
  MODIFY COLUMN `sub_title` varchar(255) NULL COMMENT 'subTitle',
  MODIFY COLUMN `sort` int NULL COMMENT 'sort',
  MODIFY COLUMN `course_count` int NULL COMMENT 'courseCount',
  MODIFY COLUMN `is_enabled` int NULL COMMENT 'isEnabled[1是,0否]',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'CourseCategoryDO表';
