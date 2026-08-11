ALTER TABLE
  `level` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `type` int NULL COMMENT '类型(角色 ID)',
  MODIFY COLUMN `enable` int NULL COMMENT '是否开启(0 否, 1 是)',
  MODIFY COLUMN `value` int NULL COMMENT '等级值(1 2 3 ...)' AFTER `enable`,
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '等级名称',
  MODIFY COLUMN `permission` json NULL COMMENT '等级权限(JSON 列)',
  MODIFY COLUMN `condition` json NULL COMMENT '升级条件(JSON 列)' AFTER `permission`,
ADD
  COLUMN `condition_judge_type` int NULL COMMENT '条件判断类型(0 满足任意一项, 1 全部满足)' AFTER `condition`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `condition_judge_type`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_level_type`(`type`) COMMENT '类型',
  COMMENT = '等级持久化对象';
