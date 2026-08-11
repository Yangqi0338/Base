ALTER TABLE
  `lecturer_category` DROP COLUMN `create_by`,
  DROP COLUMN `update_by`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `category_name` varchar(255) NULL COMMENT '分类名称',
  MODIFY COLUMN `icon_url` varchar(255) NULL COMMENT '图标URL',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_enabled`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '讲师分类数据对象';
