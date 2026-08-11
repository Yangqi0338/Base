ALTER TABLE
  `user_lecturer_follow` DROP COLUMN `is_deleted`,
  DROP COLUMN `create_by`,
  DROP COLUMN `update_by`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '用户ID',
  MODIFY COLUMN `lecturer_id` bigint NULL COMMENT '讲师ID(关联讲师表主键)',
  MODIFY COLUMN `follow_time` datetime NULL COMMENT '关注时间',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `follow_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '用户关注讲师数据对象';
