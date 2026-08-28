CREATE TABLE `user_lecturer_follow` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NULL COMMENT '用户ID',
  `lecturer_id` bigint NULL COMMENT '讲师ID(关联讲师表主键)',
  `follow_time` datetime NULL COMMENT '关注时间',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '用户关注讲师数据对象';
