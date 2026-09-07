ALTER TABLE
  `refund` CHANGE COLUMN order_id `order_no` varchar(255) NULL COMMENT '交易单号' AFTER `id`;
