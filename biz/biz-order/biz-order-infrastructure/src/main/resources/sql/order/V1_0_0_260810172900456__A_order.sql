ALTER TABLE
  `order` DROP COLUMN `out_order_id`,
  DROP COLUMN `benefitTripartiteId`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
ADD
  COLUMN `order_no` varchar(255) NULL COMMENT '交易单号' AFTER `id`,
  MODIFY COLUMN `order_type` int NULL COMMENT '订单类型' AFTER `order_no`,
  MODIFY COLUMN `out_order_no` varchar(255) NULL COMMENT '外部订单号' AFTER `order_type`,
ADD
  COLUMN `platform_type` varchar(255) NULL COMMENT '外部平台来源(三方单来源, 非外部单为 null)' AFTER `out_order_no`,
  MODIFY COLUMN `operator_id` bigint NULL COMMENT '运营商ID' AFTER `platform_type`,
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商ID' AFTER `operator_id`,
  MODIFY COLUMN `ship_v_o` json NULL COMMENT '收货信息' AFTER `channel_id`,
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '订单备注' AFTER `ship_v_o`,
  MODIFY COLUMN `supplier_amount` bigint NULL COMMENT '货款金额' AFTER `remark`,
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '选品金额' AFTER `supplier_amount`,
  MODIFY COLUMN `store_amount` bigint NULL COMMENT '铺货金额' AFTER `goods_amount`,
  MODIFY COLUMN `freight_amount` bigint NULL COMMENT '选品运费' AFTER `store_amount`,
  MODIFY COLUMN `custom_freight_amount` bigint NULL COMMENT '自营运费' AFTER `freight_amount`,
  MODIFY COLUMN `discount_amount` bigint NULL COMMENT '优惠金额' AFTER `custom_freight_amount`,
  MODIFY COLUMN `service_amount` bigint NULL COMMENT '服务费: 渠道商应付' AFTER `discount_amount`,
  MODIFY COLUMN `total_amount` bigint NULL COMMENT '渠道商待支付金额' AFTER `service_amount`,
  MODIFY COLUMN `member_amount` bigint NULL COMMENT 'C端待支付金额' AFTER `total_amount`,
  MODIFY COLUMN `order_state` int NULL COMMENT '订单状态',
ADD
  COLUMN `member_pay_state` int NULL COMMENT 'C端支付状态' AFTER `order_state`,
ADD
  COLUMN `channel_pay_state` int NULL COMMENT '渠道商支付状态' AFTER `member_pay_state`,
  MODIFY COLUMN `pay_time` datetime NULL COMMENT '支付时间' AFTER `channel_pay_state`,
  MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式' AFTER `pay_time`,
  MODIFY COLUMN `order_state_log` varchar(255) NULL COMMENT '订单状态流转日志(逗号分隔)' AFTER `pay_type`,
  MODIFY COLUMN `order_snap_v_o` json NULL COMMENT '订单快照' AFTER `order_state_log`,
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店ID' AFTER `order_snap_v_o`,
  MODIFY COLUMN `member_id` bigint NULL COMMENT 'C端会员ID' AFTER `store_id`,
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号id(account.id)' AFTER `member_id`,
  MODIFY COLUMN `pay_flow` varchar(255) NULL COMMENT '支付流水' AFTER `account_id`,
  MODIFY COLUMN `buy_mode` int NULL COMMENT '购买方式' AFTER `pay_flow`,
  MODIFY COLUMN `nickname` varchar(255) NULL COMMENT '用户名' AFTER `buy_mode`,
  MODIFY COLUMN `username` varchar(255) NULL COMMENT '账号' AFTER `nickname`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `username`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  UNIQUE INDEX `auto_idx_order_order_no`(`order_no`) COMMENT '交易单号',
ADD
  INDEX `auto_idx_order_operator_id`(`operator_id`) COMMENT '运营商ID',
ADD
  INDEX `auto_idx_order_channel_id`(`channel_id`) COMMENT '渠道商ID',
ADD
  INDEX `auto_idx_order_order_state`(`order_state`) COMMENT '订单状态',
ADD
  INDEX `auto_idx_order_pay_time`(`pay_time`) COMMENT '支付时间',
ADD
  INDEX `auto_idx_order_store_id`(`store_id`) COMMENT '门店ID',
ADD
  INDEX `auto_idx_order_member_id`(`member_id`) COMMENT 'C端会员ID',
ADD
  INDEX `auto_idx_order_account_id`(`account_id`) COMMENT '账号id(account.id)';
