ALTER TABLE
  `user_task` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '用户id',
  MODIFY COLUMN `type` int NULL COMMENT '任务类型',
  MODIFY COLUMN `seq` int NULL COMMENT '序号',
  MODIFY COLUMN `ratio` double(6, 2) NULL COMMENT '比例',
  MODIFY COLUMN `count` int NULL COMMENT '完成次数',
  MODIFY COLUMN `foreign_id` bigint NULL COMMENT '外键ID',
  MODIFY COLUMN `status` varchar(255) NULL COMMENT '状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `status`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_user_task_account_id`(`account_id`) COMMENT '用户id',
  COMMENT = '用户任务(user_task)持久化对象';
