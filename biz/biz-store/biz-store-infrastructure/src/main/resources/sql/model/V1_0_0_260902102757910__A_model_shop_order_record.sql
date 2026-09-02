ALTER TABLE
  `model_shop_order_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
  MODIFY COLUMN `model_shop_id` bigint NULL COMMENT '样板店ID',
  MODIFY COLUMN `type` varchar(255) NULL COMMENT '订单类型',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '样板店订单记录(DB 表 model_shop_order_record)';
