CREATE TABLE `app_version` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `app_code` varchar(255) NULL COMMENT '应用编码',
  `app_name` varchar(255) NULL COMMENT 'app 名称',
  `app_version` varchar(255) NULL COMMENT 'app 版本',
  `update_config` int NULL COMMENT '更新配置',
  `resource_url` varchar(255) NULL COMMENT '资源地址',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_app_version_app_code`(`app_code`) COMMENT '应用编码',
  INDEX `auto_idx_app_version_app_version`(`app_version`) COMMENT 'app 版本'
) COMMENT = 'app 版本管理数据对象';
