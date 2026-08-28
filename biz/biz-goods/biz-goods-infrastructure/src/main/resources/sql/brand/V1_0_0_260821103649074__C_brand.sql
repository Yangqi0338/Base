CREATE TABLE `brand` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `account_id` bigint NULL COMMENT '用户ID',
  `name` varchar(255) NULL COMMENT '名称(查询)',
  `logo` varchar(255) NULL COMMENT '图标',
  `state` varchar(255) NULL COMMENT '状态',
  `category_id_list` varchar(255) NULL COMMENT '类目ID',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_brand_account_id`(`account_id`) COMMENT '用户ID',
  INDEX `auto_idx_brand_category_id_list`(`category_id_list`) COMMENT '类目ID',
  INDEX `auto_idx_brand_state`(`state`) COMMENT '状态'
) COMMENT = '品牌';
