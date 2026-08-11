ALTER TABLE
  `audit_data_spu` DROP COLUMN `spu_id`,
  DROP COLUMN `inventory`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '商品名称',
  MODIFY COLUMN `category_id` bigint NULL COMMENT '商品分类',
  MODIFY COLUMN `category_name` varchar(255) NULL COMMENT '商品分类名称完整',
  MODIFY COLUMN `spu_create_info_json` json NULL COMMENT '商品创建信息',
  MODIFY COLUMN `sku_sale_price_json` json NULL COMMENT 'sku销售价' AFTER `spu_create_info_json`,
  MODIFY COLUMN `admin_user_name` varchar(255) NULL COMMENT 'sku审批人值对象' AFTER `sku_sale_price_json`,
  MODIFY COLUMN `brand_name` varchar(255) NULL COMMENT '品牌名称' AFTER `admin_user_name`,
  MODIFY COLUMN `supply_price` bigint NULL COMMENT '供货价' AFTER `brand_name`,
  MODIFY COLUMN `market_price` bigint NULL COMMENT '市场价' AFTER `supply_price`,
  MODIFY COLUMN `unit_price` bigint NULL COMMENT '建议零售价' AFTER `market_price`,
ADD
  COLUMN `state` int NULL COMMENT '审批状态' AFTER `unit_price`,
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `state`,
  MODIFY COLUMN `flow_id` bigint NULL COMMENT '审批流id',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `flow_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_audit_data_spu_category_id`(`category_id`) COMMENT '商品分类',
ADD
  INDEX `auto_idx_audit_data_spu_foreign_id`(`foreign_id`) COMMENT '外键id',
ADD
  INDEX `auto_idx_audit_data_spu_flow_id`(`flow_id`) COMMENT '审批流id';
