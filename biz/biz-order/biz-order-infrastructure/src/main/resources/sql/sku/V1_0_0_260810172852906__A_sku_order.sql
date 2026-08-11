ALTER TABLE
  `sku_order` DROP COLUMN `member_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `order_id` bigint NULL COMMENT '交易单ID' AFTER `id`,
  MODIFY COLUMN `spu_order_id` bigint NULL COMMENT 'SPU订单ID' AFTER `order_id`,
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '选品金额' AFTER `spu_order_id`,
  MODIFY COLUMN `store_amount` bigint NULL COMMENT '铺货金额' AFTER `goods_amount`,
  MODIFY COLUMN `freight_amount` bigint NULL COMMENT '运费金额' AFTER `store_amount`,
  MODIFY COLUMN `discount_amount` bigint NULL COMMENT '优惠金额' AFTER `freight_amount`,
ADD
  COLUMN `total_amount` bigint NULL COMMENT '总金额' AFTER `discount_amount`,
  MODIFY COLUMN `supplier_amount` bigint NULL COMMENT '货款金额' AFTER `total_amount`,
  MODIFY COLUMN `order_state` int NULL COMMENT '订单状态',
  MODIFY COLUMN `dealer_id` bigint NULL COMMENT '交易师ID' AFTER `order_state`,
  MODIFY COLUMN `operator_id` bigint NULL COMMENT '运营商ID' AFTER `dealer_id`,
  MODIFY COLUMN `supplier_id` bigint NULL COMMENT '供应商ID' AFTER `operator_id`,
  MODIFY COLUMN `settlement_config_v_o` json NULL COMMENT '结算配置(源列 settlement_config_v_o, 多来源 JSON 故用 String)' AFTER `supplier_id`,
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'spuID' AFTER `settlement_config_v_o`,
  MODIFY COLUMN `sku_id` bigint NULL COMMENT 'skuID',
  MODIFY COLUMN `out_sku_id` varchar(255) NULL COMMENT '外部SkuId' AFTER `sku_id`,
  MODIFY COLUMN `count` int NULL COMMENT '购买数量',
  MODIFY COLUMN `spu_name` varchar(255) NULL COMMENT 'spu名称' AFTER `count`,
  MODIFY COLUMN `sku_sale_attribute` json NULL COMMENT 'sku销售属性',
  MODIFY COLUMN `sku_weight` double(6, 2) NULL COMMENT 'sku重量(千克)',
  MODIFY COLUMN `sku_volume` double(6, 2) NULL COMMENT 'sku体积(m3)',
  MODIFY COLUMN `sku_supplier_price` bigint NULL COMMENT 'sku供货价',
  MODIFY COLUMN `sku_sale_price` bigint NULL COMMENT 'sku采购价',
  MODIFY COLUMN `sku_store_price` bigint NULL COMMENT 'sku铺货价' AFTER `sku_sale_price`,
  MODIFY COLUMN `deliver_count` int NULL COMMENT '发货数量',
  MODIFY COLUMN `refunding_count` int NULL COMMENT '售后中数量',
  MODIFY COLUMN `refunded_count` int NULL COMMENT '已售后数量',
  MODIFY COLUMN `order_state_log` varchar(255) NULL COMMENT '订单状态流转日志(逗号隔开)' AFTER `refunded_count`,
  MODIFY COLUMN `delivered_time` datetime NULL COMMENT '发货完成时间' AFTER `order_state_log`,
  MODIFY COLUMN `receive_time` datetime NULL COMMENT '确认收货时间' AFTER `delivered_time`,
  MODIFY COLUMN `settle_order_type` int NULL COMMENT '结算节点' AFTER `receive_time`,
  MODIFY COLUMN `settle_send_state` int NULL COMMENT '结算发送状态' AFTER `settle_order_type`,
  MODIFY COLUMN `total_service_change` bigint NULL COMMENT '总服务费' AFTER `settle_send_state`,
  MODIFY COLUMN `operator_service_change` bigint NULL COMMENT '运营商服务费' AFTER `total_service_change`,
  MODIFY COLUMN `operator_real_ratio` double(6, 2) NULL COMMENT '运营商实际服务比例' AFTER `operator_service_change`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `operator_real_ratio`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_sku_order_order_id`(`order_id`) COMMENT '交易单ID',
ADD
  INDEX `auto_idx_sku_order_spu_order_id`(`spu_order_id`) COMMENT 'SPU订单ID',
ADD
  INDEX `auto_idx_sku_order_order_state`(`order_state`) COMMENT '订单状态',
ADD
  INDEX `auto_idx_sku_order_dealer_id`(`dealer_id`) COMMENT '交易师ID',
ADD
  INDEX `auto_idx_sku_order_operator_id`(`operator_id`) COMMENT '运营商ID',
ADD
  INDEX `auto_idx_sku_order_supplier_id`(`supplier_id`) COMMENT '供应商ID',
ADD
  INDEX `auto_idx_sku_order_spu_id`(`spu_id`) COMMENT 'spuID',
ADD
  INDEX `auto_idx_sku_order_sku_id`(`sku_id`) COMMENT 'skuID';
