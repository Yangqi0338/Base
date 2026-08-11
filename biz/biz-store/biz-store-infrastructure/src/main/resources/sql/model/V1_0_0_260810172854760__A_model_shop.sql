ALTER TABLE
  `model_shop` DROP COLUMN `model_logo`,
  DROP COLUMN `version_no`,
  DROP COLUMN `version_description`,
  DROP COLUMN `parent_model`,
  DROP COLUMN `version_num`,
  DROP COLUMN `condition_list`,
  DROP COLUMN `create_id`,
  DROP COLUMN `create_name`,
  DROP COLUMN `is_delete`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `model_shop_name` varchar(255) NULL COMMENT '样板店名称',
  MODIFY COLUMN `earning_config` json NULL COMMENT '分润配置',
  MODIFY COLUMN `channel_earning` bigint NULL COMMENT '渠道商收益',
  MODIFY COLUMN `total_earning` bigint NULL COMMENT '总收益',
  MODIFY COLUMN `audit_state` int NULL COMMENT '审核状态',
  MODIFY COLUMN `style_code` varchar(255) NULL COMMENT '样式code' AFTER `audit_info`,
  MODIFY COLUMN `use_store_num` int NULL COMMENT '使用门店数' AFTER `style_code`,
  MODIFY COLUMN `total_use_store_num` int NULL COMMENT '累计使用门店数' AFTER `use_store_num`,
  MODIFY COLUMN `total_order_amount` bigint NULL COMMENT '累计下单金额' AFTER `total_use_store_num`,
  MODIFY COLUMN `total_order_num` int NULL COMMENT '累计下单数' AFTER `total_order_amount`,
  MODIFY COLUMN `total_pay_amount` bigint NULL COMMENT '累计支付金额' AFTER `total_order_num`,
  MODIFY COLUMN `total_pay_num` int NULL COMMENT '累计支付订单数' AFTER `total_pay_amount`,
  MODIFY COLUMN `state` int NULL COMMENT '状态:0正常，1已禁用' AFTER `total_pay_num`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_model_shop_channel_id`(`channel_id`) COMMENT '渠道商id',
ADD
  INDEX `auto_idx_model_shop_model_shop_name`(`model_shop_name`) COMMENT '样板店名称',
ADD
  INDEX `auto_idx_model_shop_operator_id`(`operator_id`) COMMENT '运营商id',
ADD
  INDEX `auto_idx_model_shop_audit_state`(`audit_state`) COMMENT '审核状态',
ADD
  INDEX `auto_idx_model_shop_style_code`(`style_code`) COMMENT '样式code';
