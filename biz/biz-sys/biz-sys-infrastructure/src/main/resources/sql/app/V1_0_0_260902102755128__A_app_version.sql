ALTER TABLE
  `app_version` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `app_code` varchar(255) NULL COMMENT '应用编码',
  MODIFY COLUMN `app_name` varchar(255) NULL COMMENT 'app 名称',
  MODIFY COLUMN `app_version` varchar(255) NULL COMMENT 'app 版本',
  MODIFY COLUMN `update_config` int NULL COMMENT '更新配置',
  MODIFY COLUMN `resource_url` varchar(255) NULL COMMENT '资源地址',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = 'app 版本管理数据对象';
