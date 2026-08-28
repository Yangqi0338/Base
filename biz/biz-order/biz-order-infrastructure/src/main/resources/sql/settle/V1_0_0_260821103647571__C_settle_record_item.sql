CREATE TABLE `settle_record_item` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `settle_record_id` bigint NULL COMMENT '结算记录ID',
  `spu_id` bigint NULL COMMENT 'SPU_ID',
  `spu_name` varchar(255) NULL COMMENT 'spu名称',
  `spu_img` varchar(255) NULL COMMENT 'spu图片',
  `sku_settle_detail` varchar(255) NULL COMMENT 'sku订单结算信息',
  `settle_money` bigint NULL COMMENT '结算金额',
  `settle_goods_num` int NULL COMMENT '结算商品数量',
  `spu_freight` bigint NULL COMMENT '结算运费',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_settle_record_item_settle_record_id`(`settle_record_id`) COMMENT '结算记录ID',
  INDEX `auto_idx_settle_record_item_spu_id`(`spu_id`) COMMENT 'SPU_ID'
) COMMENT = '结算记录明细表';
