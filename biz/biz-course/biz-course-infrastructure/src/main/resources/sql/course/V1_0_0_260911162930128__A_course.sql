ALTER TABLE
  `course`
ADD
  COLUMN `course_no` varchar(255) NULL COMMENT '课程编码' AFTER `id`,
  MODIFY COLUMN `intro` varchar(2000) NULL COMMENT '课程简介',
ADD
  COLUMN `expand` json NULL COMMENT '扩展信息' AFTER `is_enabled`;
