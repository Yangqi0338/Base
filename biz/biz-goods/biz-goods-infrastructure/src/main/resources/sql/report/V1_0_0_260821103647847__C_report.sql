CREATE TABLE `report` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '名称',
  `path` varchar(255) NULL COMMENT '路径',
  `category_id_list` varchar(255) NULL COMMENT '分类id列表',
  `spu_id_list` varchar(255) NULL COMMENT '商品id列表',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_report_category_id_list`(`category_id_list`) COMMENT '分类id列表',
  INDEX `auto_idx_report_spu_id_list`(`spu_id_list`) COMMENT '商品id列表'
) COMMENT = '测试报告';
