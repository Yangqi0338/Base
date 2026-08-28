CREATE TABLE `dict_item` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `dict_id` bigint NULL COMMENT '父字典 id',
  `item_key` varchar(255) NULL COMMENT '条目键',
  `item_value` varchar(255) NULL COMMENT '条目值',
  `sort` int NULL COMMENT '排序(升序)',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_dict_item_dict_id`(`dict_id`) COMMENT '父字典 id',
  INDEX `auto_idx_dict_item_item_key`(`item_key`) COMMENT '条目键',
  INDEX `auto_idx_dict_item_sort`(`sort`) COMMENT '排序'
) COMMENT = '字典条目数据对象';
