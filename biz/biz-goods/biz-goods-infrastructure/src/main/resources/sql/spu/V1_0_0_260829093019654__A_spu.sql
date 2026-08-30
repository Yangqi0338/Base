ALTER TABLE
  `spu`
ADD
  COLUMN `search_key` varchar(255) NULL COMMENT '搜索关键字(逗号隔开)' AFTER `scroll_img`;
