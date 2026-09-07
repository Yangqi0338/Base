ALTER TABLE
  `user_collection` CHANGE COLUMN store_distribution_id `store_goods_id` bigint NULL COMMENT '铺货表ID' AFTER `store_name`,
  DROP INDEX `auto_idx_user_collection_store_distribution_id`,
ADD
  INDEX `auto_idx_user_collection_store_goods_id`(`store_goods_id`) COMMENT '铺货表ID';
