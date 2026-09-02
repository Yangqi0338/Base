ALTER TABLE
  `video_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '分类名称',
  MODIFY COLUMN `sort` int NULL COMMENT '排序值(小于100)',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用(0-禁用, 1-启用)[1是,0否]',
  MODIFY COLUMN `recommend_groups` varchar(255) NULL COMMENT '推荐人群(逗号分隔)',
  MODIFY COLUMN `weight` int NULL COMMENT '权重值',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '视频分类数据实体';
