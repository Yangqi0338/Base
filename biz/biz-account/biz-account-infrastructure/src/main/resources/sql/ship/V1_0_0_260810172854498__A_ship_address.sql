ALTER TABLE
  `ship_address` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `ship_area` varchar(255) NULL COMMENT '收货地区, 例如: 辽宁省,沈阳市,铁西区,XXX镇',
  MODIFY COLUMN `ship_name` varchar(255) NULL COMMENT '收货联系人姓名',
  MODIFY COLUMN `ship_address` varchar(255) NULL COMMENT '收货地址, 如创业路东',
  MODIFY COLUMN `ship_zip_code` varchar(255) NULL COMMENT '收货邮编',
  MODIFY COLUMN `ship_province_code` int NULL COMMENT '收货地址编码, 省 CODE, 6 位',
  MODIFY COLUMN `ship_city_code` int NULL COMMENT '收货地址编码, 市 CODE, 6 位',
  MODIFY COLUMN `ship_area_code` int NULL COMMENT '收货地址编码, 区 CODE, 6 位',
  MODIFY COLUMN `is_default` int NULL COMMENT '是否默认(0 否, 1 是)',
  MODIFY COLUMN `role_id` bigint NULL COMMENT '角色类型 ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号 ID' AFTER `role_id`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `account_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_ship_address_role_id`(`role_id`) COMMENT '角色类型 ID',
ADD
  INDEX `auto_idx_ship_address_account_id`(`account_id`) COMMENT '账号 ID',
  COMMENT = '收货地址持久化对象';
