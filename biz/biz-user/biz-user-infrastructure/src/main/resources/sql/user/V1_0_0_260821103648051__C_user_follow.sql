CREATE TABLE `user_follow` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `follower_id` bigint NULL COMMENT '关注者',
  `following_id` bigint NULL COMMENT '被关注者',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `auto_idx_idx_key`(`follower_id`, `following_id`)
) COMMENT = '用户关注持久化对象';
