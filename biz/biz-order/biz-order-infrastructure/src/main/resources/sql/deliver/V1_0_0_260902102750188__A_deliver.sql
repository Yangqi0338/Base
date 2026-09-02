ALTER TABLE
  `deliver` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `spu_order_id` bigint NULL COMMENT 'SPU订单ID',
  MODIFY COLUMN `deliver_username` varchar(255) NULL COMMENT '发货人用户名',
  MODIFY COLUMN `express_company_name` varchar(255) NULL COMMENT '物流公司名称',
  MODIFY COLUMN `express_no` varchar(255) NULL COMMENT '物流单号',
  MODIFY COLUMN `express_mobile` varchar(255) NULL COMMENT '快递联系电话',
  MODIFY COLUMN `item` json NULL COMMENT '发货明细',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
