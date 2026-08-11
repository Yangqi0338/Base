ALTER TABLE
  `user_collection` DROP COLUMN `is_deleted`,
  DROP COLUMN `collection_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '用户ID',
  MODIFY COLUMN `user_name` varchar(255) NULL COMMENT '用户名称',
  MODIFY COLUMN `store_distribution_id` bigint NULL COMMENT '铺货表ID' AFTER `store_name`,
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'SPU ID' AFTER `store_distribution_id`,
  MODIFY COLUMN `spu_name` varchar(255) NULL COMMENT 'SPU 名称(快照)' AFTER `spu_id`,
  MODIFY COLUMN `sku_id` bigint NULL COMMENT 'SKU ID' AFTER `spu_name`,
  MODIFY COLUMN `sku_name` varchar(255) NULL COMMENT 'SKU 名称(快照)' AFTER `sku_id`,
  MODIFY COLUMN `price` bigint NULL COMMENT '商品价格(快照)',
  MODIFY COLUMN `main_image` varchar(255) NULL COMMENT '商品主图URL(快照)',
  MODIFY COLUMN `is_valid` int NULL COMMENT '是否有效',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_valid`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_user_collection_user_id`(`user_id`) COMMENT '用户ID',
ADD
  INDEX `auto_idx_user_collection_store_id`(`store_id`) COMMENT '门店ID',
ADD
  INDEX `auto_idx_user_collection_store_distribution_id`(`store_distribution_id`) COMMENT '铺货表ID',
ADD
  INDEX `auto_idx_user_collection_spu_id`(`spu_id`) COMMENT 'SPU ID',
ADD
  INDEX `auto_idx_user_collection_sku_id`(`sku_id`) COMMENT 'SKU ID',
  COMMENT = '用户商品收藏持久化对象';
