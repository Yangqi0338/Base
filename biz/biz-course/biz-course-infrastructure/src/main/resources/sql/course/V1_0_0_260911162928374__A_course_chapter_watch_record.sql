ALTER TABLE
  `course_chapter_watch_record`
ADD
  COLUMN `course_no` varchar(255) NULL COMMENT '课程编码(冗余字段)' AFTER `course_chapter_id`;
