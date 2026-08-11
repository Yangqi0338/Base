ALTER TABLE
  `member_task_record` DROP COLUMN `is_delete`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `member_id` varchar(255) NULL COMMENT '会员ID',
  MODIFY COLUMN `member_nickname` varchar(255) NULL COMMENT '会员昵称',
  MODIFY COLUMN `task_num` varchar(255) NULL COMMENT '任务编号(关联 task_info.task_num)',
  MODIFY COLUMN `task_config_id` bigint NULL COMMENT '任务配置ID(关联 task_config 主键)',
  MODIFY COLUMN `task_name` varchar(255) NULL COMMENT '任务名称',
  MODIFY COLUMN `task_type` int NULL COMMENT '任务类型编码',
  MODIFY COLUMN `task_status` int NULL COMMENT '任务状态编码',
  MODIFY COLUMN `count` bigint NULL COMMENT '任务完成次数或金额' AFTER `task_status`,
  MODIFY COLUMN `complete_condition` varchar(255) NULL COMMENT '完成条件',
  MODIFY COLUMN `task_progress` varchar(255) NULL COMMENT '任务进度',
  MODIFY COLUMN `red_packet_reward` bigint NULL COMMENT '红包奖励(单位 0.0001 元，1元存 10000)',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `red_packet_reward`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_member_task_record_member_id`(`member_id`) COMMENT '会员ID',
ADD
  INDEX `auto_idx_member_task_record_task_num`(`task_num`) COMMENT '任务编号',
  COMMENT = '会员任务进度(member_task_record)持久化对象';
