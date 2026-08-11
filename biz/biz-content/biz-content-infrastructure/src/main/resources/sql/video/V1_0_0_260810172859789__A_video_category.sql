ALTER TABLE
  `video_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '分类名称',
  MODIFY COLUMN `sort` int NULL COMMENT '排序值(小于100)',
  MODIFY COLUMN `video_count` int NULL COMMENT '视频数量',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用(0-禁用, 1-启用)' AFTER `video_count`,
  MODIFY COLUMN `recommend_groups` varchar(255) NULL COMMENT '推荐人群(逗号分隔)' AFTER `is_enabled`,
  MODIFY COLUMN `weight` int NULL COMMENT '权重值' AFTER `recommend_groups`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `weight`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '视频分类数据实体';
