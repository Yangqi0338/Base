ALTER TABLE
  `task_config` DROP COLUMN `task_type_name`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `task_type` int NULL COMMENT '任务类型',
  MODIFY COLUMN `task_group` int NULL COMMENT '任务分组编码',
  MODIFY COLUMN `task_group_name` varchar(255) NULL COMMENT '任务分组名称',
  MODIFY COLUMN `task_count` int NULL COMMENT '任务数量(关联的任务条数)',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用' AFTER `task_count`,
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '备注信息' AFTER `is_enabled`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `remark`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_task_config_task_group`(`task_group`) COMMENT '任务分组编码',
  COMMENT = '营销任务类型(task_config)持久化对象';
