ALTER TABLE
  `pack_order` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '下单账号ID' AFTER `id`,
  CHANGE COLUMN ship_v_o `ship` json NULL COMMENT '收货信息' AFTER `account_id`,
  MODIFY COLUMN `pack_id` bigint NULL COMMENT '礼包商品ID' AFTER `ship`,
  MODIFY COLUMN `pack_type` bigint NULL COMMENT '礼包类型' AFTER `pack_id`,
  MODIFY COLUMN `pack_level` int NULL COMMENT '礼包等级' AFTER `pack_type`,
  MODIFY COLUMN `level_name` varchar(255) NULL COMMENT '礼包名称' AFTER `pack_level`,
  MODIFY COLUMN `amount` bigint NULL COMMENT '订单金额',
  MODIFY COLUMN `freight_company` varchar(255) NULL COMMENT '物流公司',
  MODIFY COLUMN `freight_code` varchar(255) NULL COMMENT '物流单号',
  MODIFY COLUMN `pack_order_item_list` json NULL COMMENT '订单明细',
  MODIFY COLUMN `deliver_time` datetime NULL COMMENT '发货时间',
  MODIFY COLUMN `last_freight_api_use_time` datetime NULL COMMENT '上次物流API调用时间' AFTER `deliver_time`,
  MODIFY COLUMN `state` int NULL COMMENT '订单状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_pack_order_account_id`(`account_id`) COMMENT '下单账号ID',
ADD
  INDEX `auto_idx_pack_order_pack_id`(`pack_id`) COMMENT '礼包商品ID',
ADD
  INDEX `auto_idx_pack_order_state`(`state`) COMMENT '订单状态',
  COMMENT = '入会礼包订单(pack_order)持久化对象';
