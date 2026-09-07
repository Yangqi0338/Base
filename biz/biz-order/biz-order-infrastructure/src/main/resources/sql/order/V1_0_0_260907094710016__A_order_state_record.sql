ALTER TABLE
  `order_state_record` CHANGE COLUMN order_id `order_no` varchar(255) NULL COMMENT '交易单号' AFTER `id`,
  DROP INDEX `auto_idx_order_state_record_order_id`,
ADD
  INDEX `auto_idx_order_state_record_order_no`(`order_no`) COMMENT '交易单号';
