ALTER TABLE
  `order` DROP INDEX `auto_idx_order_order_no`,
ADD
  UNIQUE INDEX `auto_idx_key`(`order_no`, `del_flag`);
