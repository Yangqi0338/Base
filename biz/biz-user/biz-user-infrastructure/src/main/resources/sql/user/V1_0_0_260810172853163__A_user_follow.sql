ALTER TABLE
  `user_follow` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `follower_id` bigint NULL COMMENT '关注者',
  MODIFY COLUMN `following_id` bigint NULL COMMENT '被关注者',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `following_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  UNIQUE INDEX `auto_idx_idx_key`(`follower_id`, `following_id`),
  COMMENT = '用户关注持久化对象';
