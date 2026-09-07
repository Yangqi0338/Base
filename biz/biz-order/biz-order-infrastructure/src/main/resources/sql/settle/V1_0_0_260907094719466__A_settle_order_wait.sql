ALTER TABLE
  `settle_order_wait` CHANGE COLUMN spu_order_id `order_no` varchar(255) NULL COMMENT '交易单号

<p>原 spu_order_id 列, slug42 起已存 order 主键值, 本次改名并转业务单号</p>' AFTER `supplier_id`,
  MODIFY COLUMN `settle_state` int NULL COMMENT '结算状态[1是|启用,0否|禁用]',
  DROP INDEX `auto_idx_settle_order_wait_spu_order_id`,
ADD
  INDEX `auto_idx_settle_order_wait_order_no`(`order_no`) COMMENT '交易单号

<p>原 spu_order_id 列, slug42 起已存 order 主键值, 本次改名并转业务单号</p>';
