CREATE TABLE `settle_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `supplier_id` bigint NULL COMMENT '供应商ID',
  `settle_time` datetime NULL COMMENT '结算时间(版本号)',
  `settle_money` bigint NULL COMMENT '结算金额',
  `settle_goods_num` int NULL COMMENT '结算商品数量',
  `goods_amount` bigint NULL COMMENT '货款金额',
  `freight_amount` bigint NULL COMMENT '运费金额',
  `refund_amount` bigint NULL COMMENT '售后金额',
  `label` varchar(255) NULL COMMENT '标签',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_settle_record_settle_time`(`settle_time`) COMMENT '结算时间(版本号)',
  INDEX `auto_idx_settle_record_supplier_id`(`supplier_id`) COMMENT '供应商ID'
) COMMENT = '结算记录表';
