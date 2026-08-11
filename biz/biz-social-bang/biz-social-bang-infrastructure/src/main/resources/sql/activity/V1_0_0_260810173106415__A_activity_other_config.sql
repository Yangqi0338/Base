ALTER TABLE
  `activity_other_config` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `show_config` varchar(255) NULL COMMENT '显示配置',
  MODIFY COLUMN `config_details` varchar(255) NULL COMMENT '具体配置',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `config_details`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '活动其他配置';
