CREATE TABLE `article_category` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '分类名称',
  `sort` int NULL COMMENT '排序值(小于100, 相同时按创建时间倒序)',
  `is_enabled` int NULL COMMENT '是否启用(0-禁用, 1-启用)',
  `recommend_groups` varchar(255) NULL COMMENT '推荐人群(逗号分隔)',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '文章分类数据实体';
