ALTER TABLE
  `app_version` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `app_code` varchar(255) NULL COMMENT '应用编码',
  MODIFY COLUMN `app_name` varchar(255) NULL COMMENT 'app 名称',
  MODIFY COLUMN `app_version` varchar(255) NULL COMMENT 'app 版本',
  MODIFY COLUMN `update_config` int NULL COMMENT '更新配置',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `resource_url`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_app_version_app_code`(`app_code`) COMMENT '应用编码',
ADD
  INDEX `auto_idx_app_version_app_version`(`app_version`) COMMENT 'app 版本',
  COMMENT = 'app 版本管理数据对象';
