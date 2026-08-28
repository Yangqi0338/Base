CREATE TABLE `lecturer_category` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `category_name` varchar(255) NULL COMMENT '分类名称',
  `icon_url` varchar(255) NULL COMMENT '图标URL',
  `is_enabled` int NULL COMMENT '是否启用',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '讲师分类数据对象';
