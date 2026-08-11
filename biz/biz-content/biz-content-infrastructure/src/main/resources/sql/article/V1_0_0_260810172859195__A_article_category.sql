ALTER TABLE
  `article_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '分类名称',
  MODIFY COLUMN `sort` int NULL COMMENT '排序值(小于100, 相同时按创建时间倒序)',
  MODIFY COLUMN `article_count` int NULL COMMENT '文章数量',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用(0-禁用, 1-启用)' AFTER `article_count`,
  MODIFY COLUMN `recommend_groups` varchar(255) NULL COMMENT '推荐人群(逗号分隔)' AFTER `is_enabled`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `recommend_groups`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '文章分类数据实体';
