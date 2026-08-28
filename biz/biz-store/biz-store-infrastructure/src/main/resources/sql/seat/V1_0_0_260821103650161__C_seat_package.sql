CREATE TABLE `seat_package` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `seat_package_code` varchar(255) NULL COMMENT '席位套餐code',
  `seat_package_name` varchar(255) NULL COMMENT '席位套餐名称',
  `seat_num` int NULL COMMENT '席位个数',
  `package_price` bigint NULL COMMENT '套餐价格',
  `package_describe` varchar(255) NULL COMMENT '描述',
  `state` int NULL COMMENT '状态',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_seat_package_seat_package_code`(`seat_package_code`) COMMENT '席位套餐code',
  INDEX `auto_idx_seat_package_seat_package_name`(`seat_package_name`) COMMENT '席位套餐名称'
) COMMENT = '席位套餐领域对象';
