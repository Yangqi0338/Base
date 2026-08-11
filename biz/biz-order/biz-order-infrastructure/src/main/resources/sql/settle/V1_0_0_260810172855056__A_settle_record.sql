ALTER TABLE
  `settle_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `settle_time` datetime NULL COMMENT '结算时间(版本号)',
  MODIFY COLUMN `settle_money` bigint NULL COMMENT '结算金额',
  MODIFY COLUMN `goods_amount` bigint NULL COMMENT '货款金额' AFTER `settle_goods_num`,
  MODIFY COLUMN `freight_amount` bigint NULL COMMENT '运费金额' AFTER `goods_amount`,
  MODIFY COLUMN `refund_amount` bigint NULL COMMENT '售后金额' AFTER `freight_amount`,
  MODIFY COLUMN `label` varchar(255) NULL COMMENT '标签' AFTER `refund_amount`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `label`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_settle_record_supplier_id`(`supplier_id`) COMMENT '供应商ID',
ADD
  INDEX `auto_idx_settle_record_settle_time`(`settle_time`) COMMENT '结算时间(版本号)';
