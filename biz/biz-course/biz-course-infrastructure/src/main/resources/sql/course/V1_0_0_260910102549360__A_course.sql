ALTER TABLE
  `course` DROP COLUMN `total_duration_desc`,
  MODIFY COLUMN `intro` varchar(2000) NULL COMMENT '课程简介',
  MODIFY COLUMN `details` text NULL COMMENT '课程详情富文本',
ADD
  COLUMN `expand` json NULL COMMENT '扩展信息' AFTER `is_enabled`;
