ALTER TABLE
  `course_chapter` MODIFY COLUMN `is_free` int NULL COMMENT '是否免费[1是,0否]',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用[1是,0否]';
