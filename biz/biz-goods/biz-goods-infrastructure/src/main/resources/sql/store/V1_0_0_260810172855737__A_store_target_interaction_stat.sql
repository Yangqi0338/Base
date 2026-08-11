ALTER TABLE
  `store_target_interaction_stat` DROP COLUMN `created_time`,
  DROP COLUMN `updated_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店ID',
  MODIFY COLUMN `publisher_id` bigint NULL COMMENT '发布者ID',
  MODIFY COLUMN `target_type` varchar(255) NULL COMMENT '互动对象类型',
  MODIFY COLUMN `target_id` bigint NULL COMMENT '互动对象ID',
  MODIFY COLUMN `view_count` int NULL COMMENT '浏览次数',
  MODIFY COLUMN `like_count` int NULL COMMENT '点赞次数',
  MODIFY COLUMN `share_count` int NULL COMMENT '分享次数',
  MODIFY COLUMN `ext_json` json NULL COMMENT '扩展信息(JSON)',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息',
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id',
ADD
  COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_target_interaction_stat_store_id`(`store_id`) COMMENT '门店ID',
ADD
  INDEX `auto_idx_store_target_interaction_stat_publisher_id`(`publisher_id`) COMMENT '发布者ID',
ADD
  INDEX `auto_idx_store_target_interaction_stat_target_type`(`target_type`) COMMENT '互动对象类型',
ADD
  INDEX `auto_idx_store_target_interaction_stat_target_id`(`target_id`) COMMENT '互动对象ID',
  COMMENT = '门店-对象互动统计DO（数据库映射实体）';
