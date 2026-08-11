ALTER TABLE
  `config_channel` DROP COLUMN `alter_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `platform_config` json NULL COMMENT '平台服务费',
  MODIFY COLUMN `operator_config` json NULL COMMENT '运营商服务费',
  MODIFY COLUMN `platform_now_value` double(6, 2) NULL COMMENT '平台当前服务费',
  MODIFY COLUMN `operator_now_value` double(6, 2) NULL COMMENT '运营商当前服务费',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息',
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id',
ADD
  COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  DROP PRIMARY KEY,
ADD
  PRIMARY KEY (`id`),
ADD
  INDEX `auto_idx_config_channel_channel_id`(`channel_id`) COMMENT '渠道商id',
  COMMENT = '渠道商服务费配置 DO';
