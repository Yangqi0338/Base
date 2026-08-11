ALTER TABLE
  `model_shop_use_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `model_shop_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_model_shop_use_record_store_id`(`store_id`) COMMENT '门店id',
ADD
  INDEX `auto_idx_model_shop_use_record_model_shop_id`(`model_shop_id`) COMMENT '样板店ID',
  COMMENT = '样板店使用记录(DB 表 model_shop_use_record)';
