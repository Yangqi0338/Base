CREATE TABLE `settle_goods` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `supplier_id` bigint NULL COMMENT '供应商ID',
  `spu_id` bigint NULL COMMENT 'SPU_ID',
  `next_settle_time` datetime NULL COMMENT '下次结算时间',
  `settle_num` int NULL COMMENT '结算次数',
  `settle_money` bigint NULL COMMENT '结算金额',
  `settle_goods_num` int NULL COMMENT '结算商品数量',
  `up_num` int NULL COMMENT '账期修改次数',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '结算商品信息表';
