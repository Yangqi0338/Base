ALTER TABLE
  `channel`
ADD
  COLUMN `open_id` varchar(255) NULL COMMENT '微信 openId' AFTER `store_name`,
ADD
  COLUMN `union_id` varchar(255) NULL COMMENT '微信 unionId' AFTER `open_id`;
