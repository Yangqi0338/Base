ALTER TABLE
  `good_package` DROP COLUMN `create_id`,
  DROP COLUMN `create_name`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `package_id` varchar(255) NULL COMMENT '套餐业务编码(唯一)',
  MODIFY COLUMN `goods_num` bigint NULL COMMENT '商品席位数',
  MODIFY COLUMN `package_price` bigint NULL COMMENT '套餐价格',
  MODIFY COLUMN `state` int NULL COMMENT '状态(1 启用，0 停用)',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '商品-套餐数据对象';
