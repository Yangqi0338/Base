CREATE TABLE `config_channel` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `channel_id` bigint NULL COMMENT '渠道商id',
  `platform_config` json NULL COMMENT '平台服务费',
  `operator_config` json NULL COMMENT '运营商服务费',
  `platform_now_value` double(6, 2) NULL COMMENT '平台当前服务费',
  `operator_now_value` double(6, 2) NULL COMMENT '运营商当前服务费',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_config_channel_channel_id`(`channel_id`) COMMENT '渠道商id'
) COMMENT = '渠道商服务费配置 DO';
