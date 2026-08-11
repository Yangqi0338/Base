ALTER TABLE
  `course_category` DROP COLUMN `is_deleted`,
  DROP COLUMN `create_by`,
  DROP COLUMN `update_by`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `category_code` varchar(255) NULL COMMENT '分类编码',
  MODIFY COLUMN `category_name` varchar(255) NULL COMMENT '分类名称',
  MODIFY COLUMN `sub_title` varchar(255) NULL COMMENT '小标题',
  MODIFY COLUMN `sort` int NULL COMMENT '排序值(越小越靠前)',
  MODIFY COLUMN `course_count` int NULL COMMENT '该分类下课程总数',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_enabled`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '课程分类数据对象';
