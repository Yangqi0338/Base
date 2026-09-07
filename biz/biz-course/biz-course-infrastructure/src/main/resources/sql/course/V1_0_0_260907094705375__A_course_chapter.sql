ALTER TABLE
  `course_chapter` MODIFY COLUMN `is_free` int NULL COMMENT '是否免费[1是|启用,0否|禁用]',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用[1是|启用,0否|禁用]';
