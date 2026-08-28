CREATE TABLE `deliver` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `spu_order_id` bigint NULL COMMENT 'SPU订单ID',
  `deliver_username` varchar(255) NULL COMMENT '发货人用户名',
  `express_company_name` varchar(255) NULL COMMENT '物流公司名称',
  `express_no` varchar(255) NULL COMMENT '物流单号',
  `express_mobile` varchar(255) NULL COMMENT '快递联系电话',
  `item` json NULL COMMENT '发货明细',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
);
