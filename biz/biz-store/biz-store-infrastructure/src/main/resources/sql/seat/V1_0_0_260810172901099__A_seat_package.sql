ALTER TABLE
  `seat_package` DROP COLUMN `deleted`,
  DROP COLUMN `create_id`,
  DROP COLUMN `create_name`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `seat_package_code` varchar(255) NULL COMMENT '席位套餐code',
  MODIFY COLUMN `seat_package_name` varchar(255) NULL COMMENT '席位套餐名称',
  MODIFY COLUMN `seat_num` int NULL COMMENT '席位个数',
  MODIFY COLUMN `package_price` bigint NULL COMMENT '套餐价格',
  MODIFY COLUMN `state` int NULL COMMENT '状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_seat_package_seat_package_code`(`seat_package_code`) COMMENT '席位套餐code',
ADD
  INDEX `auto_idx_seat_package_seat_package_name`(`seat_package_name`) COMMENT '席位套餐名称',
  COMMENT = '席位套餐领域对象';
