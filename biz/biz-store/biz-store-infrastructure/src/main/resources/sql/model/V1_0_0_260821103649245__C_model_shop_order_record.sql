CREATE TABLE `model_shop_order_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `store_id` bigint NULL COMMENT '门店id',
  `model_shop_id` bigint NULL COMMENT '样板店ID',
  `type` varchar(255) NULL COMMENT '订单类型',
  `amount` bigint NULL COMMENT '金额',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_model_shop_order_record_model_shop_id`(`model_shop_id`) COMMENT '样板店ID',
  INDEX `auto_idx_model_shop_order_record_store_id`(`store_id`) COMMENT '门店id'
) COMMENT = '样板店订单记录(DB 表 model_shop_order_record)';
