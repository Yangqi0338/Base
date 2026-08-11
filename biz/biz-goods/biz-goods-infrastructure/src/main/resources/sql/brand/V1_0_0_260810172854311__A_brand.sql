ALTER TABLE
  `brand` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称(查询)',
  MODIFY COLUMN `state` varchar(255) NULL COMMENT '状态' AFTER `logo`,
ADD
  COLUMN `category_id_list` varchar(255) NULL COMMENT '类目ID' AFTER `state`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `category_id_list`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_brand_account_id`(`account_id`) COMMENT '用户ID',
ADD
  INDEX `auto_idx_brand_state`(`state`) COMMENT '状态',
ADD
  INDEX `auto_idx_brand_category_id_list`(`category_id_list`) COMMENT '类目ID';
