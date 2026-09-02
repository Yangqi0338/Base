ALTER TABLE
  `user_follow` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `follower_id` bigint NULL COMMENT '关注者',
  MODIFY COLUMN `following_id` bigint NULL COMMENT '被关注者',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '用户关注持久化对象';
