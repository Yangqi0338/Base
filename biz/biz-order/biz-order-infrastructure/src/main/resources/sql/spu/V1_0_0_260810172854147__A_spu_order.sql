ALTER TABLE
  `spu_order` DROP COLUMN `goods_type`,
  DROP COLUMN `merchant_id`,
  DROP COLUMN `store_name`,
  DROP COLUMN `store_head`,
  DROP COLUMN `benefit_tripartite_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `order_type` int NULL COMMENT '订单类型' AFTER `id`,
  MODIFY COLUMN `spu_channel_type` int NULL COMMENT '渠道类型' AFTER `order_type`,
  MODIFY COLUMN `out_order_no` varchar(255) NULL COMMENT '外部订单号' AFTER `spu_channel_type`,
  MODIFY COLUMN `order_id` bigint NULL COMMENT '交易单ID' AFTER `out_order_no`,
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商ID' AFTER `order_id`,
  MODIFY COLUMN `supplier_id` bigint NULL COMMENT '供应商ID' AFTER `channel_id`,
  MODIFY COLUMN `dealer_id` bigint NULL COMMENT '交易师ID' AFTER `supplier_id`,
  MODIFY COLUMN `operator_id` bigint NULL COMMENT '运营商ID' AFTER `dealer_id`,
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'SPU_ID' AFTER `operator_id`,
  MODIFY COLUMN `spu_name` varchar(255) NULL COMMENT 'SPU名称' AFTER `spu_id`,
  MODIFY COLUMN `spu_img` varchar(255) NULL COMMENT 'SPU图片' AFTER `spu_name`,
  MODIFY COLUMN `sku_count` int NULL COMMENT 'SKU种类数量' AFTER `spu_img`,
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '选品金额' AFTER `sku_count`,
  MODIFY COLUMN `store_amount` bigint NULL COMMENT '铺货金额' AFTER `goods_amount`,
  MODIFY COLUMN `freight_amount` bigint NULL COMMENT '运费金额' AFTER `store_amount`,
  MODIFY COLUMN `discount_amount` bigint NULL COMMENT '优惠金额' AFTER `freight_amount`,
ADD
  COLUMN `total_amount` bigint NULL COMMENT '渠道商待支付总金额' AFTER `discount_amount`,
  MODIFY COLUMN `supplier_amount` bigint NULL COMMENT '货款金额' AFTER `total_amount`,
  MODIFY COLUMN `member_amount` bigint NULL COMMENT 'C端支付金额' AFTER `supplier_amount`,
  MODIFY COLUMN `service_amount` bigint NULL COMMENT '服务费: 渠道商应付' AFTER `member_amount`,
  MODIFY COLUMN `order_state` int NULL COMMENT '订单状态',
  MODIFY COLUMN `ship_v_o` json NULL COMMENT '收货信息' AFTER `order_state`,
  MODIFY COLUMN `ship_phone` varchar(255) NULL COMMENT '收货人手机号' AFTER `ship_v_o`,
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '订单备注' AFTER `ship_phone`,
  MODIFY COLUMN `order_state_log` varchar(255) NULL COMMENT '订单状态流转日志(逗号隔开)' AFTER `remark`,
  MODIFY COLUMN `settle_send_state` int NULL COMMENT '运费结算发送状态' AFTER `order_state_log`,
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店ID' AFTER `settle_send_state`,
  MODIFY COLUMN `member_id` bigint NULL COMMENT 'C端会员ID' AFTER `store_id`,
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号id(account.id)' AFTER `member_id`,
  MODIFY COLUMN `delivered_time` datetime NULL COMMENT '发货完成时间' AFTER `account_id`,
  MODIFY COLUMN `receive_time` datetime NULL COMMENT '确认收货时间' AFTER `delivered_time`,
  MODIFY COLUMN `close_time` datetime NULL COMMENT '关闭时间' AFTER `receive_time`,
  MODIFY COLUMN `refunding_count` int NULL COMMENT '售后中数量' AFTER `close_time`,
  MODIFY COLUMN `spu_order_ext` json NULL COMMENT '订单拓展信息' AFTER `refunding_count`,
  MODIFY COLUMN `refund` int NULL COMMENT '是否有售后' AFTER `spu_order_ext`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `refund`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_spu_order_out_order_no`(`out_order_no`) COMMENT '外部订单号',
ADD
  INDEX `auto_idx_spu_order_order_id`(`order_id`) COMMENT '交易单ID',
ADD
  INDEX `auto_idx_spu_order_channel_id`(`channel_id`) COMMENT '渠道商ID',
ADD
  INDEX `auto_idx_spu_order_supplier_id`(`supplier_id`) COMMENT '供应商ID',
ADD
  INDEX `auto_idx_spu_order_dealer_id`(`dealer_id`) COMMENT '交易师ID',
ADD
  INDEX `auto_idx_spu_order_operator_id`(`operator_id`) COMMENT '运营商ID',
ADD
  INDEX `auto_idx_spu_order_spu_id`(`spu_id`) COMMENT 'SPU_ID',
ADD
  INDEX `auto_idx_spu_order_order_state`(`order_state`) COMMENT '订单状态',
ADD
  INDEX `auto_idx_spu_order_store_id`(`store_id`) COMMENT '门店ID',
ADD
  INDEX `auto_idx_spu_order_member_id`(`member_id`) COMMENT 'C端会员ID',
ADD
  INDEX `auto_idx_spu_order_account_id`(`account_id`) COMMENT '账号id(account.id)';
