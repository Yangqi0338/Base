ALTER TABLE
  `member`
ADD
  COLUMN `union_id` varchar(255) NULL COMMENT 'unionId' AFTER `open_id`;
