ALTER TABLE
  `sku`
ADD
  COLUMN `code` varchar(255) NULL COMMENT '编码' AFTER `id`;
