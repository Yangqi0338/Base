ALTER TABLE
  `sku_order` DROP COLUMN `spu_order_id`,
  DROP COLUMN `discount_amount`,
  DROP COLUMN `dealer_id`,
  DROP COLUMN `operator_id`,
  DROP COLUMN `settlement_config_v_o`,
  DROP COLUMN `spu_name`,
  DROP COLUMN `sku_name`,
  DROP COLUMN `sku_weight`,
  DROP COLUMN `sku_volume`,
  DROP COLUMN `operator_service_change`,
  DROP COLUMN `operator_real_ratio`,
  CHANGE COLUMN order_id `order_no` varchar(255) NULL COMMENT '交易单号' AFTER `id`,
ADD
  COLUMN `sku_order_no` varchar(255) NULL COMMENT 'SKU订单号' AFTER `order_no`,
ADD
  COLUMN `out_spu_id` varchar(255) NULL COMMENT '外部SpuId' AFTER `out_sku_id`,
ADD
  COLUMN `spu_channel_type` int NULL COMMENT 'SPU渠道类型[0供应商商品,2外部供应链商品]' AFTER `out_spu_id`,
ADD
  COLUMN `order_sku_info` json NULL COMMENT '内嵌SPU/SKU信息(下沉自SpuOrder)' AFTER `spu_channel_type`,
  CHANGE COLUMN sku_supplier_price `supplier_price` bigint NULL COMMENT 'sku供货价' AFTER `sku_sale_attribute`,
  CHANGE COLUMN sku_sale_price `sale_price` bigint NULL COMMENT 'sku采购价' AFTER `supplier_price`,
  CHANGE COLUMN sku_store_price `store_price` bigint NULL COMMENT 'sku铺货价' AFTER `sale_price`,
  MODIFY COLUMN `delivered_time` datetime NULL COMMENT '发货完成时间' AFTER `refunded_count`,
  MODIFY COLUMN `receive_time` datetime NULL COMMENT '确认收货时间' AFTER `delivered_time`,
  MODIFY COLUMN `settle_send_state` int NULL COMMENT '结算发送状态[1是|启用,0否|禁用]',
  DROP INDEX `auto_idx_sku_order_spu_order_id`,
  DROP INDEX `auto_idx_sku_order_dealer_id`,
  DROP INDEX `auto_idx_sku_order_operator_id`,
  DROP INDEX `auto_idx_sku_order_order_id`,
ADD
  INDEX `auto_idx_sku_order_order_no`(`order_no`) COMMENT '交易单号',
ADD
  INDEX `auto_idx_sku_order_sku_order_no`(`sku_order_no`) COMMENT 'SKU订单号',
ADD
  INDEX `auto_idx_sku_order_spu_channel_type`(`spu_channel_type`) COMMENT 'SPU渠道类型';
