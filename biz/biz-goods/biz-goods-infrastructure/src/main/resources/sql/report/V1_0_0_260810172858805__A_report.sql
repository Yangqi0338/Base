ALTER TABLE
  `report` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称',
  MODIFY COLUMN `path` varchar(255) NULL COMMENT '路径',
ADD
  COLUMN `category_id_list` varchar(255) NULL COMMENT '分类id列表' AFTER `path`,
ADD
  COLUMN `spu_id_list` varchar(255) NULL COMMENT '商品id列表' AFTER `category_id_list`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `spu_id_list`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_report_category_id_list`(`category_id_list`) COMMENT '分类id列表',
ADD
  INDEX `auto_idx_report_spu_id_list`(`spu_id_list`) COMMENT '商品id列表',
  COMMENT = '测试报告';
