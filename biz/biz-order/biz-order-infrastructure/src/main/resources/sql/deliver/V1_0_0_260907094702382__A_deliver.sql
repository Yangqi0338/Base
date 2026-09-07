ALTER TABLE
  `deliver` DROP COLUMN `spu_order_id`,
ADD
  COLUMN `order_no` varchar(255) NULL COMMENT 'SPU订单ID' AFTER `id`;
