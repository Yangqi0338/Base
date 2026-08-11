ALTER TABLE
  `task_info` DROP COLUMN `task_type_name`,
  DROP COLUMN `is_deleted`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `task_num` varchar(255) NULL COMMENT '任务编号',
  MODIFY COLUMN `task_name` varchar(255) NULL COMMENT '任务名称',
  MODIFY COLUMN `task_id` bigint NULL COMMENT '任务配置ID(关联 task_config 主键)',
  MODIFY COLUMN `task_type` int NULL COMMENT '任务类型编码',
  MODIFY COLUMN `start_time` datetime NULL COMMENT '任务开始时间',
  MODIFY COLUMN `end_time` datetime NULL COMMENT '任务结束时间',
  MODIFY COLUMN `task_intro` varchar(255) NULL COMMENT '任务简介(最多200字)',
  MODIFY COLUMN `ad_count` int NULL COMMENT '完整观看广告数(仅观看激励广告类型有效)',
  MODIFY COLUMN `goods_list` varchar(255) NULL COMMENT '参与活动的商品集合',
  MODIFY COLUMN `complete_count` int NULL COMMENT '完成人数',
  MODIFY COLUMN `is_show` int NULL COMMENT '是否显示',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_show`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_task_info_task_num`(`task_num`) COMMENT '任务编号',
ADD
  INDEX `auto_idx_task_info_task_id`(`task_id`) COMMENT '任务配置ID',
  COMMENT = '营销任务(task_info)持久化对象';
