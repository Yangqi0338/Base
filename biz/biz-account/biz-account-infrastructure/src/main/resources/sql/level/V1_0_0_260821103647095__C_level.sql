CREATE TABLE `level` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `type` bigint NULL COMMENT '类型(角色 ID)',
  `enable` int NULL COMMENT '是否开启(0 否, 1 是)',
  `value` int NULL COMMENT '等级值(1 2 3 ...)',
  `name` varchar(255) NULL COMMENT '等级名称',
  `permission` json NULL COMMENT '等级权限(JSON 列)',
  `condition` json NULL COMMENT '升级条件(JSON 列)',
  `condition_judge_type` int NULL COMMENT '条件判断类型(0 满足任意一项, 1 全部满足)',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_level_type`(`type`) COMMENT '类型'
) COMMENT = '等级持久化对象';
