ALTER TABLE
  `audit_data_work_table` DROP COLUMN `spu_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `spu_name` varchar(255) NULL COMMENT '商品名称(查询)' AFTER `id`,
  MODIFY COLUMN `operate_type` int NULL COMMENT '操作类型',
  MODIFY COLUMN `operate_target` int NULL COMMENT '操作目标',
  MODIFY COLUMN `spu_info_json` json NULL COMMENT '原商品信息',
  MODIFY COLUMN `spu_edit_info_json` json NULL COMMENT '商品修改信息',
  MODIFY COLUMN `sku_sale_price_json` json NULL COMMENT 'sku销售价' AFTER `spu_edit_info_json`,
ADD
  COLUMN `state` int NULL COMMENT '审批状态' AFTER `sku_sale_price_json`,
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `state`,
  MODIFY COLUMN `flow_id` bigint NULL COMMENT '审批流id',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `flow_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_audit_data_work_table_operate_target`(`operate_target`) COMMENT '操作目标',
ADD
  INDEX `auto_idx_audit_data_work_table_foreign_id`(`foreign_id`) COMMENT '外键id',
ADD
  INDEX `auto_idx_audit_data_work_table_flow_id`(`flow_id`) COMMENT '审批流id';
