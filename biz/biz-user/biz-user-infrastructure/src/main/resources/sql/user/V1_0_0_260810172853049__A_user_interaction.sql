ALTER TABLE
  `user_interaction` DROP COLUMN `created_time`,
  DROP COLUMN `updated_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '操作人ID(关联用户表)',
  MODIFY COLUMN `publisher_id` bigint NULL COMMENT '被操作对象发布者ID(视频/商品的发布者，关联用户表)',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店ID' AFTER `publisher_id`,
  MODIFY COLUMN `target_type` varchar(255) NULL COMMENT '被操作对象类型',
  MODIFY COLUMN `target_id` bigint NULL COMMENT '被操作对象ID(视频ID或商品ID，与targetType对应)',
  MODIFY COLUMN `action_type` varchar(255) NULL COMMENT '操作类型',
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
  INDEX `auto_idx_user_interaction_user_id`(`user_id`) COMMENT '操作人ID',
ADD
  INDEX `auto_idx_user_interaction_publisher_id`(`publisher_id`) COMMENT '被操作对象发布者ID',
ADD
  INDEX `auto_idx_user_interaction_store_id`(`store_id`) COMMENT '门店ID',
ADD
  INDEX `auto_idx_user_interaction_target_type`(`target_type`) COMMENT '被操作对象类型',
ADD
  INDEX `auto_idx_user_interaction_target_id`(`target_id`) COMMENT '被操作对象ID',
  COMMENT = '用户互动操作持久化对象（存储点赞、转发记录）';
